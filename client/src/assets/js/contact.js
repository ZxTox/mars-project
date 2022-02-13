"use strict";
document.addEventListener('DOMContentLoaded', init);


function init() {
    document.querySelector('#sendMessage').addEventListener('click', openPopUp);
    document.querySelector('#closepopup').addEventListener('click', closePopup);

    function openPopUp(e) {
        e.preventDefault();
        const contact = document.querySelector('#contact-page');
        const popup = document.querySelector('#contactpopup');
        const nameValue = document.querySelector('#name').value;
        const emailValue = document.querySelector('#email').value;
        const messageValue = document.querySelector('#message').value;

        insertHTML(contact, popup, nameValue, emailValue, messageValue);
    }
}

function insertHTML(contact, popup, nameValue, emailValue, messageValue) {
    if (nameValue.length !== 0 || emailValue.length !== 0 || messageValue.length !== 0) {
        contact.style.display = "none";
        popup.style.display = "block";
        document.querySelector('#thankyoumessage').insertAdjacentHTML("afterbegin", getPopUpHTML());
    } else {
        createAlert("Info", "Please fill in all the fields!");
    }
}

function closePopup(e) {
    e.preventDefault();
    const contact = document.querySelector('#contact-page');
    const popup = document.querySelector('#contactpopup')

    document.querySelector('#name').value = "";
    document.querySelector('#email').value = "";
    document.querySelector('#message').value = "";

    contact.style.display = "flex";
    popup.style.display = "none";
    document.querySelector('#thankyoumessage').innerHTML = "";

}

function getPopUpHTML() {
    const person = document.querySelector('#name').value;
    const message = document.querySelector('#message').value;
    return `<p>Thank you ${person}, Your message has been sent succesfully!</p>
                <p>"${message}"</p>`;
}
