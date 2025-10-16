const urlParams = new URLSearchParams(window.location.search);
const houseId = urlParams.get('houseId');

//Расписание функционала кнопок
const buttonAction = [
    ["Дом", "EditHouse/editHouse.html", "images/House.jpg"],
    ["Штат", "https://google.com", "images/stuff.png"],
    ["Жители", "house.html", 'images/people.jpg'],
    ["На главную", "../index.html", "images/toHomePage.jpg"]
];

const buttonRange = buttonAction.length;
window.onload = function () {
    setHousePage();
};

window.onresize = function () {
    setHousePage();
}

function setHousePage() {

    getHouse().then(
        e => {
            house = e;
            buildHousePage();
        });

    function buildHousePage() {

        const main = document.getElementById('main');
        const title = document.getElementsByTagName('title');
        title.item(0).innerHTML += house.address;

        // Получаем ширину кнопки выбора действия, чтобы рассчитать количество колонок на текущую ширину страницы.
        main.innerHTML = "<button id=\"tempBtn\" class=\"house-button\" style=\"visibility: hidden;\"></button>";
        let Button = document.getElementById("tempBtn");
        let ButtonWidth = parseInt(getComputedStyle(Button).width);
        const buttonsInLine = Math.floor(main.offsetWidth / ButtonWidth);

        Button = null;
        main.innerHTML = "";
        let pageContent = ""; // Переменная для динамического формирования HTML-наполнения страницы

        main.style.setProperty('--column-count', buttonsInLine.toString());
        main.style.setProperty('--row-count', (Math.floor(buttonRange / buttonsInLine)).toString());

        buttonAction.forEach((e, index) => {
            pageContent += `
                <div class="house-div">
                    <button id="button-${index}" class="house-button" style="background-image: url(${e[2]});">
                        ${e[0]}
                    </button>
                </div>`;
        })

        main.innerHTML = pageContent;

        // Делегирование событий
        main.addEventListener('click', handleHouseButtonClick);
    }

    function handleHouseButtonClick(event) {
        // Находим конкретную кнопку, на которую кликнули
        const button = event.target.closest('.house-button');

        if (!button) return; // если кликнули мимо кнопки

        let choice = parseInt(button.id.replaceAll(/\D/g, ""));
        if (choice === 3) {
            history.back();
        } else {
            window.open(
                buttonAction[choice][1] + `?houseId=${houseId}`,
                '_blank',
                'noopener, noreferrer'
            );
        }

    }

}

// Получение объекта House для представления на странице его полей
async function getHouse() {
    try {
        const response = await fetch(`${BACKEND_URL}/houses/get/${houseId}`);

        if (!response.ok) {
            throw new Error(`Ошибка получения данных дома. Статус: ${response.status}`);
        }

        const house = await response.json();
        console.log('Получен дом:', house);
        return house;

    } catch (error) {
        console.error('Ошибка при получении данных дома:', error);
        return null;
    }

}
