
const urlParams = new URLSearchParams(window.location.search);
const houseId = urlParams.get('houseId');

// Заполняем форму после загрузки DOM
document.addEventListener('DOMContentLoaded', function() {
    getHouse().then(
        housesArray => {
            houses = housesArray;
            console.log(houses);
            document.getElementById('address').value = houses.address;
            document.getElementById('hoaName').value = houses.hoaId;
            document.getElementById('numOfFloors').value = houses.numOfFloors;
        });
});

// Обработчик кнопки "Сохранить"
document.querySelector('.save-btn').addEventListener('click', function(e) {

    e.preventDefault(); // ← ВАЖНО: предотвращаем отправку формы

    if (validateForm()) {
        goBackWithHouseId();
    }

});

// Обработчик кнопки "Отменить"
document.querySelector('.cancel-btn').addEventListener('click', function() {

    goBackWithHouseId() ;

});

function goBackWithHouseId() {
    window.location.href = `${document.referrer}?houseId=${houseId}`;
}

function validateForm() {
    const address = document.getElementById('address').value.trim();
    const hoaName = document.getElementById('hoaName').value.trim();
    const numOfFloors = parseInt(document.getElementById('numOfFloors').value);

    // Сложные проверки
    if (!address) {
        alert('Адрес обязателен для заполнения');
        return false;
    }

    if (address.length < 5) {
        alert('Адрес слишком короткий');
        return false;
    }

    if (address.length > 200) {
        alert('Адрес должен включать не более 200 символов');
        return false;
    }

    if (hoaName.length < 2) {
        alert('Наименование ТСЖ должно быть не менее одного символа');
        return false;
    }

    if (hoaName.length > 200) {
        alert('Наименование ТСЖ должно быть не более 50 символов');
        return false;
    }

    if (numOfFloors < 1 || numOfFloors > 100 || isNaN(numOfFloors)) {
        alert('Этажность должна быть числом от 1 до 100');
        return false;
    }

    return true;

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
