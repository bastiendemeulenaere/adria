
import Checkbox from '@mui/joy/Checkbox';
import Sheet from '@mui/joy/Sheet';
import Box from '@mui/joy/Box';
import {TCategory} from "./common/category.tsx";

type TInterestBlockProps = {
    readonly interest: TCategory;
    readonly onChange: () => void;
    readonly isSelected: boolean;
}

export default function InterestBlock({interest, onChange, isSelected}: TInterestBlockProps) {

    return <Box
        className={"box-interest"}
    >
        <Sheet variant="outlined">
            <Checkbox overlay label={interest.name} onChange={onChange} checked={isSelected}/>
        </Sheet>
    </Box>;
}



