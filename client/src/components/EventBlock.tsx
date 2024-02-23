import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {TEvent} from "./common/events";
import PersonIcon from '@mui/icons-material/Person';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import {AccessTimeFilled} from "@mui/icons-material";
import {DATE_OPTIONS, TIME_OPTIONS} from "../helpers/datehelper.ts";


type TEventBlockProps = {
    readonly simple?: boolean
    readonly event: TEvent;
    readonly onClick: () => void;
};

export default function EventBlock(prop: TEventBlockProps) {
    const [imgSrc, setImgSrc] = useState<string | undefined>(undefined);
    const navigate = useNavigate();
    useEffect(() => {
        import(`../assets/img/${prop.event.category.categoryId}.png`)
            .then((module) => {
                setImgSrc(module.default);
            })
            .catch((error) => {
                console.error('Error loading image:', error);
            });
    }, [prop.event.category.categoryId]);

    return (
        <div onClick={() => navigate(`/app/events/view/${prop.event.id}`)} className="event"
             key={prop.event.id.toString()}>
            <img src={imgSrc} alt={`Category: ${prop.event.name}`}/>
            <p>{prop.event.name}</p>
            <ul>
                {!prop.simple &&
                  <li><PersonIcon/>{prop.event.organiser.firstName} {prop.event.organiser.lastName}</li>
                }
                <li><LocationOnIcon/>{prop.event.sector.name}</li>
                <li>
                    <AccessTimeFilled/>{new Date(prop.event.startDateTime).toLocaleDateString(undefined, DATE_OPTIONS)}{' | '}
                    {new Date(prop.event.startDateTime).toLocaleTimeString(undefined, TIME_OPTIONS)}</li>
                {!prop.simple &&
                  <li><CheckCircleIcon/>{getPeopleLeft()} spots of {prop.event.amountOfPeople} left</li>
                }
            </ul>
        </div>
    );

    function getPeopleLeft() {
        return prop.event.amountOfPeople - prop.event.attendees;
    }

}