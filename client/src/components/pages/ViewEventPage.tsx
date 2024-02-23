import {useEffect, useState} from "react";
import {useParams, useNavigate, useSearchParams} from "react-router-dom";
import axios from "axios";
import evApi from "../../services/api-events";
import ViewEventBlock from "../ViewEventBlock";
import ArrowBackIosIcon from "@mui/icons-material/ArrowBackIos";
import "../../assets/css/view-event.scss";
import toast from "react-hot-toast";
import {log} from "ol/console";
import {TEvent} from "../common/events.tsx";

export default function ViewEventPage() {
    const [event, setEvent] = useState<TEvent | null>(null);
    const {id: eventId} = useParams(); // Ensure that the parameter name matches your route
    const navigate = useNavigate();
    const [isJoined, setIsJoined] = useState(false);
    const [searchParams] = useSearchParams();
    const joinReq = axios.CancelToken.source();
    const [isCreator, setIsCreator] = useState(false);
    if (!eventId || Number.isNaN(parseInt(eventId))) {
        navigate("/");
    }

    useEffect(() => {
        const evReq = axios.CancelToken.source();
        if (eventId) {
            evApi.getEventFromId(parseInt(eventId), evReq.token)
                .then(data => {
                    setEvent(data);
                });
            evApi.hasUserJoined(parseInt(eventId), evReq.token)
                .then(data => {
                    if (data) {
                        setIsJoined(data.attending);
                    }
                })
        }


        return () => {
            evReq.cancel();
            joinReq.cancel();
        };
    }, [eventId]);

    useEffect(() => {
        if (event) {
            const adriaId = localStorage.getItem("adriaId") ?? "";
            setIsCreator(event.organiser.id === adriaId);
        }
    }, [event]);
    useEffect(() => {
        const isJoined = searchParams.get('joined');
        setIsJoined(isJoined === "true");
    }, [searchParams]);


    async function joinEvent() {
        if (!eventId) return;
        return await evApi.joinEvent(parseInt(eventId), joinReq.token);
    }

    async function leaveEvent() {
        if (!eventId) return;
        return await evApi.leaveEvent(parseInt(eventId), joinReq.token);
    }


    function handleJoin() {
        const joinReq = toast.promise(joinEvent(), {
            loading: "Joining event...",
            success: "Successfully joined event!",
            error: "Error joining event, try again later. If the issue persists contact support."
        }).catch(err => log(err));
        joinReq.then((res) => {
            if (res) {
                setEvent(res);
            }
            setIsJoined(true);
        });
    }

    function handleLeave() {
        const leaveReq = toast.promise(leaveEvent(), {
            loading: "Leaving event...",
            success: "Successfully left event!",
            error: "Error leaving event, try again later. If the issue persists contact support."
        });
        leaveReq.then((event) => {
            setEvent(event);
            setIsJoined(false);
        });
    }

    return (
        <div className={"event-view-container"}>
            <ArrowBackIosIcon onClick={() => navigate(-1)}/>
            <h1 id="joinOrLeave">{isJoined ? 'Leave event' : 'Join event'}</h1>
            {event && <ViewEventBlock event={event}/>}
            <div className="joinButtonContainer">
                <button type="submit" onClick={() => isJoined ? handleLeave() : handleJoin()}
                        className="buttons joinButton">
                    {isJoined ? 'Leave' : 'Join'}
                </button>
                {isCreator && (
                    <button className={"buttons joinButton"} onClick={() => navigate(`/app/event/create?id=${eventId}`)}>
                        Edit
                    </button>
                )}
            </div>
        </div>
    );
}