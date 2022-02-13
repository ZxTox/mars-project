document.addEventListener('DOMContentLoaded', init);

const navLinks = [
	{
		name: 'My account',
		link: 'my-account.html',
	},
	{
		name: 'About us',
		link: 'about.html',
	},
	{
		name: 'Flight classes',
		link: 'flight-classes.html',
	},
	{
		name: 'Contact',
		link: 'contact.html',
	},
];

function init() {
	addToContainer();
	//functions

	function addToContainer() {
		const $containerEl = document.querySelector('#container');
		$containerEl.insertAdjacentHTML('afterbegin', getHeaderHTML());
		$containerEl.insertAdjacentHTML('beforeend', getFooterHTML());

	}
}

function getHeaderHTML() {
	return `<header>
        <a href="index.html">
            <svg viewBox="0 0 62 58" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M31 0L38.1844 22.1115H61.4338L42.6247 35.7771L49.8091 57.8885L31 44.2229L12.1909 57.8885L19.3753 35.7771L0.566191 22.1115H23.8156L31 0Z" fill="black"/>
            </svg>
            <h1>marsmile</h1>
        </a>

        <button class="btn btn-black"  id="go-back">Back</button>

    </header>`;
}

function getFooterHTML() {
	let linksHTML = '';
	navLinks.forEach(
		(link) =>
			(linksHTML += `<li><a href="${link.link}">${link.name}</a></li>`)
	);
	return `<footer>
        <nav>
            <ul>

            ${linksHTML}

			</ul>
        </nav>
    </footer>`;
}
