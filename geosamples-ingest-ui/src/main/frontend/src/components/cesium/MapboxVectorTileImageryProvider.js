/* eslint-disable no-underscore-dangle,camelcase,no-bitwise */
import {
  GeographicTilingScheme,
  // WebMercatorTilingScheme,
  Rectangle,
  Event,
  defined,
  defaultValue,
  DeveloperError,
  // Cartographic,
  // Cartesian2,
  BoundingRectangle,
  Intersect,
  WindingOrder,
  Resource,
  Math as CesiumMath,
  // EllipsoidGeodesic,
  // Ellipsoid,
  // Cartesian3,
} from 'cesium';
import { VectorTile } from '@mapbox/vector-tile';
import Protobuf from 'pbf';
import Point from '@mapbox/point-geometry';
import URITemplate from 'urijs/src/URITemplate';

const POLYGON_FEATURE = 3; // feature.type == 3 for polygon features
const LINESTRNG_FEATURE = 2;

const loadArrayBuffer = (urlOrResource) => Resource.fetchArrayBuffer(urlOrResource);

// const tile2lon = (x, z) => {
//   const twoToZoom = 1 << z;
//   const numXTiles = 2 * twoToZoom;
//   const degreesPerTile = 360 / numXTiles;
//   return (x * degreesPerTile) - 180;
// };
//
// const tile2lat = (y, z) => {
//   const twoToZoom = 1 << z;
//   const numXTiles = 2 * twoToZoom;
//   const numYTiles = numXTiles / 2;
//   const degreesPerTile = 180 / numYTiles;
//   return 90 - (y * degreesPerTile);
// };

/**
 * Determine the winding order of a polygon ring.
 * See https://github.com/mapbox/vector-tile-spec/tree/master/2.0#4344-polygon-geometry-type && https://en.wikipedia.org/wiki/Shoelace_formula
 * @param  {Point[]} ring The polygon ring as an array of '@mapbox/point-geometry' Points (or any points conforming to {x: number, y: number}).
 * @return {WindingOrder} The winding order of the polygon ring.
 */
const computeRingWindingOrder = (ring) => {
  const n = ring.length;
  let twiceArea = ring[n - 1].x * (ring[0].y - ring[n - 2].y)
    + ring[0].x * (ring[1].y - ring[n - 1].y);
  for (let i = 1; i <= n - 2; i++) {
    twiceArea += ring[i].x * (ring[i + 1].y - ring[i - 1].y);
  }
  // eslint-disable-next-line no-restricted-globals
  if (isNaN(twiceArea)) {
    throw new DeveloperError('Area was NaN');
  }
  return twiceArea > 0.0
    ? WindingOrder.COUNTER_CLOCKWISE
    : WindingOrder.CLOCKWISE;
};

// Use x,y,level vector tile to produce imagery for newX,newY,newLevel
const overzoomGeometry = (rings, nativeTile, newExtent, newTile) => {
  const diffZ = newTile.level - nativeTile.level;
  if (diffZ === 0) {
    return rings;
  }
  const newRings = [];
  // (offsetX, offsetY) is the (0,0) of the new tile
  const offsetX = newExtent * (newTile.x - (nativeTile.x << diffZ));
  const offsetY = newExtent * (newTile.y - (nativeTile.y << diffZ));
  for (let i = 0; i < rings.length; i++) {
    const ring = [];
    for (let i2 = 0; i2 < rings[i].length; i2++) {
      ring.push(rings[i][i2].sub(new Point(offsetX, offsetY)));
    }
    newRings.push(ring);
  }
  return newRings;
};

const isExteriorRing = (ring) => {
  // Normally an exterior ring would be clockwise but because these coordinates are in "canvas space" the ys are inverted
  // hence check for counter-clockwise ring
  const windingOrder = computeRingWindingOrder(ring);
  return windingOrder === WindingOrder.COUNTER_CLOCKWISE;
};

// Adapted from npm package "point-in-polygon" by James Halliday
// Licence included in LICENSE.md
const inside = (point, vs) => {
  // ray-casting algorithm based on
  // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html

  const x = point.x;

  const y = point.y;

  // eslint-disable-next-line no-shadow
  let inside = false;
  for (let i = 0, j = vs.length - 1; i < vs.length; j = i++) {
    const xi = vs[i].x;

    const yi = vs[i].y;
    const xj = vs[j].x;

    const yj = vs[j].y;

    // eslint-disable-next-line no-mixed-operators
    const intersect = yi > y !== yj > y && x < ((xj - xi) * (y - yi)) / (yj - yi) + xi;
    if (intersect) inside = !inside;
  }

  return inside;
};

// According to the Mapbox Vector Tile specifications, a polygon consists of one exterior ring followed by 0 or more interior rings. Therefore:
// for each ring:
//   if point in ring:
//     for each interior ring (following the exterior ring):
//       check point in interior ring
//     if point not in any interior rings, feature is clicked
const isFeatureClicked = (rings, point) => {
  for (let i = 0; i < rings.length; i++) {
    if (inside(point, rings[i])) {
      // Point is in an exterior ring
      // Check whether point is in any interior rings
      let inInteriorRing = false;
      while (i + 1 < rings.length && !isExteriorRing(rings[i + 1])) {
        i++;
        if (!inInteriorRing && inside(point, rings[i])) {
          inInteriorRing = true;
          // Don't break. Still need to iterate over the rest of the interior rings but don't do point-in-polygon tests on those
        }
      }
      // Point is in exterior ring, but not in any interior ring. Therefore point is in the feature region
      if (!inInteriorRing) {
        return true;
      }
    }
  }
  return false;
};

const minDistance = (a, b, e) => {
  const ab = { x: b.x - a.x, y: b.y - a.y };
  const be = { x: e.x - b.x, y: e.y - b.y };
  const ae = { x: e.x - a.x, y: e.y - a.y };

  const abbe = (ab.x * be.x + ab.y * be.y);
  const abae = (ab.z * ae.z + ab.y * ae.y);

  let ans;
  if (abbe > 0) {
    const y = e.y - b.y;
    const x = e.x - b.x;
    ans = Math.sqrt(x * x + y * y);
  } if (abae < 0) {
    const y = e.y - a.y;
    const x = e.x - a.x;
    ans = Math.sqrt(x * x + y * y);
  } else {
    const x1 = ab.x;
    const y1 = ab.y;
    const x2 = ae.x;
    const y2 = ae.y;
    const mod = Math.sqrt(x1 * x1 + y1 * y1);
    ans = Math.abs(x1 * y2 - y1 * x2) / mod;
  }

  return ans;
};

const isLineClicked = (segments, point) => {
  let last;
  for (let i = 0; i < segments.length; i++) {
    for (let j = 0; j < segments[i].length; j++) {
      const next = segments[i][j];
      if (!last) {
        last = next;
      } else {
        const d = minDistance(last, next, point);
        if (d < 3) {
          return true;
        }
      }
    }
    last = undefined;
  }
  return false;
};

// const measureDistance = (c1, c2) => {
//   const start = Ellipsoid.WGS84.cartesianToCartographic(c1);
//   const end = Ellipsoid.WGS84.cartesianToCartographic(c2);
//   const geodesic = new EllipsoidGeodesic(start, end, Ellipsoid.WGS84);
//   return geodesic.surfaceDistance;
// };

const defaultDeaderFunc = () => ({});

class MapboxVectorTileImageryProvider {
  constructor(options) {
    this._uriTemplate = new URITemplate(options.url);
    this._headerFunc = options.headerFunc || defaultDeaderFunc;

    // if (typeof options.layerName !== 'string') {
    //   throw new DeveloperError('Layer name is required');
    // }
    // this._layerName = options.layerName;

    this._subdomains = defaultValue(options.subdomains, []);

    if (!(options.styleFunc instanceof Function)) {
      throw new DeveloperError('Styles are required');
    }
    this._styleFunc = options.styleFunc;

    this._tilingScheme = new GeographicTilingScheme(); // WebMercatorTilingScheme(); //

    this._tileWidth = 256;
    this._tileHeight = 256;

    this._minimumLevel = defaultValue(options.minimumZoom, 0);
    this._maximumLevel = defaultValue(options.maximumZoom, Infinity);
    this._maximumNativeLevel = defaultValue(
      options.maximumNativeZoom,
      this._maximumLevel,
    );

    this._rectangle = defined(options.rectangle)
      ? Rectangle.intersection(options.rectangle, this._tilingScheme.rectangle)
      : this._tilingScheme.rectangle;
    this._uniqueIdProp = options.uniqueIdProp;
    this._featureInfoFunc = options.featureInfoFunc;
    // this._featurePicking = options.featurePicking;

    // Check the number of tiles at the minimum level.  If it's more than four,
    // throw an exception, because starting at the higher minimum
    // level will cause too many tiles to be downloaded and rendered.
    const swTile = this._tilingScheme.positionToTileXY(
      Rectangle.southwest(this._rectangle),
      this._minimumLevel,
    );
    const neTile = this._tilingScheme.positionToTileXY(
      Rectangle.northeast(this._rectangle),
      this._minimumLevel,
    );
    const tileCount = (Math.abs(neTile.x - swTile.x) + 1) * (Math.abs(neTile.y - swTile.y) + 1);
    if (tileCount > 4) {
      throw new DeveloperError(`Minimum level has more than four tiles: ${tileCount}`);
    }

    this._errorEvent = new Event();

    this._ready = true;
  }

  get url() {
    return this._uriTemplate.expression;
  }

  /**
   * Gets a value indicating whether or not the provider is ready for use.
   * @memberof ImageryProvider.prototype
   * @type {Boolean}
   * @readonly
   */
  get ready() {
    return this._ready;
  }

  /**
   * Gets a promise that resolves to true when the provider is ready for use.
   * @memberof ImageryProvider.prototype
   * @type {Promise.<Boolean>}
   * @readonly
   */
  // eslint-disable-next-line class-methods-use-this
  get readyPromise() {
    return undefined;
  }

  /**
   * Gets the rectangle, in radians, of the imagery provided by the instance.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {Rectangle}
   * @readonly
   */
  get rectangle() {
    return this._rectangle;
  }

  /**
   * Gets the width of each tile, in pixels.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {Number}
   * @readonly
   */
  get tileWidth() {
    return this._tileWidth;
  }

  /**
   * Gets the height of each tile, in pixels.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {Number}
   * @readonly
   */
  get tileHeight() {
    return this._tileHeight;
  }

  /**
   * Gets the maximum level-of-detail that can be requested.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {Number}
   * @readonly
   */
  get maximumLevel() {
    return this._maximumLevel;
  }

  /**
   * Gets the minimum level-of-detail that can be requested.  This function should
   * not be called before {@link ImageryProvider#ready} returns true. Generally,
   * a minimum level should only be used when the rectangle of the imagery is small
   * enough that the number of tiles at the minimum level is small.  An imagery
   * provider with more than a few tiles at the minimum level will lead to
   * rendering problems.
   * @memberof ImageryProvider.prototype
   * @type {Number}
   * @readonly
   */
  get minimumLevel() {
    return this._minimumLevel;
  }

  /**
   * Gets the tiling scheme used by the provider.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {TilingScheme}
   * @readonly
   */
  get tilingScheme() {
    return this._tilingScheme;
  }

  /**
   * Gets the tile discard policy.  If not undefined, the discard policy is responsible
   * for filtering out "missing" tiles via its shouldDiscardImage function.  If this function
   * returns undefined, no tiles are filtered.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {TileDiscardPolicy}
   * @readonly
   */
  get tileDiscardPolicy() {
    if (!this.ready) {
      throw new DeveloperError('tileDiscardPolicy must not be called before the imagery provider is ready.');
    }
    return undefined;
  }

  /**
   * Gets an event that is raised when the imagery provider encounters an asynchronous error..  By subscribing
   * to the event, you will be notified of the error and can potentially recover from it.  Event listeners
   * are passed an instance of {@link TileProviderError}.
   * @memberof ImageryProvider.prototype
   * @type {Event}
   * @readonly
   */
  get errorEvent() {
    return this._errorEvent;
  }

  /**
   * Gets the credit to display when this imagery provider is active.  Typically this is used to credit
   * the source of the imagery. This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @memberof ImageryProvider.prototype
   * @type {Credit}
   * @readonly
   */
  get credit() {
    if (!this.ready) {
      throw new DeveloperError('credit must not be called before the imagery provider is ready.');
    }
    return undefined;
  }

  /**
   * Gets the proxy used by this provider.
   * @memberof ImageryProvider.prototype
   * @type {Proxy}
   * @readonly
   */
  // eslint-disable-next-line class-methods-use-this
  get proxy() {
    return undefined;
  }

  /**
   * Gets a value indicating whether or not the images provided by this imagery provider
   * include an alpha channel.  If this property is false, an alpha channel, if present, will
   * be ignored.  If this property is true, any images without an alpha channel will be treated
   * as if their alpha is 1.0 everywhere.  When this property is false, memory usage
   * and texture upload time are reduced.
   * @memberof ImageryProvider.prototype
   * @type {Boolean}
   * @readonly
   */
  // eslint-disable-next-line class-methods-use-this
  get hasAlphaChannel() {
    return true;
  }

  /**
   * Gets the credits to be displayed when a given tile is displayed.
   * @function
   *
   * @param {Number} x The tile X coordinate.
   * @param {Number} y The tile Y coordinate.
   * @param {Number} level The tile level;
   * @returns {Credit[]} The credits to be displayed when the tile is displayed.
   *
   * @exception {DeveloperError} <code>getTileCredits</code> must not be called before the imagery provider is ready.
   */
  // eslint-disable-next-line no-unused-vars
  getTileCredits(x, y, level) {
    if (!this.ready) {
      throw new DeveloperError('getTileCredits must not be called before the imagery provider is ready.');
    }
    return undefined;
  }

  _drawTile(requestedTile, nativeTile, tile, canvas) {
    // const layer = tile.layers[this._layerName];

    // if (!defined(layer)) {
    //   return canvas; // return blank canvas for blank tile
    // }

    // eslint-disable-next-line no-unused-vars,no-restricted-syntax
    for (const [layerName, layer] of Object.entries(tile.layers)) {
      const context = canvas.getContext('2d');
      // context.strokeStyle = 'black';
      // context.lineWidth = 1;

      let pos;

      let extentFactor = canvas.width / layer.extent; // Vector tile works with extent [0, 4095], but canvas is only [0,255]

      // Features
      for (let i = 0; i < layer.length; i++) {
        const feature = layer.feature(i);
        if (feature.type === POLYGON_FEATURE || feature.type === LINESTRNG_FEATURE) {
          this._styleFunc({ feature, context, layer });
          // if (!style) {
          //   // eslint-disable-next-line no-continue
          //   continue;
          // }
          // context.fillStyle = style.fillStyle;
          // context.strokeStyle = style.strokeStyle;
          // context.lineWidth = style.lineWidth;
          // context.lineJoin = style.lineJoin;
          context.beginPath();
          let coordinates;
          if (nativeTile.level !== requestedTile.level) {
            // Overzoom feature
            const bbox = feature.bbox(); // [w, s, e, n] bounding box
            const featureRect = new BoundingRectangle(
              bbox[0],
              bbox[1],
              bbox[2] - bbox[0],
              bbox[3] - bbox[1],
            );
            const levelDelta = requestedTile.level - nativeTile.level;
            const size = layer.extent >> levelDelta;
            if (size < 16) {
              // Tile has less less detail than 16x16
              throw new DeveloperError('Tile has less less detail than 16x16');
            }
            // eslint-disable-next-line no-bitwise
            const x1 = size * (requestedTile.x - (nativeTile.x << levelDelta)); //
            // eslint-disable-next-line no-bitwise
            const y1 = size * (requestedTile.y - (nativeTile.y << levelDelta));
            const tileRect = new BoundingRectangle(x1, y1, size, size);
            if (BoundingRectangle.intersect(featureRect, tileRect) === Intersect.OUTSIDE) {
              // eslint-disable-next-line no-continue
              continue;
            }
            extentFactor = canvas.width / size;
            coordinates = overzoomGeometry(
              feature.loadGeometry(),
              nativeTile,
              size,
              requestedTile,
            );
          } else {
            coordinates = feature.loadGeometry();
          }

          // Polygon rings
          // eslint-disable-next-line no-plusplus
          for (let i2 = 0; i2 < coordinates.length; i2++) {
            pos = coordinates[i2][0];
            context.moveTo(pos.x * extentFactor, pos.y * extentFactor);

            // Polygon ring points
            // eslint-disable-next-line no-plusplus
            for (let j = 1; j < coordinates[i2].length; j++) {
              pos = coordinates[i2][j];
              context.lineTo(pos.x * extentFactor, pos.y * extentFactor);
            }
          }
          context.stroke();
          if (feature.type === POLYGON_FEATURE) {
            context.fill();
          }
        }
        // else {
        //   console.log(
        //     `Unexpected geometry type: ${
        //       feature.type
        //     } in region map on tile ${
        //       [requestedTile.level, requestedTile.x, requestedTile.y].join('/')}`,
        //   );
        // }
      }
    }

    return canvas;
  }

  _buildImageUrl(x, y, level) {
    return this._uriTemplate.expand({ z: level, x, y });
  }

  _requestImage(x, y, level, canvas) {
    const requestedTile = { x, y, level };
    let nativeTile; // The level, x & y of the tile used to draw the requestedTile
    // Check whether to use a native tile or overzoom the largest native tile
    if (level > this._maximumNativeLevel) {
      // Determine which native tile to use
      const levelDelta = level - this._maximumNativeLevel;
      nativeTile = {
        x: x >> levelDelta,
        y: y >> levelDelta,
        level: this._maximumNativeLevel,
      };
    } else {
      nativeTile = requestedTile;
    }

    const that = this;
    const url = this._buildImageUrl(nativeTile.x, nativeTile.y, nativeTile.level);
    const headers = this._headerFunc();

    return loadArrayBuffer({ url, headers }).then((data) => that._drawTile(
      requestedTile,
      nativeTile,
      new VectorTile(new Protobuf(data)),
      canvas,
    ));
  }

  /**
   * Requests the image for a given tile.  This function should
   * not be called before {@link ImageryProvider#ready} returns true.
   * @function
   *
   * @param {Number} x The tile X coordinate.
   * @param {Number} y The tile Y coordinate.
   * @param {Number} level The tile level.
   * @param {Request} [request] The request object. Intended for internal use only.
   * @returns {Promise.<Image|Canvas>|undefined} A promise for the image that will resolve when the image is available, or
   *          undefined if there are too many active requests to the server, and the request
   *          should be retried later.  The resolved image may be either an
   *          Image or a Canvas DOM object.
   *
   * @exception {DeveloperError} <code>requestImage</code> must not be called before the imagery provider is ready.
   */
  // eslint-disable-next-line no-unused-vars
  requestImage(x, y, level, request) {
    const canvas = document.createElement('canvas');
    canvas.width = this._tileWidth;
    canvas.height = this._tileHeight;
    return this._requestImage(x, y, level, canvas);
  }

  /**
   * Asynchronously determines what features, if any, are located at a given longitude and latitude within
   * a tile.  This function should not be called before {@link ImageryProvider#ready} returns true.
   * This function is optional, so it may not exist on all ImageryProviders.
   *
   * @function
   *
   * @param {Number} x The tile X coordinate.
   * @param {Number} y The tile Y coordinate.
   * @param {Number} level The tile level.
   * @param {Number} longitude The longitude at which to pick features.
   * @param {Number} latitude  The latitude at which to pick features.
   * @return {Promise.<ImageryLayerFeatureInfo[]>|undefined} A promise for the picked features that will resolve when the asynchronous
   *                   picking completes.  The resolved value is an array of {@link ImageryLayerFeatureInfo}
   *                   instances.  The array may be empty if no features are found at the given location.
   *                   It may also be undefined if picking is not supported.
   *
   * @exception {DeveloperError} <code>pickFeatures</code> must not be called before the imagery provider is ready.
   */
  // eslint-disable-next-line no-unused-vars
  pickFeatures(x, y, level, longitude, latitude) {
    let nativeTile;
    let levelDelta;
    const requestedTile = { x, y, level };
    // Check whether to use a native tile or overzoom the largest native tile
    if (level > this._maximumNativeLevel) {
      // Determine which native tile to use
      levelDelta = level - this._maximumNativeLevel;
      nativeTile = {
        x: x >> levelDelta,
        y: y >> levelDelta,
        level: this._maximumNativeLevel,
      };
    } else {
      nativeTile = {
        x,
        y,
        level,
      };
    }

    const that = this;
    const url = this._buildImageUrl(nativeTile.x, nativeTile.y, nativeTile.level);
    const headers = this._headerFunc();

    return loadArrayBuffer({ url, headers }).then((data) => {
      // const layer = new VectorTile(new Protobuf(data)).layers[that._layerName];

      const features = [];

      // eslint-disable-next-line no-unused-vars,no-restricted-syntax
      for (const [layerName, layer] of Object.entries(new VectorTile(new Protobuf(data)).layers)) {
        // if (!defined(layer)) {
        //   return []; // return empty list of features for empty tile
        // }

        // const vt_range = [0, (layer.extent >> levelDelta) - 1];

        // const boundRect = that._tilingScheme.tileXYToNativeRectangle(x, y, level);
        const boundRect = that._tilingScheme.tileXYToRectangle(x, y, level);
        // console.log(br2);
        // const east = boundRect.east;
        const south = boundRect.south;
        // const north = boundRect.north;
        const west = boundRect.west;
        // const nw = new Cartesian3(west, north);

        // const x_range = [nw.x, se.x];
        // const y_range = [nw.y, se.y];
        // const x_range = [west, east];
        // const y_range = [north, south];

        // const tileX = tile2lon(x, level);
        // const tileY = tile2lat(y, level);
        //
        // const distanceX = measureDistance(ne, new Cartesian3(east, latitude));

        const pickedX = CesiumMath.toDegrees(longitude);
        const pickedY = CesiumMath.toDegrees(latitude);

        const degreesLeft = CesiumMath.toDegrees(west);
        const degreesBottom = CesiumMath.toDegrees(south);

        const degreesX = 360.0 / that._tilingScheme.getNumberOfXTilesAtLevel(level);
        const degreesY = 180.0 / that._tilingScheme.getNumberOfYTilesAtLevel(level);

        // const distanceY = measureDistance(nw, new Cartesian3(west, south));
        // const totalDistanceY = measureDistance(nw, new Cartesian3(west, south));
        const percentX = (pickedX - degreesLeft) / degreesX;
        const percentY = (degreesY - (pickedY - degreesBottom)) / degreesY;

        /*
        For example, if a tile has an extent of 4096, coordinate units within the tile refer to 1/4096th of its square
         dimensions. A coordinate of 0 is on the top or left edge of the tile, and a coordinate of 4096 is on the bottom
          or right edge. Coordinates from 1 through 4095 inclusive are fully within the extent of the tile, and
           coordinates less than 0 or greater than 4096 are fully outside the extent of the tile. A point at (1,10)
            or (4095,10) is within the extent of the tile. A point at (0,10) or (4096,10) is on the edge of the extent.
             A point at (-1,10) or (4097,10) is outside the extent of the tile.
         */
        const extent = layer.extent >> levelDelta;
        const point = new Point(Math.round(percentX * extent), Math.round(percentY * extent));

        // const pickedCart = Cesium.Ellipsoid.WGS84.cartesianToCartographic(Cartesian3.fromDegrees(longitude, latitude));
        // const cartNw = Cesium.Ellipsoid.WGS84.cartesianToCartographic(nw);
        // // var lon = Cesium.Math.toDegrees(carto.longitude);
        // // var lat = Cesium.Math.toDegrees(carto.latitude);

        // console.log({
        //   distanceX,
        //   distanceY,
        //   pickedX,
        //   pickedY,
        // });

        // const map = function (pos, in_x_range, in_y_range, out_x_range, out_y_range) {
        //   const offset = new Cartesian2();
        //   Cartesian2.subtract(
        //     pos,
        //     new Cartesian2(in_x_range[0], in_y_range[0]),
        //     offset,
        //   ); // Offset of point from bottom left corner of bounding box
        //   const scale = new Cartesian2(
        //     (out_x_range[1] - out_x_range[0]) / (in_x_range[1] - in_x_range[0]),
        //     (out_y_range[1] - out_y_range[0]) / (in_y_range[1] - in_y_range[0]),
        //   );
        //   return Cartesian2.add(
        //     Cartesian2.multiplyComponents(offset, scale, new Cartesian2()),
        //     new Cartesian2(out_x_range[0], out_y_range[0]),
        //     new Cartesian2(),
        //   );
        // };
        //
        // let pos = Cartesian2.fromCartesian3(
        //   that._tilingScheme.projection.project(
        //     new Cartographic(longitude, latitude),
        //   ),
        // );
        // pos = map(pos, x_range, y_range, vt_range, vt_range);
        //
        // const point = new Point(pos.x, pos.y);

        // const features = [];
        for (let i = 0; i < layer.length; i++) {
          const feature = layer.feature(i);
          const geom = overzoomGeometry(
            feature.loadGeometry(),
            nativeTile,
            layer.extent >> levelDelta,
            requestedTile,
          );
          if (feature.type === POLYGON_FEATURE && isFeatureClicked(geom, point)) {
            const featureInfo = that._featureInfoFunc({ feature, layer });
            if (defined(featureInfo)) {
              features.push(featureInfo);
            }
          } else if (feature.type === LINESTRNG_FEATURE && isLineClicked(geom, point)) {
            const featureInfo = that._featureInfoFunc({ feature, layer });
            if (defined(featureInfo)) {
              features.push(featureInfo);
            }
          }
        }
      }

      return features;
    });
  }
}

// MapboxVectorTileImageryProvider.prototype.createHighlightImageryProvider = function(
//   regionUniqueID
// ) {
//   var that = this;
//   var styleFunc = function(FID) {
//     if (regionUniqueID === FID) {
//       // No fill, but same style border as the regions, just thicker
//       var regionStyling = that._styleFunc(FID);
//       if (defined(regionStyling)) {
//         regionStyling.fillStyle = "rgba(0,0,0,0)";
//         regionStyling.lineJoin = "round";
//         regionStyling.lineWidth = Math.floor(
//           1.5 * defaultValue(regionStyling.lineWidth, 1) + 1
//         );
//         return regionStyling;
//       }
//     }
//     return undefined;
//   };
//   var imageryProvider = new MapboxVectorTileImageryProvider({
//     url: this._uriTemplate.expression,
//     layerName: this._layerName,
//     subdomains: this._subdomains,
//     rectangle: this._rectangle,
//     minimumZoom: this._minimumLevel,
//     maximumNativeZoom: this._maximumNativeLevel,
//     maximumZoom: this._maximumLevel,
//     uniqueIdProp: this._uniqueIdProp,
//     styleFunc: styleFunc
//   });
//   imageryProvider.pickFeatures = function() {
//     return undefined;
//   }; // Turn off feature picking
//   return imageryProvider;
// };

export default MapboxVectorTileImageryProvider;
