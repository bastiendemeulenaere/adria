import {createContext, PropsWithChildren, useContext, useEffect, useMemo, useState} from "react";
import EventBus from "@vertx/eventbus-bridge-client.js";
import URI from "../../api";
import {v4 as uuidv4} from "uuid";

export const WebSocketContext = createContext<TWebSocketContext | null>(null);
const CHNL_TO_CLIENTS_BROADCAST = "events.to.users";
const CHNL_NOTIFY_TO_USER_JOIN = "events.to.user.join";
const CHNL_USER_TO_SERVER = "events.to.server";
export type TWebSocketContext = {
    eb: any;
    addBroadcastListener: (listener: Listener) => string;
    removeBroadcastListener: (listenerId: string) => void;
    addUnicastListener: (listener: Listener) => string;
    removeUnicastListener: (listenerId: string) => void;
    sendToServer: (message: any) => void
    cleanUp: () => void;
};
type Listener = (error: Error, message: any) => void;
type ListenerItem = {
    id: string;
    listener: Listener
};

export function WebSocketProvider({children}: Readonly<PropsWithChildren>) {
    const [eb, setEb] = useState<EventBus.EventBus>();
    const [broadcastListeners, setBroadcastListeners] = useState<ListenerItem[]>([]);
    const [unicastListeners, setUnicastListeners] = useState<ListenerItem[]>([]);
    const [adriaId, setAdriaId] = useState<string>("");
    let joinId = "";

    useEffect(() => {
        const id = localStorage.getItem("adriaId");
        const CHNL_NOTIFY_TO_USER = "events.to.user." + id;
        if (!id) {
            throw new Error("No adriaId initialized!");
        }
        setAdriaId(id);
        const eventBus = new EventBus(URI + "/events");
        eventBus.onopen = () => {
            eventBus.registerHandler(CHNL_TO_CLIENTS_BROADCAST, onBroadcast);
            eventBus.registerHandler(CHNL_NOTIFY_TO_USER, onUnicast);
            eventBus.registerHandler(CHNL_NOTIFY_TO_USER_JOIN, onConnect);

            //this function waits for the connection to open
            function waitForConnection() {
                setTimeout(() => {
                    if (!joinId) {
                        waitForConnection();
                    }
                    eventBus.send(CHNL_USER_TO_SERVER, ({type: "join", adriaId: id, joinId: joinId}));
                }, 100)
            }

            waitForConnection();
        };
        setEb(eventBus);

        return () => {
            eventBus.close();
        };
    }, [broadcastListeners]);

    useEffect(() => {
        if (!eb) return;
    }, [eb]);

    const onConnect = (_: Error, message: any) => {
        joinId = (message.body.joinId);
    }

    function sendToServer(message: any) {
        message.adriaId = adriaId;
        try {
            eb?.send(CHNL_USER_TO_SERVER, (message));
        } catch (e) {
            console.log(e)
        }
    }


    const onBroadcast = (error: Error, message: any) => {
        if (broadcastListeners.length < 1) {
            console.log("No broadcastListeners defined");
        } else {
            broadcastListeners.forEach((l) => {
                l.listener(error, message);
            });
        }
    };
    const onUnicast = (error: Error, message: any) => {
        if (unicastListeners.length < 1) {
            console.log("No unicastlisteners defined");
        } else {
            unicastListeners.forEach((l) => {
                l.listener(error, message);
            });
        }
    };

    const addBroadcastListener = (listener: Listener) => {
        const id = generateUniqueId();
        setBroadcastListeners((prevListeners) => [...prevListeners, {id, listener}]);
        return id;
    };

    const removeBroadcastListener = (listenerId: string) => {
        setBroadcastListeners((prevListeners) =>
            prevListeners.filter((l) => l.id !== listenerId)
        );
    };

    const addUnicastListener = (listener: Listener) => {
        const id = generateUniqueId();
        setUnicastListeners((prev) => [...prev, {id, listener}]);
        return id;
    };

    const removeUnicastListener = (listenerId: string) => {
        setUnicastListeners((prev) =>
            prev.filter((l) => l.id !== listenerId)
        );
    };
    const cleanUp = () => {
        setBroadcastListeners([]);
        setUnicastListeners([]);
        console.log("Cleaned up websocketcontext");
    }

    return (
        <WebSocketContext.Provider
            value={useMemo(() => ({
                eb,
                    addBroadcastListener: addBroadcastListener,
                    removeBroadcastListener: removeBroadcastListener,
                    addUnicastListener: addUnicastListener,
                    removeUnicastListener: removeUnicastListener,
                    sendToServer: sendToServer,
                    cleanUp: cleanUp
            }), [])}>
            {children}
        </WebSocketContext.Provider>
    );
}

export const useWebSocket = (): TWebSocketContext => {
    const context: TWebSocketContext | null = useContext(WebSocketContext);
    if (!context) {
        throw new Error("useWebSocket must be used within a WebSocketProvider");
    }
    return context;
};

function generateUniqueId() {
    return uuidv4();
}