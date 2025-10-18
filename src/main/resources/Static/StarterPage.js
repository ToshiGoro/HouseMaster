var house = [];
const main = document.getElementById("main");

window.onload = function () {
    setStartPage();
};

window.onresize = function () {
    setStartPage();
}

function setStartPage() {

    getAllHouses().then(
        housesArray => {
            house = housesArray;
            buildStartPage();
        });

    function buildStartPage() {

        let houseRange = house.length; // Количество обслуживаемых домов

        // Получаем ширину кнопки дома, чтобы рассчитать количество колонок на текущую ширину страницы.
        main.innerHTML = "<button id=\"tempBtn\" class=\"house-button\" style=\"visibility: hidden;\"></button>";
        let houseButton = document.getElementById("tempBtn");
        let houseButtonWidth = parseInt(getComputedStyle(houseButton).width);
        const buttonsInLine = Math.floor(main.offsetWidth / houseButtonWidth);

        houseButton = null;
        main.innerHTML = "";
        let pageContent = ""; // Переменная для динамического формирования HTML-наполнения страницы

        main.style.setProperty('--column-count', buttonsInLine.toString());
        main.style.setProperty('--row-count', (Math.floor(houseRange / buttonsInLine)).toString());

        pageContent += "<div class=\"house-div\">" +
            "<button id=\"newHouse\" class=\"house-button house-button--new\">Создать</button></div>";

        main.innerHTML = pageContent;

        createHouseButtons(house);

    }

}

/**
 * Отработка нажатия кнопки с названием ТСЖ
 * @param buttonId ID кнопки, автоматически присвоенный при её создании.
 */
function houseBtnClicked(buttonId) {
    let checkSign = document.getElementById("check" + buttonId.slice(3));
    checkSign.style.visibility = checkSign.style.visibility === 'visible' ? 'hidden' : 'visible';
}

function selectedHouses(selection) {

    if (selection === "all") {
        console.log(house);
    }

    if (selection === "group") {
        let selHouses = [];
        for (let i = 0; i < house.length; i++) {
            if (document.getElementById("check" + i).style.visibility === 'visible') {
                selHouses.push(house[i]);
            }
        }
        console.log(selHouses);
    }

}

// Получение списка всех домов для формирования стартовой страницы
async function getAllHouses() {
    try {
        const response = await fetch(`${BACKEND_URL}/houses/getAllForStartPage`);

        if (!response.ok) {
            throw new Error(`Ошибка получения списка домов. Статус: ${response.status}`);
        }

        const houses = await response.json();
        return houses;

    } catch (error) {
        console.error('Ошибка при получении списка домов:', error);
        return [];
    }
}

function createHouseButtons(houses) {
    main.innerHTML += houses.map((house, index) => `
        <div class="house-div">
            <button class="house-button" data-house-id="${house.id}" data-index="${index}">
                <div class="button-div">
                    <span class="span1">${house.address}</span>
                    ${house.hoaName}
                </div>
            </button>
            <label class="check-sign">
                <img id="check${index}" src="images/CheckSign.png" width="40" height="40" style="visibility: hidden;">
            </label>
        </div>
    `).join('');

    // Делегирование событий
    main.addEventListener('click', handleHouseButtonClick);
}

function handleHouseButtonClick(event) {
    // Находим конкретную кнопку, на которую кликнули
    const button = event.target.closest('.house-button');

    if (!button) return; // если кликнули мимо кнопки

    switch (button.id) {
        case "newHouse":
            // window.location.href = 'House/EditHouse/editHouse.html';
            window.open('House/EditHouse/editHouse.html',
            '_blank',
            'noopener, noreferrer'
        );
            break;
        case "all":
            alert("All");
            break;
        case "group":
            alert("Group");
            break;
        default:
            const houseId = button.dataset.houseId;
            window.location.href = `House/house.html?houseId=${houseId}`;
            break;
    }

    // Выполняем действия с выбранной кнопкой
    // resetAllCheckMarks();          // сбрасываем все галочки
    // showCheckMark(index);          // показываем галочку на этой кнопке
    // highlightSelectedButton(button); // подсвечиваем кнопку
    // processHouseSelection(houseId, houseData); // основная логика
}

function resetAllCheckMarks() {
    document.querySelectorAll('.check-sign img').forEach(img => {
        img.style.visibility = 'hidden';
    });
}

function showCheckMark(index) {
    const checkImg = document.getElementById(`check${index}`);
    if (checkImg) {
        checkImg.style.visibility = 'visible';
    }
}

function highlightSelectedButton(selectedButton) {
    // Сбрасываем подсветку у всех кнопок
    document.querySelectorAll('.house-button').forEach(btn => {
        btn.style.borderColor = '#ddd';
        btn.style.transform = 'scale(1)';
    });

    // Подсвечиваем выбранную
    selectedButton.style.borderColor = '#007bff';
    selectedButton.style.transform = 'scale(1.02)';
}

function processHouseSelection(houseId, houseData) {
    console.log('Выбран дом ID:', houseId);
    console.log('Данные дома:', houseData);

    // Здесь твоя основная логика:
    // - Переход на другую страницу
    // - Открытие модального окна
    // - Отправка данных на сервер
    // - и т.д.

    // Например:
    // openHouseDetailsPage(houseId);
    // или
    // showHouseModal(houseData);
}
