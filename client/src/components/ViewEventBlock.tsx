import {useEffect, useRef, useState} from "react";
import {TEvent} from "./common/events";
import PersonIcon from "@mui/icons-material/Person";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import getSectorName from "../helpers/sectorhelper.ts";
import {AccessTimeFilled} from "@mui/icons-material";
import {DATE_OPTIONS, TIME_OPTIONS} from "../helpers/datehelper.ts";
import {Coordinate} from "ol/coordinate";
import {
    convertServerSectorToClientSector,
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
import {TCoordinate, World} from "./common/world.tsx";
import worldApi from "../services/api-world.ts"
import {Point, Polygon} from "ol/geom";
import {Feature} from "ol";
import VectorSource from "ol/source/Vector";
import VectorLayer from "ol/layer/Vector";
import {Stroke} from "ol/style";

type TViewEventBlockProps = {
    readonly event: TEvent;
};

export default function EventBlock(prop: TViewEventBlockProps) {
    const [imgSrc, setImgSrc] = useState<string | undefined>(undefined);
    const [sector, setSector] = useState<World>();
    useEffect(() => {
        worldApi.getSector(prop.event.sector.id).then(res => {
            setSector(res);
        })
    }, []);

    useEffect(() => {
        import(`../assets/img/${prop.event.category.categoryId}.png`)
            .then((module) => {
                setImgSrc(module.default);
            })
            .catch((error) => {
                console.error('Error loading image:', error);
            });
    }, [prop.event.category.categoryId]);

    return (<div className={"event-details-container"}>
            <div>
                <div className="imageContainer">
                    <img src={imgSrc} alt={`Category: ${prop.event.name}`} className="imgViewEvent"/>
                </div>
                <div className="event-details" id={prop.event.id.toString()}>
                    <h2>{prop.event.name}</h2>
                    <ul>
                        <li><PersonIcon/>{prop.event.organiser.firstName} {prop.event.organiser.lastName}</li>
                        <li><LocationOnIcon/>{getSectorName(prop.event.sector.id)}</li>
                        <li>
                            <AccessTimeFilled/>{new Date(prop.event.startDateTime).toLocaleDateString(undefined, DATE_OPTIONS)}{' | '}
                            {new Date(prop.event.startDateTime).toLocaleTimeString(undefined, TIME_OPTIONS)}
                        </li>
                        <li><CheckCircleIcon/>{getPeopleLeft()} spots left</li>
                        <li>{prop.event.description}</li>
                    </ul>
                </div>
            </div>

            <MarkerMap
                markerPoint={prop.event.location}
                sector={sector}></MarkerMap>
        </div>
    );

    function getPeopleLeft() {
        return prop.event.amountOfPeople - prop.event.attendees;
    }
}

type MarkerMapProps = {
    readonly markerPoint: TCoordinate | undefined | null,
    readonly sector: World | undefined | null
}

function MarkerMap({markerPoint, sector}: MarkerMapProps) {
    const mapDiv = useRef(null);
    useEffect(() => {
        const center: Coordinate = getAdriaMiddle();
        const mapObject = createMapObject(center, 16);
        if (mapDiv.current) {
            mapObject.setTarget(mapDiv.current);
        }
        const rectFeature = drawRectangle(mapObject, center, getAdriaSize(), getAdriaSize(), "transparent")
        const rectExtent = rectFeature.getGeometry()?.getExtent();
        const coordConverter = getCoordConverter(rectExtent ?? [0, 0, 0, 0])

        let marker: VectorLayer<VectorSource<Feature<Point>>> | null;
        if (markerPoint) {
            const markerLocation = coordConverter(markerPoint.x, markerPoint.y);
            mapObject.getView().setCenter(markerLocation);
            marker = drawMarker(mapObject, markerLocation);
        }
        let sectorLayer: VectorLayer<VectorSource<Feature<Polygon>>>;

        if (sector && !markerPoint) {
            const clientSector = convertServerSectorToClientSector(sector, coordConverter);
            const start = [clientSector.coordinateRange.start.x, clientSector.coordinateRange.start.y] as Coordinate;
            const end = [clientSector.coordinateRange.end.x, clientSector.coordinateRange.end.y] as Coordinate;
            sectorLayer = drawRectangleWithStartAndEndPoint(mapObject, start, end, "transparent", new Stroke({
                color: "rgba(100,0,0, 0.5)",
                width: 2
            }));
            const middleOfSector = getCoordinateRangeMiddle(clientSector.coordinateRange)
            const middle = [middleOfSector.x, middleOfSector.y] as Coordinate
            mapObject.getView().setCenter(middle)
        }

        return () => {
            mapObject.setTarget();
            if (marker) {
                mapObject.removeLayer(marker);
            }
            if (sectorLayer) {
                mapObject.removeLayer(sectorLayer);
            }
        }
    }, [markerPoint, sector]);
    return <div ref={mapDiv} className={"ol-map"}/>
}