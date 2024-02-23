import {TCategory} from "./common/category";
import React, {Dispatch, SetStateAction, useEffect, useState} from "react";
import axios, {CancelTokenSource} from "axios";
import catApi from "../services/api-catergory";


interface CategoryBarProps {
    readonly filters: Set<number>;
    readonly setFilters: Dispatch<SetStateAction<Set<number>>>;
}
export default function CategoryBar({ filters, setFilters }: CategoryBarProps) {

    const [categories, setCategories] = useState<TCategory[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const catReq: CancelTokenSource = axios.CancelToken.source();
        catApi.getCategories(catReq.token).then(data => {
            setCategories(data);
            setLoading(false);
        }).catch(() => {
            setLoading(false);
        });

        return () => {
            catReq.cancel();
        }
    }, [])


    function handleFilterClick(e: React.MouseEvent<HTMLButtonElement>, c: number) {
        e.preventDefault();
        setFilters(prev => {
            const updatedFilters = new Set(prev);
            if (updatedFilters.has(c)) {
                updatedFilters.delete(c);
            } else {
                updatedFilters.add(c);
            }
            return updatedFilters;
        });
    }

    function renderCategorybar(categories: TCategory[]) {
        return categories && categories.length > 0 ? (
            <ul>
                {categories.map((c) => (
                    <li key={c.name}>
                        <button className={`buttons capitalize ${filters.has(c.categoryId) ? 'pressed' : ""}`} onClick={(e) => handleFilterClick(e, c.categoryId)}>{c.name}</button>
                    </li>
                ))}
            </ul>
        ) : (
            <p>No categories found!</p>
        );
    }

    return <header>
        {loading ? (
            <p>Loading...</p>
        ) : (
            renderCategorybar(categories)
        )}
    </header>
}

