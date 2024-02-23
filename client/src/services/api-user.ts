import {CancelToken} from "axios";
import axiosInstance, {handleError} from "../helpers/axioshelper.ts";

export default {
    getUser: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/user", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
    getUserInterests: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/user/interests", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
    updateUserInterests: (cancelToken: CancelToken) => {
        return axiosInstance.post("api/user", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
    editAboutMe: (aboutMe: any, cancelToken: CancelToken) => {
        return axiosInstance.put("api/user/about-me", aboutMe, {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },

};
