const sectorMap: { [key: number]: string } = {
    1: "Luminara",
    2: "Miragea",
    3: "Everdawn",
    4: "Abyssia",
    5: "Celestia",
    6: "Vitalis",
    7: "Nebulia",
    8: "Radiantia",
    9: "Serenara"
};

const getSectorName = function (sectorId: number) {
    return sectorMap[sectorId] || "Unknown sector";
}
export default getSectorName;