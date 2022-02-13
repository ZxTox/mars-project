document.addEventListener('DOMContentLoaded', init);

function init() {
    document.querySelector('#container header').innerHTML = getIndexHeaderHTML();

    function getIndexHeaderHTML() {
        return `
        <a href="index.html">
            <svg viewBox="0 0 62 58" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M31 0L38.1844 22.1115H61.4338L42.6247 35.7771L49.8091 57.8885L31 44.2229L12.1909 57.8885L19.3753 35.7771L0.566191 22.1115H23.8156L31 0Z" fill="black"/>
            </svg>
            <h1>marsmile</h1>
        </a>
        <a class="btn btn-black" href="enter.html">Enter</a>
    `;
    }
}
