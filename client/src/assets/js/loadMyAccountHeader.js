document.addEventListener('DOMContentLoaded', init);

function init(){
    document.querySelector('#container header a').insertAdjacentHTML('afterend', getMyAccountHeader());

    function getMyAccountHeader(){
        return `
        <div id="my-account-mainbar">
                    
                    <div>
                        <svg viewBox="0 0 130 109" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd" clip-rule="evenodd"
                                d="M16.7507 108.353C22.671 87.2646 42.0394 71.8023 65.0191 71.8023C87.8972 71.8023 107.196 87.1281 113.208 108.073C123.469 96.6013 129.707 81.456 129.707 64.8537C129.707 29.036 100.671 0 64.8537 0C29.036 0 0 29.036 0 64.8537C0 81.5913 6.34053 96.8479 16.7507 108.353ZM87.3538 42.3534C87.3538 54.7799 77.2801 64.8537 64.8536 64.8537C52.427 64.8537 42.3533 54.7799 42.3533 42.3534C42.3533 29.9268 52.427 19.8531 64.8536 19.8531C77.2801 19.8531 87.3538 29.9268 87.3538 42.3534Z"
                                fill="#C4C4C4" />
                        </svg>
                        <h4>John Doe</h4>
                        <a href="index.html">logout</a>
                    </div>
                </div>
    `;
    }
}
