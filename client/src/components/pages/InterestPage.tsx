import {useEffect, useState} from "react";
import axios, {CancelTokenSource} from "axios";
import apiInt from "../../services/api-interests.ts";
import InterestBlock from "../InterestBlock.tsx";
import {useNavigate} from "react-router";
import {useSearchParams} from "react-router-dom";
import apiUser from "../../services/api-user.ts";
import {TCategory} from "../common/category.tsx";
import toast from "react-hot-toast";
import "../../assets/css/interestspage.scss";

interface InterestPageProps {
    readonly setHasSelectedInterests: (() => void) | null
}

export default function InterestPage({setHasSelectedInterests}: InterestPageProps) {
    const [interests, setInterests] = useState<TCategory[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [selectedInterests, setSelectedInterests] = useState<TCategory[]>([]);
    const navigate = useNavigate();
    const evReq: CancelTokenSource = axios.CancelToken.source();
    const [searchParams] = useSearchParams();
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const minInterestsSelected = 3;

    useEffect(() => {

        apiInt.getInterests(evReq.token).then(data => {
            setInterests(data);
            setIsLoading(false);
        }).catch(() => {
            setIsLoading(false);
        });
        apiUser.getUserInterests(evReq.token)
            .then(interests => {
                if (!interests) interests = [];
                setSelectedInterests(interests);
            })
            .catch(() => {
            });
        return () => {
        }
    }, [isEditing])

    useEffect(() => {
        const editing = searchParams.get('editing');
        setIsEditing(editing === "true");
    }, [searchParams, isEditing]);


    return <div className={"loading"}>
        {isLoading ? (
            <p>Loading...</p>
        ) : <form className="form-group" onSubmit={handleSubmit}>
            <div className="interests-page">
                <h2>{isEditing ? 'Change Interests' : 'Select Interests'}</h2>
                {isEditing ? (
                    <h3>Select at least 3 categories</h3>
                ) : <h3>Select at least 3 categories or skip and finish later</h3>}
                <div className="interests-grid">
                    {renderInterests(interests)}
                </div>

                {!isEditing && (
                    <button className="buttons skip-button"
                            onClick={() => {
                                toast.success('Skipped adding interest, add these in the future!');
                                if (setHasSelectedInterests) {
                                    setHasSelectedInterests();
                                }
                                navigate('/app/home')
                            }}>Skip</button>
                )
                }

                {selectedInterests.length >= minInterestsSelected && (
                    <button type="submit" className={"buttons"}
                    >Save</button>
                )
                }
            </div>
        </form>
        }
    </div>


    function handleSubmit(event: { preventDefault: () => void; }) {
        event.preventDefault();
        handleAddInterest();
        if (setHasSelectedInterests) {
            setHasSelectedInterests();
        }
        if (isEditing) {
            navigate('/app/profile');
        }
    }

    function handleAddInterest() {
        const editReq = toast.promise(addInterest(), {
            loading: "Adding interest...",
            success: "Successfully added interests!",
            error: "Error adding interest, try again later. If the issue persists contact support."
        }).catch(err => console.log(err));
        editReq.then((res) => {
            console.log(res);
        });
    }

    async function addInterest() {
        return await apiInt.addInterests(selectedInterests.map(e => e.categoryId), evReq.token);
    }

    function handleInterestClick(interest: TCategory) {
        const isAlreadySelected = selectedInterests.some((selectedInterest) => selectedInterest.categoryId === interest.categoryId);

        const updatedInterests = isAlreadySelected
            ? selectedInterests.filter((selectedInterest) => selectedInterest.categoryId !== interest.categoryId)
            : [...selectedInterests, interest];

        setSelectedInterests(updatedInterests);
    }

    function renderInterests(interests: TCategory[]) {

        return interests && interests.length > 0 ? (
            interests.map((e) => {
                return (
                    <InterestBlock
                        key={e.categoryId} interest={e}
                        onChange={() => handleInterestClick(e)}
                        isSelected={selectedInterests.some(c => e.categoryId === c.categoryId)}
                    ></InterestBlock> //key to order the Block by their id
                )
            })
        ) : (
            <div>
                <p className={"error"}>No interests found!</p>
            </div>
        )

    }
}

