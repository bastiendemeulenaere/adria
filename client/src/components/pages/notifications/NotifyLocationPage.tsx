import Button from '@mui/joy/Button';
import {TCoordinate} from "../../common/world.tsx";
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router";
import {TEvent} from "../../common/events.tsx";
import "../../../assets/css/suggested-notification.scss"
import {EventLocationInfo} from "../../EventLocationInfo.tsx";
import toast from "react-hot-toast";


export default function NotifyLocationPage() {
    const navigate = useNavigate()
    const {state} = useLocation()

    if (!state) {
        window.location.href = ("/")
    }
    const location: TCoordinate = state.location;
    const event: TEvent = state.event;
    navigator.vibrate([200]);

    useEffect(() => {
        const $root = document.querySelector("#root");
        if (!$root) return;
        $root.classList.add("full-dvh");
        return () => {
            $root.classList.remove("full-dvh");
        }
    }, []);

    async function handleSubmit(e: React.MouseEvent<HTMLAnchorElement, MouseEvent>, accepted: boolean) {
        e.preventDefault();
        if (!accepted) {
            toast("We notified the event creator you aren't going to make it!")
        }
        navigate("/");
    }

    return (
        <div className="location-popup">
            <h2>A location has been chosen for your event coming up soon!</h2>
            <EventLocationInfo event={event} location={location} dangerousArea={null}/>
            <form className="form-group" onSubmit={e => e.preventDefault()}>
                <Button type="submit" onClick={(event) => handleSubmit(event, true)}>Okay!</Button>
                <Button type="submit" color={"danger"} onClick={(event) => handleSubmit(event, false)}>I'm not going to
                    make it!</Button>
            </form>
        </div>
    );
}
