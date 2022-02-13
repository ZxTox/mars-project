const API_URL = `${API_BASE_URL}/api/flights`;
document.addEventListener('DOMContentLoaded', init);

function init() {
	document
		.querySelector('#search')
		.addEventListener('click', toggleFlightsDisplay);

	document.querySelector("#location").addEventListener("change", filterFlights);

	function toggleFlightsDisplay(e) {
		e.preventDefault();
		saveBookingData();
	}

}

function saveBookingData() {
	const { value: departPlanet } = document.querySelector("#location");
	const { value: flightClass } = document.querySelector("#class");
	const { value: departDate } = document.querySelector("#depart");
	const { value: amountOfPassengers } = document.querySelector("#passengers");

	if (!departPlanet || !flightClass || !amountOfPassengers) {
		createAlert("Info", "Please fill in the fields");
		return;
	}

	showFlights(departDate, departPlanet);

	if (localStorage) {
		const metadata = {
			isSingleWay: true,
			amountOfPassengers,
			class: flightClass
		}

		localStorage.setItem("bookingData", JSON.stringify(metadata));
	}
}

function filterFlights(e) {
	e.preventDefault();

	document.querySelectorAll('.flight').forEach(location => location.classList.add("hidden"));
	document.querySelectorAll(`.flight[data-destination="${e.target.value}"]`).forEach(location => location.classList.remove("hidden"));
}

async function getFlights(departDate, destination) {
	const options = departDate ? `?date=${departDate}&destination=${destination.toUpperCase()}` : '';
	const data = await fetch(`${API_URL}${options}`);
	return await data.json();
}

async function showFlights(departDate, destination) {
	document.querySelector("#flights").innerHTML = '';
	const data = await getFlights(departDate, destination);


	const $flight = document.querySelector('#flights');

	if (data.flights.length > 0) {
		$flight.classList.remove("hidden");
		document.querySelector("#no-flights").classList.add("hidden");


	} else {
		$flight.classList.add("hidden");
		document.querySelector("#no-flights").classList.remove("hidden");
	}

	data.flights.forEach(flight => {
		document.querySelector("#flights").insertAdjacentHTML("beforeend", generateHTML(flight));
	});

	document.querySelectorAll(".flight a").forEach(btn => btn.addEventListener("click", saveFlightId));

}

function generateHTML(flight) {
	const depart = new Date(flight.departTime);

	return `
	<div class="flight" data-destination="${flight.destinationPlanet.toLowerCase()}">
		<p>To: ${flight.destinationPlanet}</p>
		<div>
			<p>${capitalize(JSON.parse(localStorage.getItem("bookingData")).class)} class</p>
			<p>MarsMile flight: ${flight.flightId}</p>
		</div>
		<p>
			<svg class="time-icon" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
				<path
					d="M10 20C4.47715 20 0 15.5228 0 10C0 4.47715 4.47715 0 10 0C15.5228 0 20 4.47715 20 10C19.9939 15.5203 15.5203 19.9939 10 20ZM10 2C5.58172 2 2 5.58172 2 10C2 14.4183 5.58172 18 10 18C14.4183 18 18 14.4183 18 10C17.995 5.58378 14.4162 2.00496 10 2ZM15 11H9V5H11V9H15V11Z"
					fill="#494949" />
			</svg>
			${flight.travelTime} months
		</p>
		<p class="departureTime">date: ${depart.toLocaleDateString()}</p>
		<a data-flight="${flight.flightId}" href="scan-passengers.html">Book</a>
	</div>
	`
}

function saveFlightId(e) {

	if (document.querySelector("#passengers").value === "") {
		e.preventDefault();
		createAlert("Info", "Please fill in amount of passengers!");
	}

	const { flight: flightId } = e.target.dataset;

	if (localStorage) {
		localStorage.setItem("flightId", JSON.stringify(flightId));
	}


}

const capitalize = (text) => text.replace(text.charAt(0), text.charAt(0).toUpperCase());
