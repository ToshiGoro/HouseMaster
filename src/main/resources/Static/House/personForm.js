class PersonForm {

    constructor(backendUrl, contactsManager) {

        this.BACKEND_URL = backendUrl;
        this.contactsManager = contactsManager;
        this.modal = null;
        this.currentPersonId = null;
        this.isEditMode = false;
        this.onSaveCallback = null;

        this.init();

    }

    init() {
        this.createModal();
    }

    createModal() {
        this.modal = document.createElement('div');
        this.modal.className = 'person-form-modal';
        this.modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title" id="personFormTitle">Добавление жителя</h2>
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

    showCreate(onSaveCallback) {

        this.isEditMode = false;
        this.currentPersonId = null;
        this.onSaveCallback = onSaveCallback;

        document.getElementById('personFormTitle').textContent = 'Добавление жителя';
        this.clearForm();
        this.updateContactsPreview([]);
        this.modal.style.display = 'block';

    }

    async showEdit(personId, onSaveCallback) {

        this.isEditMode = true;
        this.currentPersonId = personId;
        this.onSaveCallback = onSaveCallback;

        document.getElementById('personFormTitle').textContent = 'Редактирование жителя';

        try {
            const response = await fetch(`${this.BACKEND_URL}/persons/get/${personId}`);
            if (response.ok) {
                const person = await response.json();
                this.fillForm(person);
                await this.loadContactsPreview(personId);
            } else {
                this.showAlert('Ошибка', 'Не удалось загрузить данные жителя');
            }
        } catch (error) {
            console.error('Ошибка загрузки данных:', error);
            this.showAlert('Ошибка', 'Не удалось загрузить данные жителя');
        }

        this.modal.style.display = 'block';

    }

    fillForm(person) {

        document.getElementById('lastName').value = person.lastName || '';
        document.getElementById('firstName').value = person.firstName || '';
        document.getElementById('secondName').value = person.secondName || '';
        document.getElementById('birthDate').value = person.birthDate || '';

        // Устанавливаем пол
        const genderValue = person.gender !== undefined ? person.gender.toString() : 'true';
        document.querySelector(`input[name="gender"][value="${genderValue}"]`).checked = true;

    }

    clearForm() {

        document.getElementById('lastName').value = '';
        document.getElementById('firstName').value = '';
        document.getElementById('secondName').value = '';
        document.getElementById('birthDate').value = '';
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

            this.showAlert('Внимание', 'Для ввода контактов сначала сохраните запись!');

            return;

        }

        const personName = this.getFullName();
        this.contactsManager.showContacts(this.currentPersonId, personName);

        this.close();

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
            birthDate: document.getElementById('birthDate').value || null
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

        return true;

    }

    async save(closeAfterSave) {

        if (!this.validateForm()) return;

        const formData = this.getFormData();

        try {

            let response;

            if (this.isEditMode) {
                // Редактирование существующего жителя
                response = await fetch(`${this.BACKEND_URL}/persons/update/${this.currentPersonId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                });
            } else {
                // Создание нового жителя
                response = await fetch(`${this.BACKEND_URL}/persons/create`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                });
            }

            if (response.ok) {
                const savedPerson = await response.json();
                this.showAlert('Успех',
                    this.isEditMode ? 'Данные жителя обновлены' : 'Житель успешно создан');

                if (this.onSaveCallback) {
                    this.onSaveCallback(savedPerson);
                }

                if (closeAfterSave) {
                    this.close();
                }
            } else {
                const errorText = await response.text();
                this.showAlert('Ошибка', 'Не удалось сохранить данные: ' + errorText);
            }

        } catch (error) {
            console.error('Ошибка сохранения:', error);
            this.showAlert('Ошибка', 'Не удалось сохранить данные');
        }

    }

    close() {

        this.modal.style.display = 'none';
        this.currentPersonId = null;
        this.isEditMode = false;
        this.onSaveCallback = null;

    }

    showAlert(title, message) {

        if (typeof showAlert === 'function') {
            showAlert(title, message);
        } else {
            alert(`${title}: ${message}`);
        }

    }

}

let personForm;
