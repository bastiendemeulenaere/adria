import {TUser} from "./common/user.tsx";
import Textarea from "@mui/joy/Textarea";
import toast from "react-hot-toast";
import apiUser from "../services/api-user.ts";
import axios, {CancelTokenSource} from "axios";
import {useState} from "react";

type TUserBlockProps = {
    readonly user: TUser;
    readonly isEditing: boolean;
    readonly setIsEditingFalse: () => void;
    readonly refreshUser: () => void;
}

export default function UserBlock(prop: TUserBlockProps) {
    const evReq: CancelTokenSource = axios.CancelToken.source();
    const [aboutMe, setAboutMe] = useState<string>(prop.user.aboutMe);
    return <div className="user">
        <h2>{prop.user.firstName} {prop.user.lastName}</h2>
        {prop.isEditing ? (
            <form className="form-group" onSubmit={handleSubmit}>
            <Textarea defaultValue={prop.user.aboutMe} onChange={(e) => setAboutMe(e.target.value)}></Textarea>
            <button type="submit" className={"buttons"}>Save</button>
            </form>
        ): <p className={"about-me"}>{prop.user.aboutMe}</p>
        }

    </div>;

    function handleEditAboutMe() {
        const editReq = toast.promise(editAboutMe(), {
            loading: "Changing about-me...",
            success: "Successfully changed about-me!",
            error: "Error changing about-me, try again later. If the issue persists contact support."
        }).catch(err => console.log(err));
        editReq.then((res) => {
            console.log(res);
        });
    }

    async function editAboutMe() {
        const aboutMeData = {
            "aboutMe": aboutMe
        }
        return await apiUser.editAboutMe(aboutMeData, evReq.token);
    }

    function handleSubmit(event: { preventDefault: () => void; }) {
        event.preventDefault();
        handleEditAboutMe();
        prop.setIsEditingFalse();
        prop.refreshUser();
    }
}