class EmployeeForm {
    constructor(backendUrl, contactsManager) {
        this.BACKEND_URL = backendUrl;
        this.contactsManager = contactsManager;
        this.modal = null;
        this.currentPersonId = null;
        this.positionTypes = [];
        this.isEditMode = false;
        this.onSaveCallback = null;
        this.houseId = null;

        this.init();
    }

    init() {
        this.createModal();
        this.loadPositionTypes();
    }

    createModal() {
        this.modal = document.createElement('div');
        this.modal.className = 'employee-form-modal';
        this.modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title" id="employeeFormTitle">Добавление сотрудника</h2>
                    <span class="modal-close">&times;</span>
                </div>
                
                <div class="form-section">
                    <h3 class="section-title">Основная информация</h3>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Фамилия *</label>
                            <input type="text" id="lastName" class="form-input" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Имя *</label>
                            <input type="text" id="firstName" class="form-input" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Отчество</label>
                            <input type="text" id="secondName" class="form-input">
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Пол *</label>
                            <div class="radio-group">
                                <label class="radio-option">
                                    <input type="radio" name="gender" value="true" checked>
                                    Мужской
                                </label>
                                <label class="radio-option">
                                    <input type="radio" name="gender" value="false">
                                    Женский
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Дата рождения</label>
                            <input type="date" id="birthDate" class="form-input">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Должность *</label>
                        <select id="positionSelect" class="form-input" required>
                            <option value="">-- Выберите должность --</option>
                        </select>
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="section-title">Контакты</h3>
                    <div id="contactsPreview" class="contacts-preview">
                        <div class="no-contacts">Контакты не добавлены</div>
                    </div>
                    <button type="button" id="editContactsBtn" class="edit-contacts-btn">
                        Редактировать контакты
                    </button>
                </div>

                <div class="modal-buttons">
                    <button type="button" class="form-btn btn-secondary" id="cancelBtn">Отменить</button>
                    <button type="button" class="form-btn btn-primary" id="saveBtn">Сохранить</button>
                    <button type="button" class="form-btn btn-success" id="saveCloseBtn">Сохранить и закрыть</button>
                </div>
            </div>
        `;

        document.body.appendChild(this.modal);

        // Обработчики событий
        this.modal.querySelector('.modal-close').onclick = () => this.close();
        document.getElementById('cancelBtn').onclick = () => this.close();
        document.getElementById('saveBtn').onclick = () => this.save(false);
        document.getElementById('saveCloseBtn').onclick = () => this.save(true);
        document.getElementById('editContactsBtn').onclick = () => this.editContacts();

        this.modal.onclick = (e) => {
            if (e.target === this.modal) this.close();
        };
    }

    async loadPositionTypes() {
        try {
            const response = await fetch(`${this.BACKEND_URL}/position-types`);
            if (response.ok) {
                this.positionTypes = await response.json();
                this.updatePositionSelect();
            }
        } catch (error) {
            console.error('Ошибка загрузки типов должностей:', error);
        }
    }

    updatePositionSelect() {
        const select = document.getElementById('positionSelect');
        select.innerHTML = '<option value="">-- Выберите должность --</option>';

        this.positionTypes.forEach(positionType => {
            const option = document.createElement('option');
            option.value = positionType.id;
            option.textContent = positionType.positionType;
            select.appendChild(option);
        });
    }

    async showCreate(houseId, onSaveCallback) {
        this.isEditMode = false;
        this.currentPersonId = null;
        this.houseId = houseId;
        this.onSaveCallback = onSaveCallback;

        document.getElementById('employeeFormTitle').textContent = 'Добавление сотрудника';
        this.clearForm();
        this.updateContactsPreview([]);
        this.modal.style.display = 'block';
    }

    async showEdit(personId, houseId, onSaveCallback) {
        this.isEditMode = true;
        this.currentPersonId = personId;
        this.houseId = houseId;
        this.onSaveCallback = onSaveCallback;

        document.getElementById('employeeFormTitle').textContent = 'Редактирование сотрудника';

        try {
            const personResponse = await fetch(`${this.BACKEND_URL}/persons/get/${personId}`);
            if (personResponse.ok) {
                const person = await personResponse.json();
                this.fillForm(person);
                await this.loadCurrentPosition(personId, houseId);
            }
            await this.loadContactsPreview(personId);
        } catch (error) {
            console.error('Ошибка загрузки данных:', error);
            this.showAlert('Ошибка', 'Не удалось загрузить данные сотрудника');
        }

        this.modal.style.display = 'block';
    }

    async loadCurrentPosition(personId, houseId) {
        try {
            const response = await fetch(`${this.BACKEND_URL}/positions/person/${personId}`);
            if (response.ok) {
                const positions = await response.json();
                const currentPosition = positions.find(pos => pos.houseId === houseId);
                if (currentPosition) {
                    document.getElementById('positionSelect').value = currentPosition.positionType.id;
                }
            }
        } catch (error) {
            console.error('Ошибка загрузки должности:', error);
        }
    }

    fillForm(person) {
        document.getElementById('lastName').value = person.lastName || '';
        document.getElementById('firstName').value = person.firstName || '';
        document.getElementById('secondName').value = person.secondName || '';
        document.getElementById('birthDate').value = person.birthDate || '';

        const genderValue = person.gender !== undefined ? person.gender.toString() : 'true';
        document.querySelector(`input[name="gender"][value="${genderValue}"]`).checked = true;
    }

    clearForm() {
        document.getElementById('lastName').value = '';
        document.getElementById('firstName').value = '';
        document.getElementById('secondName').value = '';
        document.getElementById('birthDate').value = '';
        document.getElementById('positionSelect').value = '';
        document.querySelector('input[name="gender"][value="true"]').checked = true;
    }

    async loadContactsPreview(personId) {
        try {
            const response = await fetch(`${this.BACKEND_URL}/contacts/person/${personId}`);
            if (response.ok) {
                const contacts = await response.json();
                this.updateContactsPreview(contacts);
            }
        } catch (error) {
            console.error('Ошибка загрузки контактов:', error);
            this.updateContactsPreview([]);
        }
    }

    updateContactsPreview(contacts) {
        const container = document.getElementById('contactsPreview');

        if (contacts && contacts.length > 0) {
            container.innerHTML = `
                <div class="contacts-list">
                    ${contacts.map(contact => `
                        <div class="contact-preview-item">
                            <span class="contact-type">${contact.contactType.contactType}:</span>
                            <span class="contact-value">${contact.contact}</span>
                        </div>
                    `).join('')}
                </div>
            `;
        } else {
            container.innerHTML = '<div class="no-contacts">Контакты не добавлены</div>';
        }
    }

    editContacts() {
        if (!this.currentPersonId) {

            this.showAlert('Внимание', 'Для ввода контактов сначала сохраните сотрудника!');

            return;

        }

        const personName = this.getFullName();
        this.contactsManager.showContacts(this.currentPersonId, personName);
    }

    getFullName() {
        const lastName = document.getElementById('lastName').value.trim();
        const firstName = document.getElementById('firstName').value.trim();
        const secondName = document.getElementById('secondName').value.trim();

        return `${lastName} ${firstName} ${secondName}`.trim();

    }

    getFormData() {

        return {
            firstName: document.getElementById('firstName').value.trim(),
            secondName: document.getElementById('secondName').value.trim(),
            lastName: document.getElementById('lastName').value.trim(),
            gender: document.querySelector('input[name="gender"]:checked').value === 'true',
            birthDate: document.getElementById('birthDate').value || null,
            positionTypeId: document.getElementById('positionSelect').value
        };

    }

    validateForm() {
        const data = this.getFormData();

        if (!data.firstName) {
            this.showAlert('Ошибка', 'Заполните поле "Имя"');

            return false;

        }

        if (!data.lastName) {
            this.showAlert('Ошибка', 'Заполните поле "Фамилия"');

            return false;

        }

        if (!data.positionTypeId) {
            this.showAlert('Ошибка', 'Выберите должность');

            return false;

        }

        return true;

    }

    async save(closeAfterSave) {
        if (!this.validateForm()) return;

        const formData = this.getFormData();

        try {
            let personResponse;
            let savedPerson;

            if (this.isEditMode) {
                personResponse = await fetch(`${this.BACKEND_URL}/persons/update/${this.currentPersonId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(formData)
                });
            } else {
                personResponse = await fetch(`${this.BACKEND_URL}/persons/create`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(formData)
                });
            }

            if (personResponse.ok) {
                savedPerson = await personResponse.json();

                if (formData.positionTypeId) {
                    await this.savePosition(savedPerson.id, formData.positionTypeId);
                }

                this.showAlert('Успех', this.isEditMode ? 'Данные сотрудника обновлены' : 'Сотрудник успешно создан');

                if (this.onSaveCallback) {
                    this.onSaveCallback(savedPerson);
                }

                if (closeAfterSave) {
                    this.close();
                }
            } else {
                const errorText = await personResponse.text();
                this.showAlert('Ошибка', 'Не удалось сохранить данные: ' + errorText);
            }

        } catch (error) {
            console.error('Ошибка сохранения:', error);
            this.showAlert('Ошибка', 'Не удалось сохранить данные');
        }

    }

    async savePosition(personId, positionTypeId) {

        try {
            const positionsResponse = await fetch(`${this.BACKEND_URL}/positions/person/${personId}`);
            if (positionsResponse.ok) {
                const positions = await positionsResponse.json();
                const existingPosition = positions.find(pos => pos.houseId === this.houseId);

                if (existingPosition) {
                    await fetch(`${this.BACKEND_URL}/positions/${existingPosition.id}`, {
                        method: 'DELETE'
                    });
                }
            }

            const response = await fetch(`${this.BACKEND_URL}/positions`, {

                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    personId: personId,
                    positionTypeId: positionTypeId,
                    houseId: this.houseId
                })

            });

            return response.ok;

        } catch (error) {

            console.error('Ошибка сохранения должности:', error);

            return false;

        }

    }

    close() {

        this.modal.style.display = 'none';
        this.currentPersonId = null;
        this.isEditMode = false;
        this.onSaveCallback = null;
        this.houseId = null;

    }

    showAlert(title, message) {

        if (typeof showAlert === 'function') {
            showAlert(title, message);
        } else {
            alert(`${title}: ${message}`);
        }

    }

}

let employeeForm;