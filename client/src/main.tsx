import React from 'react'
import ReactDOM from 'react-dom/client'
import './assets/css/index.css'
import './assets/css/reset.css'
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import {getBaseName} from "./api";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <BrowserRouter basename={getBaseName()}>
            <App />
        </BrowserRouter>
    </React.StrictMode>,
)
