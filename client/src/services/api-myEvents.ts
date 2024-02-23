import {CancelToken} from "axios";
import axiosInstance, {handleError} from "../helpers/axioshelper";

export default {
    getOngoingEvents: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/event/ongoing", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },

    getUpcomingEvents: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/event/upcoming", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },

    getCreatedEvents: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/event/created", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    }
};
