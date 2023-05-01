export const coordinates2WktPolygon = (swCoordinate, neCoordinate) => {
  if (swCoordinate && neCoordinate) {
    const swCoordinateParts = swCoordinate[0].split(',');
    const neCoordinateParts = neCoordinate[0].split(',');
    if (swCoordinateParts.length === 2 && neCoordinateParts.length === 2) {
      const swLon = swCoordinateParts[0].trim();
      const swLat = swCoordinateParts[1].trim();
      const neLon = neCoordinateParts[0].trim();
      const neLat = neCoordinateParts[1].trim();
      return `POLYGON((${swLon} ${swLat},${neLon} ${swLat},${neLon} ${neLat},${swLon} ${neLat},${swLon} ${swLat}))`;
    }
  }
  throw new Error('Invalid search coordinates');
};
