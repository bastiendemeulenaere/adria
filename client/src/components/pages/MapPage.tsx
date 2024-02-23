import {useEffect, useRef} from "react";
import * as ol from "ol";
import {Vector} from "ol/source";
import {Polygon} from "ol/geom";
import "../../assets/css/map.css"
import {useWebSocket} from "../context/WebSocketContext.tsx";
import VectorLayer from "ol/layer/Vector";
import {Coordinate} from "ol/coordinate";
import {World} from "../common/world.tsx";
import {
    convertServerSectorsToClientSectors, createMapObject,
    getAdriaMiddle, getCoordConverter
} from "../../helpers/maphelpers/server-location-helper.ts";
import {drawDangerZones, drawRectangle, drawSectors, getAdriaSize} from "../../helpers/maphelpers/shape-drawer.ts";
import api from "../../services/api-world.ts";
import axios from "axios";

function MapPage() {
    const mapDiv = useRef(null);
    const ws = useWebSocket();
    // @ts-ignore
    const dangerZoneLayers: VectorLayer<Vector<ol.Feature<Polygon>>>[] = [];
    // @ts-ignore
    const sectorLayers: VectorLayer<Vector<ol.Feature<Polygon>>>[] = [];
    useEffect(() => {
        const center: Coordinate = getAdriaMiddle();
        const mapObject = createMapObject(center, 15);
        const adriaCancelSource = axios.CancelToken.source();
        if (mapDiv.current) {
            mapObject.setTarget(mapDiv.current);
        }
        const rectFeature = drawRectangle(mapObject, center, getAdriaSize(), getAdriaSize(), "transparent")
        const rectExtent = rectFeature.getGeometry()?.getExtent();
        const coordConverter = getCoordConverter(rectExtent ?? [0, 0, 0, 0])

        function updateSectors(sectors: World[]) {
            //remove existing layers
            dangerZoneLayers.forEach(dangerzone => {
                mapObject.removeLayer(dangerzone);
            })
            if (sectorLayers.length === 0) {
                drawSectors(mapObject, sectors).forEach(sector => {
                    sectorLayers.push(sector);
                })
            }
            //add the layers
            drawDangerZones(mapObject, sectors).forEach(zone => {
                dangerZoneLayers.push(zone);
            })
        }

        function fetchSectorsAndDraw() {

            api.getAdria(adriaCancelSource.token).then(data => {
                if (!data) return;
                const sectors = convertServerSectorsToClientSectors(data.sectors, coordConverter);
                updateSectors(sectors);
            });

        }

        function handleWebSocketMessage(error: Error, message: any) {
            if (error) {
                console.error(error);
                return;
            }
            let sectors: World[] = message.body.sectors;
            sectors = convertServerSectorsToClientSectors(sectors, coordConverter);
            updateSectors(sectors);

        }

        const messageHandlerId = ws.addBroadcastListener(handleWebSocketMessage)
        fetchSectorsAndDraw();

        return () => {
            mapObject.setTarget();
            ws.removeBroadcastListener(messageHandlerId);
            adriaCancelSource.cancel();
        };
    }, []);

    return (
        <div ref={mapDiv} id="map" className={"ol-map"}/>
    );

}

export default MapPage;


