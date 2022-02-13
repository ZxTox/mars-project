const MAX_FRAME_LENGTH = 65536;
const CHAT_UPLOAD_IMAGES_URL = "https://upload.zxtox.com/upload";

let sendToserver;

document.addEventListener('DOMContentLoaded', init);

function init() {
    document.querySelector('#sendChatMessage').addEventListener('click', sendMessage);

    document.querySelector('#typeMessage').addEventListener('keyup', (e) => {
        // Enter on input field
        if (e.keyCode === 13) {
            e.preventDefault();
            sendMessage(e);
        }
    });

    document.querySelector('#join').addEventListener('click', join);
    sendToserver = openSocket();

    document.querySelector('#uploadeImage').addEventListener('click', (e) => {
        document.querySelector('#file').click();
    });

    document.querySelector('#file').addEventListener('change', handleImageUpload);
}


async function postImageFile(file) {
    try {
        const formData = new FormData()
        formData.append('myImage', file)
        const data = await fetch(CHAT_UPLOAD_IMAGES_URL, {
            method: 'POST',
            body: formData
        });
        return data.json();
    } catch (err) {
        return err;
    }
}


function handleImageUpload(e) {
    const reader = new FileReader();
    reader.onload = async function () {

        const uploadedImage = await postImageFile(document.querySelector("#file").files[0]);

        if (uploadedImage.file) {
            sendToserver({ type: 'message', message: uploadedImage.file });
        } else {
            reduceBase64Length(this.result).then(compressedBase64 => {
                if (compressedBase64.length > MAX_FRAME_LENGTH) {
                    createAlert("Info", "Image is too large please upload another image!");
                    return;
                }
                sendToserver({ type: 'message', message: compressedBase64 });
            });
        }

    };
    reader.readAsDataURL(this.files[0]);
}

function join(e) {
    e.preventDefault();

    const username = document.querySelector('#username').value;
    const data = { type: 'join', username };
    sendToserver(data);
}

function sendMessage(e) {
    e.preventDefault();

    const { value: message } = document.querySelector('#typeMessage');


    if (message.trim("").length < 1) {
        createAlert("Info", "Cannot send empty message!");
        return null;
    }

    const data = { type: 'message', message: message };

    sendToserver(data);

    document.querySelector('#typeMessage').value = '';

    loadUsers();
}

function onMessage(error, message) {
    const $messageEl = document.querySelector("#messages");
    $messageEl.insertAdjacentHTML("beforeend", generateChatMessageHtml(message));

    // Scroll down
    $messageEl.scrollTop = $messageEl.scrollHeight;

    if (error) {
        createAlert("Info", "Something went wrong!");
    }
}


function generateChatMessageHtml(message) {
    const today = new Date();
    const time = `${String(today.getHours()).padStart(2, '0')}:${String(today.getMinutes()).padStart(2, '0')}`;

    const parsedMessage = JSON.parse(message.body);

    let html = `<p>${parsedMessage.msg}</p>`;

    if (parsedMessage.msg.includes(".mp4") && parsedMessage.msg.includes("http")) {
        html = `
        <video width="300" height="180" controls>
            <source src="${parsedMessage.msg}" type="video/mp4">
        </video>
        `;
    } else if (isBase64(parsedMessage.msg) || parsedMessage.msg.includes("uploads/myImage")) {
        html = `<img src="${parsedMessage.msg}" alt="image sent from ${parsedMessage.from}" />`;
    }

    return `
        <article class="${parsedMessage.clientId === localStorage.getItem("clientId") ? "messageReceived" : "messageSent"}">
            <h5>${parsedMessage.from}</h5>
            ${html}
            <h6>${time}</h6>
        </article>`;
}
