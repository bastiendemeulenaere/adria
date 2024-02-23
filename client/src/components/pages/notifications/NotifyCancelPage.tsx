import Button from '@mui/joy/Button';
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router";
import {TEvent} from "../../common/events.tsx";
import "../../../assets/css/suggested-notification.scss"
import {EventLocationInfo} from "../../EventLocationInfo.tsx";


export type NotifyCancelPageProps = {
    event: TEvent;
}

export default function NotifyCancelPage() {
    const navigate = useNavigate()
    const {state} = useLocation()

    if (!state) {
        window.location.href = ("/")
    }
    navigator.vibrate([200]);

    const event: TEvent = state.event;

    useEffect(() => {
        const $root = document.querySelector("#root");
        if (!$root) return;
        $root.classList.add("full-dvh");
        return () => {
            $root.classList.remove("full-dvh");
        }
    }, []);

    async function handleSubmit(e: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        e.preventDefault();
        navigate("/");
    }

    return (
        <div className="location-popup">
            <h2>An event you were attending has just been cancelled!</h2>
            <EventLocationInfo event={event} location={null} dangerousArea={null}/>
            <form className="form-group" onSubmit={e => e.preventDefault()}>
                <Button type="submit" onClick={(event) => handleSubmit(event)}>Okay!</Button>
            </form>
        </div>
    );
}
