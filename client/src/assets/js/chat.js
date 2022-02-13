const USERS_CHAT_API = '/api/chatusers';

document.addEventListener("DOMContentLoaded", init);

function init() {
    const $logInButton = document.querySelector('#join');
    $logInButton.addEventListener('click', logIn);


    function logIn() {
        const { value: username } = document.querySelector('#username');

        if (username.trim().length < 1) {
            createAlert("Info", "Please enter a valid username!");
            return null;
        }

        document.querySelector('#chatScreen').style.display = "block";
        document.querySelector('#listOfUsers').style.display = "block";
        document.querySelector('#loginInput').style.display = "none";

        loadUsers();
    }

}


async function loadUsers() {
    document.querySelector("#chatusers").innerHTML = "";
    const users = await apiCall(`${API_BASE_URL}${USERS_CHAT_API}`);

    users.users.forEach(chatuser => {
        document.querySelector("#chatusers").insertAdjacentHTML("beforeend", `<li>${chatuser}</li>`);
    });
}
