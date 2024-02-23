import axios from "axios";
import URI from "../api"
import dayjs from "dayjs";
import {jwtDecode} from "jwt-decode";
export const handleError = (error: any) => {
    if (axios.isCancel(error)) {
        return null;
    }
    console.error(error);
    throw error;
};


const axiosInstance = axios.create({
    baseURL: URI
})


axiosInstance.interceptors.request.use(async (config) => {
    const jwt = localStorage.getItem('jwt');

    if (jwt) {
        config.headers.Authorization = `Bearer ${jwt}`;

        // Decode the JWT to check its expiration
        const user = jwtDecode(jwt);
        const isExpired = dayjs.unix(user.exp ?? 0).diff(dayjs()) < 1;

        if (!isExpired) {
            return config;
        }
    }
    // if the jwt is missing or expired, get a new token
    const adriaId = localStorage.getItem('adriaId');
    const response = await axios.post(`${URI}api/auth/login`, { adriaId: adriaId });
    localStorage.setItem("jwt", response.data.jwt);
    config.headers.Authorization = `Bearer ${response.data.jwt}`;

    return config;
});
export default axiosInstance;