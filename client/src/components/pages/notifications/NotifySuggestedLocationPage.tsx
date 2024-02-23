import Button from '@mui/joy/Button';
import {TCoordinate, TSectorLocation} from "../../common/world.tsx";
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router";
import {TEvent} from "../../common/events.tsx";
import "../../../assets/css/suggested-notification.scss"
import evApi from "../../../services/api-events.ts";
import {EventLocationInfo} from "../../EventLocationInfo.tsx";

export type LocationInfoProps = {
    location: TSectorLocation,
    event: TEvent
}

async function setLocation(eventId: number, coord: TCoordinate) {
    return await evApi.setEventLocation(eventId, coord);
}

async function cancelEvent(eventId: number) {
    return await evApi.cancelEvent(eventId);
}


export default function NotifySuggestedLocationPage() {
    const navigate = useNavigate();
    const {state} = useLocation()

    if (!state) {
        window.location.href = ("/")
    }

    const suggestedLocation: TSectorLocation = state.location;
    const event: TEvent = state.event;


    useEffect(() => {
        const $root = document.querySelector("#root");
        if (!$root) return;
        $root.classList.add("full-dvh");
        navigator.vibrate([200]);
        return () => {
            $root.classList.remove("full-dvh");
        }
    }, []);

    function redirectToHome() {
        navigate("/");
    }

    async function handleSubmit(e: React.MouseEvent<HTMLAnchorElement, MouseEvent>, accepted: boolean) {
        e.preventDefault();
        if (accepted) {
            setLocation(event.id, suggestedLocation.coordinate).then(() => redirectToHome()).catch(err => console.log(err));
        } else {
            cancelEvent(event.id).then(() => redirectToHome());
        }
    }

    return (
        <div className="location-popup">
            <h2>A location has been suggested for your event coming up soon!</h2>
            <EventLocationInfo event={event} location={suggestedLocation.coordinate} dangerousArea={null}/>
            <form className="form-group" onSubmit={e => e.preventDefault()}>
                <Button type="submit" onClick={(event) => handleSubmit(event, true)}>Accept Location</Button>
                <Button type="submit" color={"danger"} onClick={(event) => handleSubmit(event, false)}>Cancel
                    Event</Button>
            </form>
        </div>
    );
}
