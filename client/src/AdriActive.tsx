import {Navigate, Route, Routes, useNavigate} from "react-router-dom";
import Navbar from "./components/common/navbar";
import "./assets/css/homepage.css";
import "./assets/css/my-events-page.css"
import MapPage from "./components/pages/MapPage";
import MyEventsPage from "./components/pages/MyEventsPage.tsx";
import NotFound from "./components/common/404.tsx";
import "./assets/css/events.css";
import InterestPage from "./components/pages/InterestPage.tsx";
import ProfilePage from "./components/pages/ProfilePage.tsx";
import CreateEventPage from "./components/pages/CreateEventPage.tsx";
import {useWebSocket} from "./components/context/WebSocketContext.tsx";
import {useEffect, useState} from "react";
import ViewEventPage from "./components/pages/ViewEventPage.tsx";
import {LocationInfoProps} from "./components/pages/notifications/NotifySuggestedLocationPage.tsx";
import {WebsocketEvent} from "./components/common/websocketevent.ts";
import {TDangerousArea, TSectorLocation} from "./components/common/world.tsx";
import {TEvent} from "./components/common/events.tsx";
import SearchPage from "./components/pages/SearchPage.tsx";
import "./assets/css/primereact/themes/lara/lara-light/teal/theme.scss"
import NotificationsPage from "./components/pages/NotificationsPage.tsx";
import {NotifyCancelPageProps} from "./components/pages/notifications/NotifyCancelPage.tsx";
import {NotifyDangerPageProps} from "./components/pages/notifications/NotifyDangerPage.tsx";

function AdriActive() {
    const ws = useWebSocket();
    const navigate = useNavigate();
    const [hasSelectedInterests, setHasSelectedInterests]
        = useState(localStorage.getItem("selectedInterests") === "true" || false);
    useEffect(() => {
        const listenerId = ws.addUnicastListener(handleMessage);
        return () => {
            ws.removeUnicastListener(listenerId);
            ws.cleanUp();
        };
    }, []);

    useEffect(() => {
        if (hasSelectedInterests) {
            localStorage.setItem("selectedInterests", "true");
        }
    }, [hasSelectedInterests]);


    const handleMessage = (error: Error, message: any) => {
        if (error) {
            console.error(error);
        }

        switch (message.body.eventType) {
            case WebsocketEvent.SUGGESTLOCATION:
                handleSuggestLocation(message.body);
                break;
            case WebsocketEvent.NOTIFYLOCATION:
                handleNotifyLocation(message.body)
                break;
            case WebsocketEvent.CANCELEDEVENT:
                handleCancelEvent(message.body)
                break;
            case WebsocketEvent.DANGEREVENT:
                handleDangerEvent(message.body);
                break;
        }

    }

    function handleCancelEvent(data: any) {
        const event: TEvent = data.event;
        const stateData: NotifyCancelPageProps = {
            event: event
        }
        navigate("/notification/canceled", {state: stateData})

    }

    function handleDangerEvent(data: any) {
        const event: TEvent = data.event;
        const dangerousArea: TDangerousArea = data.dangerousArea;
        const stateData: NotifyDangerPageProps = {
            event: event,
            dangerousArea: dangerousArea
        }
        navigate("/notification/danger", {state: stateData})

    }

    const handleSuggestLocation = (data: any) => {
        const suggestedLocation: TSectorLocation = data.suggestedLocation;
        const event: TEvent = data.event;
        const stateData: LocationInfoProps = {
            location: suggestedLocation,
            event: event
        }
        navigate("/notification/suggested", {state: stateData})
    }
    const handleNotifyLocation = (data: any) => {
        const location: TSectorLocation = data.location;
        const event: TEvent = data.event;
        const stateData: LocationInfoProps = {
            location: location,
            event: event
        }
        navigate("/notification/location", {state: stateData})
    }

    return <>
        <Routes>
            {
                hasSelectedInterests ?
                    <>
                        <Route path={"/profile"} element={<ProfilePage/>}></Route>
                        <Route path={"/"} element={<Navigate to={"/app/home"}/>}></Route>
                        <Route path={"/home"} element={<SearchPage/>}></Route>
                        <Route path={"/map"} element={<MapPage/>}></Route>
                        <Route path={"/events"} element={<MyEventsPage/>}></Route>
                        <Route path={"/notifications"} element={<NotificationsPage/>}></Route>
                        <Route path={"/interests"} element={<InterestPage setHasSelectedInterests={null}/>}></Route>
                        <Route path={"/event/create"} element={<CreateEventPage/>}></Route>
                        <Route path={"/events/view/:id"} element={<ViewEventPage/>}></Route>
                        <Route path={"*"} element={<NotFound/>}></Route>
                    </>
                    : <Route path={"*"} element={<InterestPage
                        setHasSelectedInterests={() => setHasSelectedInterests(true)}/>}></Route>
            }

        </Routes>
        <Navbar></Navbar>
    </>
}

export default AdriActive;