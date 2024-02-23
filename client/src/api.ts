import config from "./config.json";

const getApi = () => {
    if (process.env.NODE_ENV === 'development') {
        return `${config.devHost}`;
    } else {
        return `${config.host}`;
    }
}

export const getBaseName = () => {
    // Check if the app is running locally or on the server
        return '/';
};


export default getApi();