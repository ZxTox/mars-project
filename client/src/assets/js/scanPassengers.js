const RANDOM_MARS_ID_LENGTH = 7;
const SEAT_SELECTION_PAGE = "seat-selection.html";
document.addEventListener("DOMContentLoaded", init);

function init() {
    document.querySelector("#scan-ids > button").addEventListener("click", savePassengers);

    (function generateInputs() {
        const { amountOfPassengers } = getBookingMetadataLocalStorage();

        for (let i = 0; i < amountOfPassengers; i++) {
            document.querySelector(".input-fields").insertAdjacentHTML("beforeend", generateInputHtml(i + 1));
        }

        document.querySelectorAll(".input-fields button").forEach(button => button.addEventListener("click", fillRandomMarsId))
    })();

    function fillRandomMarsId(e) {
        e.preventDefault();
        const { for: inputFieldID } = e.target.dataset;
        document.querySelector(`#${inputFieldID}`).value = getRandomId();
    }

    function savePassengers() {
        const { amountOfPassengers } = getBookingMetadataLocalStorage();
        const inputs = [...document.querySelectorAll("input[name='marsid']")];

        if (localStorage.getItem("bookingData") && inputs.map(input => input.value).join('').length === (amountOfPassengers * RANDOM_MARS_ID_LENGTH)) {
            const currentData = JSON.parse(localStorage.getItem("bookingData"));
            currentData.passengers = inputs.map(input => (
                {
                    marsId: input.value,
                    seat: 'XH7'
                }
            ));

            localStorage.setItem("bookingData", JSON.stringify(currentData));

            window.location.replace(SEAT_SELECTION_PAGE);

        } else {
            createAlert("Info", "Please scan all the Mars IDs");
        }
    }
}

function generateInputHtml(index) {
    return `
    <div>
        <label for="marsid-${index}">MarsID passenger ${index}</label>
        <input type="text" name="marsid" id="marsid-${index}">
        <button class="btn " data-for="marsid-${index}">Scan ID</button>
    </div>`;
}

function getRandomId() {
    return [...Array(RANDOM_MARS_ID_LENGTH)].map(() => Math.random().toString(36)[2]).join('').toUpperCase();
}

