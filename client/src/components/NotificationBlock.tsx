import {TNotification} from "./common/notification.tsx";
import NotificationsActiveIcon from "@mui/icons-material/NotificationsActive";
import CardContent from "@mui/joy/CardContent";
import Typography from "@mui/joy/Typography";
import Chip from "@mui/joy/Chip";
import Card from "@mui/joy/Card";
import {useNavigate} from "react-router-dom";
import "../assets/css/notifications.scss";


type TNotificationBlockProps = {
    readonly notification: TNotification;
    readonly onClick: () => void;
    readonly onMouseEnter: () => void;
};
export default function NotificationBlock(prop: TNotificationBlockProps) {
    const navigate = useNavigate();
    return (
        <Card
            onClick={() => navigate(`/app/events/view/${prop.notification.eventId}`)}
            onMouseEnter={prop.onMouseEnter}
            variant="outlined"
            orientation="horizontal"
            className={"card"}
        >
            <NotificationsActiveIcon className={"bell-icon"}/>
            <CardContent>
                <Typography level="title-lg" id="notification-description">
                    {prop.notification.title}
                </Typography>
                <Typography level="body-sm">{dateToTimeAgo(prop.notification.startTime)}</Typography>
                <Chip>
                    {prop.notification.description}
                </Chip>
            </CardContent>
        </Card>
    )

    function dateToTimeAgo(startTime: number): string {
        const currentDate = new Date();
        const notificationDate = new Date(startTime);
        const timeDifference = currentDate.getTime() - notificationDate.getTime();
        const seconds = Math.floor(timeDifference / 1000);
        const minutes = Math.floor(seconds / 60);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);

        if (days > 0) {
            return `${days}d ago`;
        } else if (hours > 0) {
            return `${hours}h ago`;
        } else if (minutes > 0) {
            return `${minutes}m ago`;
        } else {
            return `${seconds}s ago`;
        }
    }


}