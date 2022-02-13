const API_URL = `${API_BASE_URL}/api/sessions`;
const stripe = Stripe("pk_test_51JrQEKAi0vwMn6lUKQDQmS5RtD8PuFcdzsoIYCzUFz1Y0qlGZkJ1Fd7czfFaN58E0sNcBRevTh6g1Pttk43M02r300hYGhttXm");

const API_FLIGHTS = `${API_BASE_URL}/api/flights`;


document.addEventListener('DOMContentLoaded', init);


// Example data that should be sent to the server
const data = {
	"flightId": JSON.parse(localStorage.getItem("flightId")),
	"class": getBookingMetadataLocalStorage().class,
	"isSingleWay": true,
	"passengers": [
		// dynamic
	]
};



async function init() {
	JSON.parse(localStorage.getItem("bookingData")).passengers.forEach((passenger, index) => data.passengers.push({
		"marsId": passenger.marsId,
		"seat": JSON.parse(localStorage.getItem("seats"))[index]
	}));

	document.querySelector("#persons").innerText = `${data.passengers.length} persons`;
	document.querySelector("#price").innerText = `Selected seats: ${data.passengers.map(el => el.seat).join(", ")}`;

	document.querySelector('#btn-confirm').addEventListener('click', getPaymentUrl);

	getFlight();
}

async function getFlight() {
	const allFlights = await apiCall(API_FLIGHTS);
	const [flight] = allFlights.flights.filter(flight => flight.flightId === data.flightId);
	const departDate = new Date(flight.departTime);
	const arrivalDate = new Date(flight.estimatedArrival);

	document.querySelector("#depart").innerText = `Depart: ${departDate.toLocaleDateString()} ${departDate.toLocaleTimeString()}`;
	document.querySelector("#arrival").innerText = `Estimated Arrival: ${arrivalDate.toLocaleDateString()} ${arrivalDate.toLocaleTimeString()}`;
}

async function postRequest(url, data) {
	const dataAPI = await fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(data),
	});

	return await dataAPI.json();
}

async function getPaymentUrl(e) {
	e.preventDefault();
	const stripeData = await postRequest(API_URL, data);

	if (stripeData.failure) {
		createAlert("Info", "Something went wrong, cannot generate payment url.");
		console.log("cannot proceed error!");
		return;
	}

	e.target.disabled = true;
	addPayButton(stripeData);

}

function addPayButton(stripeData) {
	document
		.querySelector('#confirm-booking')
		.insertAdjacentHTML(
			'beforeend',
			`< button class="btn btn-pay" > Pay now!</button > `
		);

	document.querySelector('.btn-pay').addEventListener('click', async (e) => {
		e.preventDefault();
		const result = await stripe.redirectToCheckout({
			sessionId: stripeData.id,
		});

		if (result.error) {
			createAlert("Info", "Something went wrong with the payment.");
			console.log(result.error.message)
		}
	});
}
