export type TCoordinate = {
    x: number;
    y: number;
};

export type TCoordinateRange = {
    start: TCoordinate;
    end: TCoordinate;
};

export type TDangerousArea = {
    reason: string;
    coordinateRange: TCoordinateRange;
};

export type World = {
    name: string;
    coordinateRange: TCoordinateRange;
    dangerousAreas: TDangerousArea[];
};
export type TSector = {
    id: number;
    name: string;
}
export type TSectorLocation = {
    sector: TSector;
    coordinate: TCoordinate;
}