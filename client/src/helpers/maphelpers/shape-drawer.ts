import * as ol from "ol";
import {World} from "../../components/common/world.tsx";
import VectorLayer from "ol/layer/Vector";
import {Vector} from "ol/source";
import {Circle, Point, Polygon} from "ol/geom";
import {Coordinate} from "ol/coordinate";
import {ColorLike} from "ol/colorlike";
import {Fill, Icon, Stroke, Style} from "ol/style";
import {Feature} from "ol";
import VectorSource from "ol/source/Vector";
import marker from "../../assets/img/marker-icon.png"

export function drawSectors(map: ol.Map, sectors: World[]): VectorLayer<Vector<ol.Feature<Polygon>>>[] {
    const sectorLayers: VectorLayer<Vector<ol.Feature<Polygon>>>[] = [];
    sectors.forEach(sector => {
        const start = [sector.coordinateRange.start.x, sector.coordinateRange.start.y] as Coordinate;
        const end = [sector.coordinateRange.end.x, sector.coordinateRange.end.y] as Coordinate;
        const sectorLayer = drawRectangleWithStartAndEndPoint(map, start, end, "transparent", new Stroke({
            color: "rgba(0,0,0, 0.3)",
            width: 1
        }));
        sectorLayers.push(sectorLayer);
    });
    return sectorLayers;
}

export function drawDangerZones(map: ol.Map, sectors: World[]): VectorLayer<Vector<ol.Feature<Polygon>>>[] {
    const dangerZoneLayers: VectorLayer<Vector<ol.Feature<Polygon>>>[] = [];
    sectors.forEach(sector => {
        sector.dangerousAreas.forEach(area => {
            const start = [area.coordinateRange.start.x, area.coordinateRange.start.y] as Coordinate;
            const end = [area.coordinateRange.end.x, area.coordinateRange.end.y] as Coordinate;
            const dangerzone = drawRectangleWithStartAndEndPoint(map, start, end, "rgba(255,0,0,0.2)");
            dangerZoneLayers.push(dangerzone);
        })
    })
    return dangerZoneLayers;
}

export function getAdriaSize() {
    return 3000;
}

export function drawRectangle(mapObject: ol.Map, center: Coordinate, width: number, height: number, color?: ColorLike | string) {
    const halfWidth = width / 2;
    const halfHeight = height / 2;
    const coordinates = [
        [
            [center[0] - halfWidth, center[1] - halfHeight],
            [center[0] + halfWidth, center[1] - halfHeight],
            [center[0] + halfWidth, center[1] + halfHeight],
            [center[0] - halfWidth, center[1] + halfHeight],
            [center[0] - halfWidth, center[1] - halfHeight],
        ],
    ];

    const rectangle = new Polygon(coordinates);

    const rectangleFeature = new ol.Feature({
        geometry: rectangle,
    });

    const rectangleStyle = new Style({
        fill: new Fill({
            color: color ?? 'rgba(0, 0, 255, 0.5)',
        }),
    });
    rectangleFeature.setStyle(rectangleStyle);
    const layer = new VectorLayer({
        source: new Vector({
            features: [rectangleFeature],
        }),
    });

    mapObject.addLayer(layer);
    return rectangleFeature;
}

export function drawRectangleWithStartAndEndPoint(
    mapObject: ol.Map,
    start: Coordinate,
    end: Coordinate,
    color?: ColorLike | string,
    stroke?: Stroke
) {
    const coordinates = [
        [
            [start[0], start[1]],
            [end[0], start[1]],
            [end[0], end[1]],
            [start[0], end[1]],
            [start[0], start[1]],
        ],
    ];

    const rectangle = new Polygon(coordinates);

    const rectangleFeature = new Feature({
        geometry: rectangle,
    });

    const rectangleStyle = new Style({
        fill: new Fill({
            color: color ?? 'rgba(0, 0, 255, 0.5)',
        })
    });
    if (stroke) {
        rectangleStyle.setStroke(stroke);
    }
    rectangleFeature.setStyle(rectangleStyle);

    const layer = new VectorLayer({
        source: new VectorSource({
            features: [rectangleFeature],
        }),
    });

    mapObject.addLayer(layer);
    return layer;
}

export function drawCircle(mapObject: ol.Map, center: Coordinate, radius: number, color?: ColorLike | string) {
    let circle = new Circle(center, radius);
    const circleFeature = new ol.Feature(circle);

    const circleStyle = new Style({
        fill: new Fill({
            color: color ?? 'rgba(255, 0, 0, 0.4)'
        })
    })
    circleFeature.setStyle(circleStyle);
    const layer = new VectorLayer({
        source: new Vector({
            features: [circleFeature]
        })
    });
    mapObject.addLayer(layer);
}

export function drawMarker(mapObject: ol.Map, center: Coordinate) {
    const markerStyle = new Style({
        image: new Icon({
            src: marker,
            scale: 0.25,
            anchor: [0.5, 1]
        })
    });

    const markerFeature = new Feature({
        geometry: new Point(center)
    });
    markerFeature.setStyle(markerStyle);

    const markerLayer = new VectorLayer({
        source: new VectorSource({
            features: [markerFeature]
        })
    });
    mapObject.addLayer(markerLayer);
    return markerLayer;
}