const CHNL_TO_CLIENTS_BROADCAST = 'events.to.clients';
const CHNL_TO_SERVER = 'events.to.server';
const EVENTBUS_PATH = `${API_BASE_URL}/events/`;
const CHNL_TO_CLIENT_UNICAST = 'events.to.client.';

function openSocket() {
    let eb = new EventBus(EVENTBUS_PATH);

    let clientId = localStorage.getItem('clientId');

    if (localStorage.getItem('clientId') === null) {
        clientId = uuidv4();
        localStorage.setItem('clientId', clientId);
    }

    function sendToServer(message) {
        message.clientId = clientId;
        eb.send(CHNL_TO_SERVER, message);
    }

    eb.onopen = function () {
        eb.registerHandler(CHNL_TO_CLIENTS_BROADCAST, onMessage);
        eb.registerHandler(CHNL_TO_CLIENT_UNICAST + clientId, onMessage);
    };

    return sendToServer;
}
