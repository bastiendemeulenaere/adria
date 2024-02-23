const AmountOfAttendees = function ({attendees, amount}: { attendees: [number, number], amount?: number }) {
    return (!amount ?
        <span>{attendees[0] - attendees[1]}</span> :
        <span>{attendees[0] - attendees[1]} ({amount})</span>);
}
export default AmountOfAttendees;