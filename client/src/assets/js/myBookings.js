const API_URL = `${API_BASE_URL}/api/bookings`;


document.addEventListener("DOMContentLoaded", init);

async function init() {

    (async function showBookings() {
        const res = await getBookings();
        res.bookings.forEach(booking => {
            document.querySelector("#ticket-overview > #my-tickets").insertAdjacentHTML("beforeend", generateHTML(booking));
            document.querySelectorAll('#my-tickets > article').forEach(ticket => ticket.addEventListener("click", getTicketDetails));
            document.querySelectorAll('#my-tickets > article > *').forEach(ticket => ticket.addEventListener("click", (e) => {
                e.stopPropagation();
                e.currentTarget.parentElement.click();
            }));

        });
    })();
}

async function getBookings() {
    return await apiCall(`${API_URL}`);
}

function generateHTML(booking) {
    return `
    <article class="ticket"
    data-booking=${JSON.stringify(booking)}
    >
    <h2>${booking.flight.flightId}</h2>
    <p>${formatter.format(booking.price)}</p>
    <div>
        <p>${booking.ticketClass} class</p>
        <p>MarsMile aircraft</p>
    </div>
    <p>
        <svg class="time-icon" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M10 20C4.47715 20 0 15.5228 0 10C0 4.47715 4.47715 0 10 0C15.5228 0 20 4.47715 20 10C19.9939 15.5203 15.5203 19.9939 10 20ZM10 2C5.58172 2 2 5.58172 2 10C2 14.4183 5.58172 18 10 18C14.4183 18 18 14.4183 18 10C17.995 5.58378 14.4162 2.00496 10 2ZM15 11H9V5H11V9H15V11Z"
                fill="#494949" />
        </svg>
        ${booking.flight.travelTime} months
    </p>
    </article> 
    `;

}

function getTicketDetails(e) {
    e.stopPropagation();

    editTicketCss();

    const { booking } = e.target.dataset;

    document.querySelector("#my-tickets-info").innerHTML = "";
    document.querySelector("#my-tickets-info").insertAdjacentHTML("beforeend", generateDetailsHTML(JSON.parse(booking)));
}

function editTicketCss() {
    document.querySelector('#my-tickets').style.width = "70%";
    document.querySelector('#my-tickets-info').style.display = "block";
}



function generateDetailsHTML(booking) {
    const depart = new Date(booking.flight.departTime);
    const arrival = new Date(booking.flight.estimatedArrival);

    const passengers = generatePassengersList(booking.passengers);

    return `
    <div id="mainTicketInfo">
                           <div id="ticketGeneralInfo">
                               <h5>Ticket Number: ${booking.flight.flightId}</h5>
                               <p>expires: ${depart}</p>
                           </div>
                           <div id="price">
                               <p>€ ${booking.price}</p>
                               <p>${booking.paymentStatus}</p>
                           </div>
                       </div>
                       <div id="ticketArrivalInfo">
                           <div>
                               <h5>Class:</h5>
                               <p>${booking.ticketClass}</p>
                           </div>
                           <div>
                               <h5>Destination:</h5>
                               <p>${booking.flight.destinationPlanet}</p>
                           </div>
                            <div>
                               <h5>Flight Departure:</h5>
                               <p>${depart.toLocaleDateString()} ${depart.toLocaleTimeString()}</p>
                           </div>
                            <div>
                               <h5>Estimated time of arrival:</h5>
                               <p>${arrival.toLocaleDateString()} ${arrival.toLocaleTimeString()}</p>
                           </div>

                            ${passengers}
                       </div>
   `;
}

function generatePassengersList(passengers) {
    let html = "";
    passengers.forEach(passenger => {
        html += `
        <div>
            <h5>MarsId: ${passenger.marsId}</h5>
            <p>Seat: ${passenger.seat}</p>
        </div>
        `
    });

    return html;
}
