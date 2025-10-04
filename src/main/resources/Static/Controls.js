document.addEventListener('keyup', function (event) {
    switch (event.code) {

        //Отжатие кнопки Escape снимает все метки выделения домов, обнуляя выборку.
        case 'Escape':
            for (let i = 0; i < houseRange; i++) {
                document.getElementById("check" + i).style.visibility = 'hidden';
            }
            break;

    }
});