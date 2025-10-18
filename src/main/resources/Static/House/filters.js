class TextFilter {
    constructor(columnIndex, placeholder = 'Поиск...') {
        this.columnIndex = columnIndex;
        this.placeholder = placeholder;
        this.currentValue = '';
    }

    createInput() {
        const input = document.createElement('input');
        input.type = 'text';
        input.placeholder = this.placeholder;
        input.style.cssText = `
        width: 100%;
        padding: 4px;
        border: 1px solid #ddd;
        border-radius: 3px;
        font-size: 0.8em;
    `;

        input.addEventListener('keyup', (e) => {
            if (e.key === 'Enter') {
                this.currentValue = e.target.value.trim().toLowerCase();
                console.log('Enter pressed, value:', this.currentValue);
                console.log('onFilterChange exists:', !!this.onFilterChange);
                this.onFilterChange && this.onFilterChange();
            }
        });

        return input;
    }

    filter(row) {
        if (!this.currentValue) return true;

        const cell = row.cells[this.columnIndex];
        const text = cell.textContent.toLowerCase();
        return text.includes(this.currentValue);
    }

    setOnFilterChange(callback) {
        this.onFilterChange = callback;
    }

}

class CheckboxFilter {

    constructor(columnIndex, options) {
        this.columnIndex = columnIndex;
        this.options = options; // { value: label }
        this.selectedValues = new Set(Object.keys(this.options));

    }

    createInputs() {

        const container = document.createElement('div');
        container.style.cssText = `
            display: flex;
            flex-direction: column;
            gap: 3px;
            font-size: 0.8em;
        `;

        Object.entries(this.options).forEach(([value, label]) => {

            const labelElement = document.createElement('label');
            labelElement.style.cssText = `
                display: flex;
                align-items: center;
                gap: 4px;
                cursor: pointer;
            `;

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = value;
            checkbox.checked = true;
            checkbox.style.margin = '0';

            checkbox.addEventListener('change', (e) => {

                if (e.target.checked) {
                    this.selectedValues.add(value);
                } else {
                    this.selectedValues.delete(value);
                }
                this.onFilterChange && this.onFilterChange();

            });

            labelElement.appendChild(checkbox);
            labelElement.appendChild(document.createTextNode(label));
            container.appendChild(labelElement);

        });

        return container;

    }

    filter(row) {

        if (this.selectedValues.size === 0) return true;

        const cell = row.cells[this.columnIndex];
        const cellValue = this.extractValue(cell);
        return this.selectedValues.has(cellValue);

    }

    extractValue(cell) {

        const text = cell.textContent.trim();
        if (text === 'Мужской') return 'male';
        if (text === 'Женский') return 'female';

        return text;

    }

    setOnFilterChange(callback) {
        this.onFilterChange = callback;
    }

}

class RangeFilter {

    constructor(columnIndex, min = 0, max = 150) {

        this.columnIndex = columnIndex;
        this.min = min;
        this.max = max;
        this.currentMin = min;
        this.currentMax = max;

    }

    createInputs() {

        const container = document.createElement('div');
        container.style.cssText = `
            display: flex;
            flex-direction: column;
            gap: 3px;
            font-size: 0.8em;
        `;

        const fromInput = this.createNumberInput('от', this.min, (value) => {
            this.currentMin = value;
            this.onFilterChange && this.onFilterChange();
        });

        const toInput = this.createNumberInput('до', this.max, (value) => {
            this.currentMax = value;
            this.onFilterChange && this.onFilterChange();
        });

        container.appendChild(fromInput);
        container.appendChild(toInput);

        return container;

    }

    createNumberInput(placeholder, defaultValue, onChange) {

        const container = document.createElement('div');
        container.style.display = 'flex';
        container.style.alignItems = 'center';
        container.style.gap = '2px';

        const span = document.createElement('span');
        span.textContent = placeholder;
        span.style.fontSize = '0.7em';

        const input = document.createElement('input');
        input.type = 'number';
        input.placeholder = defaultValue;
        input.min = this.min;
        input.max = this.max;
        input.style.cssText = `
            width: 40px;
            padding: 2px;
            border: 1px solid #ddd;
            border-radius: 2px;
            font-size: 0.7em;
        `;

        input.addEventListener('change', (e) => {

            let value = parseInt(e.target.value);
            if (isNaN(value)) value = placeholder === 'от' ? this.min : this.max;
            value = Math.max(this.min, Math.min(this.max, value));
            e.target.value = value;
            onChange(value);

        });

        container.appendChild(span);
        container.appendChild(input);

        return container;

    }

    filter(row) {

        const cell = row.cells[this.columnIndex];
        const age = this.extractAge(cell);

        if (age === -1) {
            return true;
        }

        return age >= this.currentMin && age <= this.currentMax;

    }

    extractAge(cell) {
        const text = cell.textContent.trim();

        // Если возраст неизвестен - пропускаем фильтрацию по возрасту
        if (text === 'Неизвестно') {
            return -1;
        }

        const ageMatch = text.match(/(\d+)/);
        return ageMatch ? parseInt(ageMatch[1]) : -1;
    }

    setOnFilterChange(callback) {
        this.onFilterChange = callback;
    }

}

// Менеджер фильтров для удобства
class FilterManager {
    constructor() {
        this.filters = [];
        this.onFilterChangeCallbacks = [];
    }

    addFilter(filter) {
        this.filters.push(filter);
        filter.setOnFilterChange(() => this.onFilterChange()); // ← ИЗМЕНИТЬ ЗДЕСЬ
        return filter;
    }

    applyFilters(rows) {
        return rows.filter(row => {
            return this.filters.every(filter => filter.filter(row));
        });
    }

    // НОВЫЙ МЕТОД ДЛЯ ВЫЗОВА КОЛБЭКОВ
    onFilterChange() {
        this.onFilterChangeCallbacks.forEach(callback => callback());
    }

    setOnFilterChange(callback) {
        this.onFilterChangeCallbacks.push(callback);
    }
}
