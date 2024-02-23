import {CancelToken} from "axios";
import axiosInstance, {handleError} from "../helpers/axioshelper";

export default {
    getNotifications: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/notifications", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
    setToRead: (notificationId: number, cancelToken: CancelToken) => {
        return axiosInstance.put(`api/notifications/${notificationId}/read`, {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
    getNotificationCount: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/notifications/count", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
};
