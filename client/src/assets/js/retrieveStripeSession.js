const API_URL = `${API_BASE_URL}/api/sessions`;
const queryString = window.location.search;
const parameters = new URLSearchParams(queryString);
document.addEventListener("DOMContentLoaded", init);


async function init() {
    try {
        const res = await retrieveSession();
        if (res.failure) {
            return handleError(res.cause);
        }

        addPassengers(res);

        document.querySelector("h3").innerText = `${formatter.format(res.totalPrice)}`;
        document.querySelector("p").innerText = `${res.quantity} persons, ${res.isSingleWay ? 'Single-way' : 'Round-Trip'}, ${res.ticketClass}-class`;
    } catch {
        handleError("Something went wrong: ERROR API")
    }

}

async function retrieveSession() {
    const res = await fetch(`${API_URL}/${parameters.get("session_id")}`);
    return await res.json();
}

function addPassengers(passengers) {
    const { pricePerPerson } = passengers;
    passengers.class.forEach(passenger => {
        document.querySelector(".passengers").insertAdjacentHTML("beforeend", generateHTML(passenger, pricePerPerson));
    });
}

function generateHTML(passenger, pricePerPerson) {
    const qrCode = QRCode.generatePNG(passenger.uniqueTicketNumber, {
        ecclevel: "M",
        textcolor: "#D13438",
        format: "html",
        margin: 4,
        modulesize: 8
    });
    return `<div class="passenger">
        <h4>Seat: ${passenger.seat}</h4>
        <p>${formatter.format(pricePerPerson)}</p>
        <img src="${qrCode}" alt="${passenger.uniqueTicketNumber} ticket number" />
    </div>`;
}

function handleError(cause) {
    document.querySelector("h2").innerText = "Error invalid or non-existent session id";
    document.querySelector("h3").innerText = `ERROR!`;
    document.querySelector("p").innerText = `${cause.split(":")[0]}`;
    document.querySelector("main").classList.add("center");
}
