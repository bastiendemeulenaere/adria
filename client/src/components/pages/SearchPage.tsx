import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import evApi from "../../services/api-events.ts";
import catApi from "../../services/api-catergory.ts";
import secApi from "../../services/api-world.ts";
import axios, {CancelTokenSource} from "axios";
import EventBlock from "../EventBlock.tsx";
import {TEvent} from "../common/events.tsx";
import "../../assets/css/search.scss";
import Input from '@mui/joy/Input';
import Select from "@mui/joy/Select";
import Option from "@mui/joy/Option";
import {TCategory} from "../common/category.tsx";

function SearchPage() {
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [categories, setCategories] = useState<TCategory[]>([]);
    const [sectors, setSectors] = useState<TCategory[]>([]);
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [events, setEvents] = useState<TEvent[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<string>('');
    const [selectedSector, setSelectedSector] = useState<string>('');
    const [selectedAmountOfPeople, setSelectedAmountOfPeople] = useState<string>('');

    const navigate = useNavigate();

    useEffect(() => {
        const evReq: CancelTokenSource = axios.CancelToken.source();
        evApi.getEvents(evReq.token)
            .then((data) => {
                setEvents(data);
                setIsLoading(false);
            })
            .catch(() => {
                setIsLoading(false);
            });

        const catReq: CancelTokenSource = axios.CancelToken.source();
        catApi.getCategories(catReq.token)
            .then(data => {
                setCategories(data);
                setIsLoading(false);
            }).catch(() => {
            setIsLoading(false);
        });

        const secReq: CancelTokenSource = axios.CancelToken.source();
        secApi.getSectors(secReq.token)
            .then(data => {
                setSectors(data);
                setIsLoading(false);
            }).catch(() => {
            setIsLoading(false);
        });

        return () => {
            evReq.cancel();
        };
    }, []);

    const handleInputChange = (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        setSearchTerm(event.target.value);
        renderEvents(events, searchTerm);
    };


    const renderOptionsCategories = () => {
        const options = categories.map((category) => (
            <Option key={category.name} value={category.name}>
                {category.name}
            </Option>
        ));

        options.unshift(<Option key="all" value={""}>Select a Category</Option>);
        return options;
    };

    const filterCategories = (e: any) => {
        setSelectedCategory(e.target.innerText);

        renderEvents(events, searchTerm);
    }

    const renderOptionsSectors = () => {
        const options = sectors.map((sector) => (
            <Option key={sector.name} value={sector.name}>
                {sector.name}
            </Option>
        ));

        options.unshift(<Option key="all" value={""}>Select a Sector</Option>);
        return options;
    };

    const filterSectors = (e: any) => {
        e.target.selected = e.target.innerText;


        setSelectedSector(e.target.innerText);

        renderEvents(events, searchTerm);
    }

    const renderOptionsAmountOfPeople = () => {
        const peopleAmounts = ['5', '10', '15', '20', '30', '40', '50', '100', '150', '200', '250', '300', '350', '400', '500'];
        const options = peopleAmounts.map((personAmount) => (
            <Option key={personAmount} value={personAmount}>
                {personAmount}
            </Option>
        ));

        options.unshift(<Option key="all" value={""}>Select the Max Amount of People</Option>);
        return options;
    };

    const filterAmountOfPeople = (e: any) => {
        setSelectedAmountOfPeople(e.target.innerText);

        renderEvents(events, searchTerm);
    }

    return (
        <>
            <div id={"filterBar"}>
                <Input
                    placeholder="Search for an event..."
                    className={"searchBar"}
                    value={searchTerm}
                    onChange={handleInputChange}
                >
                </Input>
                <Select
                    placeholder="Category"
                    variant="outlined"
                    className={"select-field"}
                    value={selectedCategory}
                    onChange={filterCategories}
                >
                    {renderOptionsCategories()}
                </Select>
                <Select
                    placeholder="Sector"
                    variant="outlined"
                    className={"select-field"}
                    value={selectedSector}
                    onChange={filterSectors}
                >
                    {renderOptionsSectors()}
                </Select>
                <Select
                    placeholder="Amount of People"
                    variant="outlined"
                    className={"select-field"}
                    value={selectedAmountOfPeople}
                    onChange={filterAmountOfPeople}
                >
                    {renderOptionsAmountOfPeople()}
                </Select>
            </div>
            <div className={"homepage"}>
                <div className={"events"}>
                    {isLoading ? (
                        <p>Loading...</p>
                    ) : (
                        renderEvents(events, searchTerm)
                    )}
                </div>
            </div>
        </>
    );

    function renderEvents(events: TEvent[], searchTerm: string) {
        let filteredEvents = events;

        if (searchTerm) {
            filteredEvents = filteredEvents.filter(
                (e) =>
                    e.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    e.organiser.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    e.organiser.lastName.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        if (selectedCategory && selectedCategory !== "Select a Category") {
            filteredEvents = filteredEvents.filter((event) => {
                return event.category.name === selectedCategory;
            });
        }

        if (selectedSector && selectedSector !== "Select a Sector") {
            filteredEvents = filteredEvents.filter((event) => {
                return event.sector.name === selectedSector;
            });
        }

        if (selectedAmountOfPeople && selectedAmountOfPeople !== "Select the max Amount Of People") {
            filteredEvents = filteredEvents.filter((event) => {
                return event.amountOfPeople <= parseInt(selectedAmountOfPeople);
            });
        }

        if (!filteredEvents || filteredEvents.length === 0) {
            return (
                <div>
                    <p className={"error"}>No events found!</p>
                </div>
            );
        }

        return filteredEvents.map((e) => (
            <EventBlock
                key={e.id}
                event={e}
                onClick={() => navigate(`/event/view/${e.id}`)}
            ></EventBlock>
        ));
    }
}

export default SearchPage;
