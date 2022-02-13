const API_SEATS = `${API_BASE_URL}/api/seats`;


const ROWS = 10;
const COLS = 10;
const selectedSeats = Array.from(Array(ROWS), () => Array.from(Array(COLS)));
const alphabet = String.fromCharCode(...Array(123).keys()).slice(97).toUpperCase();


const selectedSeatsNumbers = () => selectedSeats.flat().filter(seat => seat);

document.addEventListener("DOMContentLoaded", init);

function init() {

    document.querySelector("#seat-selection > button").addEventListener("click", redirectToCheckout);

    (async function addSeats() {
        const $row = document.querySelector(".row");
        const bookedSeats = await getBookedSeats();
        for (let k = 0; k < ROWS; k++) {
            for (let i = 0; i < COLS; i++) {
                let isBooked = false;
                if (bookedSeats.seats.includes(`${alphabet[k]}${i}`)) {
                    isBooked = true;
                }

                $row.insertAdjacentHTML("beforeend", `<svg width="29" height="29" viewBox="0 0 29 29" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path class="${isBooked ? "booked-seat" : ""}" d="M0.768799 4.11084C0.768799 1.9017 2.55966 0.11084 4.7688 0.11084H24.8161C27.0252 0.11084 28.8161 1.9017 28.8161 4.11084V28.1581H0.768799V4.11084Z" fill="#D8D8D8"/>
                    </svg>
                    `);

            }

        }

        document.querySelectorAll("path").forEach(seat => seat.addEventListener("click", handleSeatClick));
    })();


}

async function getBookedSeats() {
    return await apiCall(`${API_SEATS}/${JSON.parse(localStorage.getItem("flightId"))}`);
}

async function handleSeatClick(e) {
    e.preventDefault();
    const bookedSeats = await getBookedSeats();


    const index = Array.prototype.indexOf.call(document.querySelector(".row").children, e.target.parentElement);
    const seatRow = index % ROWS;
    const seatCol = index < 10 ? 0 : Array.from(String(index), (num) => parseInt(num))[0];

    if (bookedSeats.seats.includes(`${alphabet[seatCol]}${seatRow}`)) {
        createAlert("Info", "Cannot select a booked seat");
        return;
    }

    if (checkForMaxSelectedSeats(e)) {
        createAlert("Info", "Cannot select any more seats!");
        return;
    }

    selectedSeats[seatCol][seatRow] = e.target.classList.contains("active") ? undefined : `${alphabet[seatCol]}${seatRow}`;


    e.target.classList.toggle("active");
    document.querySelector("#seat-selection>p").innerText = `Selected seats: ${selectedSeats.flat().filter(seat => seat).join(", ")}`;

}

function checkForMaxSelectedSeats(e) {
    const amountOfPassengers = parseInt(getBookingMetadataLocalStorage().amountOfPassengers);
    const { length: selectionSize } = selectedSeatsNumbers();




    document.querySelector("#seat-selection > button").disabled = !(selectionSize === amountOfPassengers - 1 && !e.target.classList.contains("active"));

    if (selectionSize === amountOfPassengers && !e.target.classList.contains("active")) {
        document.querySelector("#seat-selection > button").disabled = false;
        return true;
    }

    return false;
}

function redirectToCheckout(e) {
    e.preventDefault();

    if (localStorage) {
        localStorage.setItem("seats", JSON.stringify(selectedSeatsNumbers()));
    }


    window.location.href = "./confirm-booking.html";
}
