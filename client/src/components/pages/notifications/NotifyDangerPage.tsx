import Button from '@mui/joy/Button';
import {TDangerousArea} from "../../common/world.tsx";
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router";
import {TEvent} from "../../common/events.tsx";
import "../../../assets/css/suggested-notification.scss"
import {EventLocationInfo} from "../../EventLocationInfo.tsx";

export type NotifyDangerPageProps = {
    event: TEvent,
    dangerousArea: TDangerousArea
}

export default function NotifyDangerPage() {
    const navigate = useNavigate()
    const {state} = useLocation();

    if (!state) {
        window.location.href = ("/")
    }


    const event: TEvent = state.event;
    const dangerousArea = state.dangerousArea;
    useEffect(() => {
        const $root = document.querySelector("#root");
        if (!$root) return;
        $root.classList.add("full-dvh");
        const vibratePattern = [250, 50, 200, 100, 250, 50, 200, 100, 250, 50, 200];

        //vibrate loop
        const vibrateInterval = setInterval(() => {
            navigator.vibrate(vibratePattern);
        }, vibratePattern.reduce((acc, val) => acc + val, 0) + 500); // calculate the total duration of the vibration pattern

        return () => {
            $root.classList.remove("full-dvh");
            clearInterval(vibrateInterval);
        }
    }, []);

    async function handleSubmit(e: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        e.preventDefault();
        navigate("/");
    }

    return (
        <div className="location-popup">
            <h2>Watch out! The weather suddenly changed! Try go get to a safe location fast!</h2>
            <EventLocationInfo event={event} location={null} dangerousArea={dangerousArea}/>
            <form className="form-group" onSubmit={e => e.preventDefault()}>
                <Button type="submit" onClick={(event) => handleSubmit(event)}>Okay!</Button>
            </form>
        </div>
    );
}
