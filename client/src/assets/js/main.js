const ALERT_DURATION = 5500;
const API_BASE_URL = window.location.href.includes("project-ii.ti.howest.be") ? "https://project-ii.ti.howest.be/mars-04" : "http://localhost:8080";

let config;
let api;
let formatter;


document.addEventListener("DOMContentLoaded", init);

async function init() {
    config = await loadConfig();
    api = `${config.host ? config.host + '/' : ''}${config.group ? config.group + '/' : ''}api/`;

    // Very small proof of concept.
    // poc();
    formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'EUR',
    });

    // Go back
    document.querySelector('#go-back').addEventListener('click', () => {
        history.back();
    });
}

async function loadConfig() {
    const response = await fetch("config.json");
    return await response.json();
}

function getBookingMetadataLocalStorage() {
    return JSON.parse(localStorage.getItem("bookingData"));
}

async function apiCall(url) {
    try {
        const data = await fetch(url);
        return data.json();
    } catch (ex) {
        createAlert("API Info", "Something went wrong!");
    }
}

function createAlert(title, message) {
    if (!document.querySelector("aside")) {
        document.querySelector("body").insertAdjacentHTML("afterbegin", "<aside></aside>");
    }
    document.querySelector("aside").insertAdjacentHTML("beforeend", `
    <div>
		<h2>${title}!</h2>
		<p>${message}</p>
	</div>`);

    setTimeout(() => {
        document.querySelectorAll("aside > div")[0].remove();
    }, ALERT_DURATION);
}

const uuidv4 = () => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0,
            v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
}

const isBase64 = (str) => {
    return str.startsWith("data:image") && str.length > 255;
}

/**
 * Resize a base 64 Image
 * @param {String} base64Str - The base64 string (must include MIME type)
 * @param {Number} MAX_WIDTH - The width of the image in pixels
 * @param {Number} MAX_HEIGHT - The height of the image in pixels
 */
async function reduceBase64Length(base64Str, MAX_WIDTH = 200, MAX_HEIGHT = 200) {
    return new Promise((resolve) => {
        let img = new Image();
        img.src = base64Str;
        img.onload = () => {
            let canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;

            if (width > height) {
                if (width > MAX_WIDTH) {
                    height *= MAX_WIDTH / width;
                    width = MAX_WIDTH;
                }
            } else {
                if (height > MAX_HEIGHT) {
                    width *= MAX_HEIGHT / height;
                    height = MAX_HEIGHT;
                }
            }
            canvas.width = width;
            canvas.height = height;
            let ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);
            resolve(canvas.toDataURL()); // this will return base64 image results after resize
        }
    });
}
