
const urlParams = new URLSearchParams(window.location.search);
let houseId = urlParams.get('houseId');

// Заполняем форму после загрузки DOM
document.addEventListener('DOMContentLoaded', function() {

    if(houseId != null) { // Если houseId получен и мы редактируем существующий дом

        getHouse().then(
            houseReceivedData => {

                house = houseReceivedData;

                document.getElementById('address').value = house.address;
                document.getElementById('livingArea').value = house.livingArea;
                document.getElementById('numOfFloors').value = house.numOfFloors;
                document.getElementById('numOfSections').value = house.numOfSections;
                document.getElementById('numOfEntrances').value = house.numOfEntrances;
                document.getElementById('numOfFlats').value = house.numOfFlats;
                document.getElementById('numOfOffices').value = house.numOfOffices;

            });

    }

    // Если дом новый, то его поля предъявляются пустыми, кроме выпадающего списка ТСЖ.
    getHoaForHouseDisplay().then(
        hoaArray => {

            hoas = [{id: "none", name: "Без ТСЖ"}].concat(hoaArray);

            hoaSelect = document.getElementById('hoaName');
            hoas.forEach(e => {
                const option = document.createElement('option');
                option.value = e.id;
                option.textContent = e.name;
                hoaSelect.appendChild(option);
            });

            if(houseId != null) {
                hoaSelect.value = house.hoaId; // Если дом существует, берём значение списка ТСЖ из поля дома hoaId,
            } else {
                hoaSelect.value = "none";        // а если нет - устанавливаем значение null - "Без ТСЖ".
            }

        });

});

function handleSaveButtons(e, saveAndClose) {
    e.preventDefault(); // предотвращаем отправку формы, чтобы был возможен возврат на вызывавшую страницу

    if (validateForm()) {

        let house = {
        address: document.getElementById('address').value,
        hoaId: hoaSelect.value === "none" ? null : hoaSelect.value,
        livingArea: document.getElementById('livingArea').value,
        numOfFloors: document.getElementById('numOfFloors').value,
        numOfSections: document.getElementById('numOfSections').value,
        numOfEntrances: document.getElementById('numOfEntrances').value,
        numOfFlats: document.getElementById('numOfFlats').value,
        numOfOffices: document.getElementById('numOfOffices').value};
        console.log(house);

        if (houseId != null) {
            updateHouse(house).then(r => {
                if (saveAndClose) window.close();
            });
        } else {
            createHouse(house).then(r => {
                houseId = r.id;
                if (saveAndClose) window.close();
                console.log(999999);
                // if (saveAndClose) history.back();
            });
        }

    }

}

// Обработчик кнопки "Сохранить"
document.querySelector('.save-btn').addEventListener('click', function(e) {
    handleSaveButtons(e, false);
});

// Обработчик кнопки "Сохранить и закрыть"
document.querySelector('.save-close-btn').addEventListener('click', function(e) {
    handleSaveButtons(e, true);
});

// Обработчик кнопки "Отменить"
document.querySelector('.cancel-btn').addEventListener('click', function() {
    console.log(88888);
    window.close();
});

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
        return await response.json();

    } catch (error) {
        console.error('Ошибка при получении данных дома:', error);
        return null;
    }

}

// Получение упрощённого объекта Hoa для представления в выпадающем списке
async function getHoaForHouseDisplay() {

    try {
        const response = await fetch(`${BACKEND_URL}/hoas/getAllHoaForHouseDisplay`);

        if (!response.ok) {
            throw new Error(`Ошибка получения данных ТСЖ. Статус: ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error('Ошибка при получении данных ТСЖ:', error);
        return null;
    }

}

async function updateHouse(houseOutgoingData) {
    try {
        const response = await fetch(`${BACKEND_URL}/houses/update/${houseId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(houseOutgoingData)
        });

        if (!response.ok) {
            throw new Error(`Ошибка передачи на сервер! Статус: ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error('Ошибка при обновлении дома:', error);
        throw error;
    }

}

async function createHouse(houseOutgoingData) {
    try {
        const response = await fetch(`${BACKEND_URL}/houses/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(houseOutgoingData)
        });

        if (!response.ok) {
            throw new Error(`Ошибка передачи на сервер! Статус: ${response.status}`);
        }

        return await response.json();

    } catch (error) {
        console.error('Ошибка при обновлении дома:', error);
        throw error;
    }

}