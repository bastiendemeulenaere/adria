import {CancelToken} from "axios";
import axiosInstance, {handleError} from "../helpers/axioshelper";

export default {
    getCategories: (cancelToken: CancelToken) => {
        return axiosInstance.get("api/category", {cancelToken: cancelToken}).then(res => {
            return res.data;
        }).catch(handleError);
    },
};
