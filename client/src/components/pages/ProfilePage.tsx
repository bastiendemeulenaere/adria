import "../../assets/css/app.scss"
import "../../assets/css/profilepage.scss"
import {useEffect, useState} from "react";
import {TInterest} from "../common/interest.tsx";
import axios, {CancelTokenSource} from "axios";
import api from "../../services/api-user.ts";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import {TUser} from "../common/user.tsx";
import {useNavigate} from "react-router";
import UserBlock from "../UserBlock.tsx";

function ProfilePage() {
    const [interests, setInterests] = useState<TInterest[]>([]);
    const [user, setUser] = useState<TUser>();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const navigate = useNavigate();

    useEffect(() => {
        const evReq: CancelTokenSource = axios.CancelToken.source();
        api.getUserInterests(evReq.token).then(data => {
            setInterests(data);
            setIsLoading(false);
        }).catch(() => {
            setIsLoading(false);
        });

        api.getUser(evReq.token).then(data => {
            setUser(data);
            setIsLoading(false);
        }).catch(() => {
            setIsLoading(false);
        });

        return () => {
            evReq.cancel();
        }
    }, [isLoading])

    function renderUserInterests(interests: TInterest[]) {
        return interests && interests.length > 0 ? (
            interests.map((e) => (
                <h3 key={e.name}>{e.name}</h3>
            ))
        ) : (
            <div>
                <p className={"error"}>No interests found!</p>
            </div>
        )
    }

    function renderUser(user: TUser | undefined) {
        return user ? (
                <UserBlock user={user} isEditing={isEditing} setIsEditingFalse={() => setIsEditing(false)}
                           refreshUser={() => setIsLoading(true)}></UserBlock>
            )
            : (
                <div>
                    <p className={"error"}>No User found!</p>
                </div>
            )

    }

    const handleEditClick = () => {
        setIsEditing(prevIsEditing => !prevIsEditing);
    };

    return <div className={"loading"}>
        {isLoading ? (
            <p>Loading...</p>
        ) : <>
            <button className="buttons edit-button" onClick={handleEditClick}>{isEditing ? 'Cancel' : 'Edit'}</button>
            <div className="profile-page">
                <div className="profile-info">
                    <div className="profile-picture">
                        <AccountCircleIcon></AccountCircleIcon>
                    </div>
                    <div className="name">
                        {renderUser(user)}
                    </div>
                </div>
                <div className="interests-list">
                    <h2>Interest List</h2>
                    <form className="list-container">
                        {renderUserInterests(interests)}
                    </form>
                    <button className={"buttons"} onClick={() => navigate('/app/interests?editing=true')}>Edit interests</button>
                </div>
            </div>
        </>
        }
    </div>

}

export default ProfilePage;