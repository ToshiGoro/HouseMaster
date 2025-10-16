class ParametersEditor {
    constructor() {
        this.form = document.getElementById('parametersForm');
        this.parametersList = document.getElementById('parametersList');
        this.saveButton = document.getElementById('saveButton');
        this.statusMessage = document.getElementById('statusMessage');

        this.init();
    }

    async init() {
        // Загружаем параметры при инициализации
        await this.loadParameters();

        // Навешиваем обработчик отправки формы
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
    }

    async loadParameters() {
        try {
            this.showLoading(true);

            // GET-запрос для получения параметров
            const response = await fetch('/api/parameters', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const parameters = await response.json();
            this.renderParameters(parameters);

        } catch (error) {
            this.showMessage(`Ошибка загрузки: ${error.message}`, 'error');
        } finally {
            this.showLoading(false);
        }
    }

    renderParameters(parameters) {
        this.parametersList.innerHTML = '';

        Object.entries(parameters).forEach(([key, value]) => {
            const row = document.createElement('div');
            row.className = 'parameter-row';

            row.innerHTML = `
                <div class="parameter-label">${this.formatParameterName(key)}</div>
                <div class="parameter-value">
                    <input 
                        type="text" 
                        class="parameter-input" 
                        name="${key}" 
                        value="${this.escapeHtml(value)}"
                        data-original-value="${this.escapeHtml(value)}"
                    >
                </div>
            `;

            this.parametersList.appendChild(row);
        });
    }

    async handleSubmit(event) {
        event.preventDefault();

        try {
            this.showLoading(true);

            // Собираем данные из формы
            const formData = this.collectFormData();

            // PUT-запрос для сохранения изменений
            const response = await fetch('/api/parameters', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const result = await response.json();
            this.showMessage('Изменения успешно сохранены!', 'success');

            // Обновляем оригинальные значения
            this.updateOriginalValues(formData);

        } catch (error) {
            this.showMessage(`Ошибка сохранения: ${error.message}`, 'error');
        } finally {
            this.showLoading(false);
        }
    }

    collectFormData() {
        const inputs = this.parametersList.querySelectorAll('.parameter-input');
        const data = {};

        inputs.forEach(input => {
            data[input.name] = input.value;
        });

        return data;
    }

    updateOriginalValues(data) {
        const inputs = this.parametersList.querySelectorAll('.parameter-input');

        inputs.forEach(input => {
            if (data[input.name] !== undefined) {
                input.setAttribute('data-original-value', data[input.name]);
            }
        });
    }

    formatParameterName(key) {
        // Преобразуем camelCase или snake_case в читаемое название
        return key
            .replace(/([A-Z])/g, ' $1')
            .replace(/_/g, ' ')
            .replace(/^./, str => str.toUpperCase())
            .trim();
    }

    escapeHtml(unsafe) {
        return unsafe
            .toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    showLoading(loading) {
        this.saveButton.disabled = loading;
        this.saveButton.textContent = loading ? 'Сохранение...' : 'Сохранить изменения';
    }

    showMessage(message, type) {
        this.statusMessage.textContent = message;
        this.statusMessage.className = `status-message ${type}`;
        this.statusMessage.style.display = 'block';

        // Автоматически скрываем сообщение через 5 секунд
        setTimeout(() => {
            this.statusMessage.style.display = 'none';
        }, 5000);
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    new ParametersEditor();
});