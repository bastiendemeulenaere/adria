import {NavLink} from "react-router-dom";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Badge from '@mui/joy/Badge';
import noApi from "../../services/api-notifications.ts";
import {useEffect, useState} from "react";
import axios, {CancelTokenSource} from "axios";

export default function Navbar() {
    const [count, setCount] = useState<number>(0);

    useEffect(() => {
        const evReq: CancelTokenSource = axios.CancelToken.source();
        noApi.getNotificationCount(evReq.token)
            .then(r => setCount(r.count))
            .catch(() => {
            });

        return () => {
            evReq.cancel();
        }
    }, [count]);

    return <nav className={"navbar"}>
        <ul>
            <li>
                <NavLink to={"/app/home"} className={({isActive}) => isActive ? "active" : ""}>
                    Home
                </NavLink>
            </li>
            <li>
                <NavLink to={"/app/map"} className={({isActive}) => isActive ? "active" : ""}>
                    Map
                </NavLink>
            </li>
            <li>
                <NavLink to={"/app/events"} className={({isActive}) => isActive ? "active" : ""}>
                    Events
                </NavLink>
            </li>

            <li>
                <Badge badgeContent={count} variant="solid">
                    <NavLink to={"/app/notifications"} className={({isActive}) => isActive ? "active" : ""}>
                        Notifications
                    </NavLink>
                </Badge>
            </li>
            <li className={"navbar-end"}>
                <NavLink to={"/app/profile"} className={({isActive}) => isActive ? "active" : ""}>
                    <AccountCircleIcon></AccountCircleIcon>
                </NavLink>
            </li>
        </ul>
    </nav>;
}