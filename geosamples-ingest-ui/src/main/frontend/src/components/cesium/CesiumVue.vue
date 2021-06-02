<template>
  <div class="cesium-map" ref="cs"/>
</template>

<script>
import * as Cesium from 'cesium';
import { GEOMETRY_FILTER_SERVICE_BASE_API, BASE_PATH } from '@/basePath';
import store from '@/store/store';
import MapboxVectorTileImageryProvider from './MapboxVectorTileImageryProvider';

export default {
  mounted() {
    this.viewer = new Cesium.Viewer(this.$refs.cs,
      {
        shadows: false,
        // geocoder: false,
        // baseLayerPicker: false,
        // imageryProvider: baseLayer,
        // selectionIndicator: false,
        // infoBox: false,
        timeline: false,
        navigationHelpButton: false,
        navigationInstructionsInitiallyVisible: false,
        fullscreenButton: false,
        homeButton: false,
        projectionPicker: false,
        sceneModePicker: false,
        scene3DOnly: true,
        skyBox: false,
        skyAtmosphere: false,
        contextOptions: {
          scene3DOnly: true,
          // contextOptions: {
          //   webgl: {
          //     // alpha: false,
          //     // depth: true,
          //     // stencil: false,
          //     antialias: true,
          //     // powerPreference: 'high-performance',
          //     // premultipliedAlpha: true,
          //     // preserveDrawingBuffer: false,
          //     // failIfMajorPerformanceCaveat: false,
          //   },
          //   allowTextureFilterAnisotropic: true,
          // },
        },
      });

    const { scene } = this.viewer;
    // scene.globe.maximumScreenSpaceError = 0.5;
    // scene.postProcessStages.fxaa.enabled = false;
    const layers = scene.imageryLayers;
    layers.addImageryProvider(
      // new Cesium.TileCoordinatesImageryProvider({
      //   tilingScheme: new Cesium.GeographicTilingScheme(),
      // }),
      new MapboxVectorTileImageryProvider({
        url: `${BASE_PATH}${GEOMETRY_FILTER_SERVICE_BASE_API}/geometry/mvt/{z}/{x}/{y}.pbf`,
        headerFunc: () => ({
          Authorization: `Bearer ${store.getters['user/token']}`,
        }),
        // layerName: 'SH1305',
        styleFunc: ({ context, feature, layer }) => {
          context.fillStyle = feature.properties.EXCLUDE ? 'rgba(255, 0, 0, 0.25)' : 'rgba(0, 255, 0, 0.25)';
          context.strokeStyle = layer.name === 'TERRITORIAL_SEAS' ? 'magenta' : 'yellow';
          context.lineWidth = 2;
        },
        // rectangle: this.rectangle,
        minimumZoom: 0,
        maximumNativeZoom: 5,
        maximumZoom: 28,
        uniqueIdProp: 'ID',
        featureInfoFunc: ({ feature, layer }) => {
          const featureInfo = new Cesium.ImageryLayerFeatureInfo();
          // if (Cesium.defined(mapboxVectorTileCatalogItem.nameProperty)) {
          //   featureInfo.name =
          //     feature.properties[mapboxVectorTileCatalogItem.nameProperty];
          // }
          featureInfo.configureDescriptionFromProperties({ ...feature.properties, DATA_SET: layer.name });
          featureInfo.name = feature.properties.SOVEREIGN_1;
          // featureInfo.properties = { ...feature.properties };
          // featureInfo.data = {
          //   id: feature.properties.ID,
          // }; // For highlight
          return featureInfo;
        },
      }),
    );

    // layers.addImageryProvider(
    //   new Cesium.TileCoordinatesImageryProvider({
    //     tilingScheme: new Cesium.GeographicTilingScheme(),
    //   }),
    // );
  },
};

</script>
