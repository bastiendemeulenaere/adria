import {useEffect, useRef, useState} from "react";
import {Coordinate} from "ol/coordinate";
import {
    convertCoordinateRange,
    createMapObject,
    getAdriaMiddle,
    getCoordConverter, getCoordinateRangeMiddle,
} from "../helpers/maphelpers/server-location-helper.ts";
import {
    drawMarker,
    drawRectangle,
    drawRectangleWithStartAndEndPoint,
    getAdriaSize
} from "../helpers/maphelpers/shape-drawer.ts";
import {TEvent} from "./common/events.tsx";
import {TCoordinate, TDangerousArea} from "./common/world.tsx";
import PersonIcon from "@mui/icons-material/Person";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import getSectorName from "../helpers/sectorhelper.ts";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {Feature} from "ol";
import {Point, Polygon} from "ol/geom";

interface EventLocationInfoProps {
    readonly event: TEvent;
    readonly location: TCoordinate | null | undefined;
    readonly dangerousArea: TDangerousArea | null | undefined;
}

export function EventLocationInfo({event, location, dangerousArea}: EventLocationInfoProps) {
    const mapDiv = useRef(null);
    const [imgSrc, setImgSrc] = useState<string | undefined>(undefined);

    function getImage() {
        import(`../assets/img/${event.category.categoryId}.png`)
            .then((module) => {
                setImgSrc(module.default);
            })
            .catch((error) => {
                console.error('Error loading image:', error);
            });
    }

    useEffect(() => {
        const center: Coordinate = getAdriaMiddle();
        const mapObject = createMapObject(center, 16);
        if (mapDiv.current) {
            mapObject.setTarget(mapDiv.current);
        }
        const rectFeature = drawRectangle(mapObject, center, getAdriaSize(), getAdriaSize(), "transparent")
        const rectExtent = rectFeature.getGeometry()?.getExtent();
        const coordConverter = getCoordConverter(rectExtent ?? [0, 0, 0, 0])
        let marker: VectorLayer<VectorSource<Feature<Point>>>;
        if (location) {
            const markerLocation = coordConverter(location.x, location.y);
            mapObject.getView().setCenter(markerLocation);
            marker = drawMarker(mapObject, markerLocation);
        }

        let dangerousAreaLayer: VectorLayer<VectorSource<Feature<Polygon>>>;
        if (dangerousArea) {
            const clientBounds = convertCoordinateRange(dangerousArea.coordinateRange, coordConverter);
            const start = [clientBounds.start.x, clientBounds.start.y] as Coordinate;
            const end = [clientBounds.end.x, clientBounds.end.y] as Coordinate;
            dangerousAreaLayer = drawRectangleWithStartAndEndPoint(mapObject, start, end, "rgba(100,0,0, 0.5)");
            const middleOfSector = getCoordinateRangeMiddle(clientBounds)
            const middle = [middleOfSector.x, middleOfSector.y] as Coordinate
            mapObject.getView().setCenter(middle)
        }

        getImage();

        return () => {
            mapObject.setTarget();
            if (marker) {
                mapObject.removeLayer(marker);
            }
            if (dangerousAreaLayer) {
                mapObject.removeLayer(dangerousAreaLayer);
            }
        }
    }, [location]);

    return <div className={"info"}>
        <div className={"map"}>
            <div ref={mapDiv} className={"ol-map"}/>
        </div>
        <div className={"bottom-container"}>
            {renderEvent(event)}
            <div className={"image-container"}>
                <img src={imgSrc} alt={"Image for: " + event.category.name}/>
            </div>
        </div>
    </div>;
}

function renderEvent(event: TEvent) {
    return <div className={"suggested-event-details"}>
        <h3>{event.name}</h3>
        <ul>
            <li><PersonIcon/>{event.organiser.firstName} {event.organiser.lastName}</li>
            <li><LocationOnIcon/>{getSectorName(event.sector.id)}</li>
            <li><CheckCircleIcon/>{event.amountOfPeople}</li>
            <li>{event.description}</li>
        </ul>
    </div>
}