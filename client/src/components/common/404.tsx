
import "../../assets/css/404.css"
import {useNavigate} from "react-router-dom";

export default function NotFound() {
    const nav = useNavigate();
    return <div className="notfound">
        <h1>Page not found!</h1>
        <p>(╯°□°）╯︵ ┻━┻</p>
        <button onClick={() => nav("/")}>Go to home</button>
    </div>
}