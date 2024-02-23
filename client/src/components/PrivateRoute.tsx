import {ReactNode, useEffect, useState} from "react";
import {v4 as uuidv4} from "uuid";
export function PrivateRoute({ children } : {children: ReactNode}) {

    const [authenticated, setAuthenticated] = useState(false);
    //set the adriaId
    let adriaId = localStorage.getItem("adriaId");
    if (!adriaId){
        adriaId = uuidv4();
        localStorage.setItem("adriaId", adriaId);
        setAuthenticated(true);
    }
    useEffect(() => {
        if (localStorage.getItem("adriaId")){
            setAuthenticated(true);
        }
    }, [authenticated])

    return authenticated ? children : <p>Logging in...</p>;
}
