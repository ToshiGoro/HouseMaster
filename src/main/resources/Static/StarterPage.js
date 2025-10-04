//TODO: получать HTTP-запросом GET из приложения!
let houses = ["Сосна", "Ивушка", "Каштан", "Клён", "Дубок", "Берёзка", "Ракита", "Ольха", "Липа"];
let houseRange = houses.length; // Количество обслуживаемых домов


const main = document.getElementById("main");

window.onload = function () {
    setStartPage();
};

window.onresize = function () {
    setStartPage();
}

function setStartPage() {

    // Получаем ширину кнопки дома, чтобы рассчитать количество колонок на текущую ширину страницы.
    main.innerHTML = "<button id=\"tempBtn\" class=\"house-button\" style=\"visibility: hidden;\"></button>";
    let houseButton = document.getElementById("tempBtn");
    let houseButtonWidth = parseInt(getComputedStyle(houseButton).width);
    const buttonsInLine = Math.floor(main.offsetWidth / houseButtonWidth);

    houseButton = null;
    main.innerHTML = "";
    let pageContent = ""; // Переменная для динамического формирования HTML-наполнения страницы

    main.style.setProperty('--column-count', buttonsInLine);
    main.style.setProperty('--row-count', Math.floor(houseRange / buttonsInLine));

    pageContent += "<div class=\"house-div\">" +
        "<button id=\"newHouse\" class=\"house-button house-button--new\">" +
        CREATE + "</button></div>";

    pageContent += "<div class=\"house-div\">" +
        "<button id=\"all\" class=\"house-button house-button--all\" onclick=\"selectedHouses(this.id)\">" +
        ALL + "</button></div>";

    pageContent += "<div class=\"house-div\">" +
        "<button id=\"group\" class=\"house-button house-button--group\" onclick=\"selectedHouses(this.id)\">"
        + GROUP + "</button></div>";

    for (let i = 0; i < houseRange; i++) {
        pageContent += "<div class=\"house-div\">" +
            "<button id=\"btn" + i + "\" class=\"house-button\" onclick=\"houseBtnClicked(this.id)\">" + houses[i] + "</button>" +
            "<label class=\"check-sign\">" +
            "<img id=\"check" + i + "\" src=\"images/CheckSign.png\" width='40px' height='40px' style=\"visibility: hidden;\">" +
            "</label></div>";
    }

    main.innerHTML = pageContent;

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
        console.log(houses);
    }

    if (selection === "group") {
        let selHouses = [];
        for (let i = 0; i < houses.length; i++) {
            if (document.getElementById("check" + i).style.visibility === 'visible') {
                selHouses.push(houses[i]);
            }
        }
        console.log(selHouses);
    }

}
