import './assets/css/app.css'
import {Navigate, Route, Routes} from "react-router-dom";
import AdriActive from "./AdriActive";
import {CssVarsProvider} from '@mui/joy/styles';
import NotFound from "./components/common/404";
import {PrivateRoute} from "./components/PrivateRoute";
import theme from "./assets/theme";
import {WebSocketProvider} from "./components/context/WebSocketContext";
import {Toaster} from 'react-hot-toast';
import NotifySuggestedLocationPage from "./components/pages/notifications/NotifySuggestedLocationPage.tsx";
import NotifyLocationPage from "./components/pages/notifications/NotifyLocationPage.tsx";
import NotifyCancelPage from "./components/pages/notifications/NotifyCancelPage.tsx";
import NotifyDangerPage from "./components/pages/notifications/NotifyDangerPage.tsx";

function checkNotificationPermission() {
    const permission = localStorage.getItem('notificationPermission');
    if (permission !== 'granted') {
        requestNotificationPermission();
    }
}

function requestNotificationPermission() {
    Notification.requestPermission().then(permission => {
        if (permission === 'granted') {
            localStorage.setItem('notificationPermission', 'granted');
        }
    }).catch(error => {
        console.error('Error in requesting notification permission:', error);
    });
}

function App() {
    checkNotificationPermission();
    return (
        <CssVarsProvider theme={theme}>
            <Routes>
                <Route path={"/"} element={<Navigate to={"/app"}/>}></Route>
                <Route path="/app/*" element={
                    <PrivateRoute>
                        <WebSocketProvider>
                            <AdriActive></AdriActive>
                        </WebSocketProvider>
                    </PrivateRoute>
                }/>
                <Route path="/notification/suggested" element={<NotifySuggestedLocationPage/>}/>
                <Route path="/notification/location" element={<NotifyLocationPage/>}/>
                <Route path="/notification/canceled" element={<NotifyCancelPage/>}/>
                <Route path="/notification/danger" element={<NotifyDangerPage/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Routes>
            <Toaster
                position="top-right"
                reverseOrder={false}
            />
        </CssVarsProvider>
    )
}

export default App
