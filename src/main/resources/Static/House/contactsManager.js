class ContactsManager {
    constructor(backendUrl) {
        this.BACKEND_URL = backendUrl;
        this.modal = null;
        this.currentPersonId = null;
        this.contactTypes = [];

        this.init();
    }

    init() {
        this.createModal();
        this.loadContactTypes();
    }

    createModal() {
        this.modal = document.createElement('div');
        this.modal.className = 'contacts-manager-modal';
        this.modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h2 class="modal-title">Контакты</h2>
                    <span class="modal-close">&times;</span>
                </div>
                
                <div class="person-info">
                    <div class="info-row">
                        <span class="info-label">ФИО:</span>
                        <span class="info-value" id="contactsPersonName">--</span>
                    </div>
                </div>

                <div class="contacts-section">
                    <h3 style="margin-bottom: 15px; color: rgba(80, 80, 255, 90%);">Список контактов</h3>
                    <div id="contactsList" class="contacts-list">
                        <!-- Контакты будут здесь -->
                    </div>
                    <div id="noContactsMessage" style="text-align: center; padding: 20px; color: #6c757d; font-style: italic;">
                        Контакты не добавлены
                    </div>
                </div>

                <div class="add-contact-section">
                    <h4 style="margin-bottom: 10px; color: #28a745;">Добавить контакт</h4>
                    <div style="display: flex; gap: 10px; align-items: end;">
                        <div style="flex: 1;">
                            <label style="display: block; margin-bottom: 5px; font-weight: 600; font-size: 0.9em;">Тип контакта</label>
                            <select id="contactTypeSelect" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                                <option value="">-- Выберите тип --</option>
                            </select>
                        </div>
                        <div style="flex: 2;">
                            <label style="display: block; margin-bottom: 5px; font-weight: 600; font-size: 0.9em;">Контакт</label>
                            <input type="text" id="contactInput" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;" 
                                   placeholder="Введите контакт">
                        </div>
                        <button id="addContactBtn" class="action-btn add-btn" title="Добавить контакт">
                            +
                        </button>
                    </div>
                </div>

                <div class="modal-buttons">
                    <button class="modal-btn btn-secondary">Закрыть</button>
                </div>
            </div>
        `;

        document.body.appendChild(this.modal);

        // Обработчики событий
        this.modal.querySelector('.modal-close').onclick = () => this.closeModal();
        this.modal.querySelector('.btn-secondary').onclick = () => this.closeModal();
        document.getElementById('addContactBtn').onclick = () => this.addContact();

        this.modal.onclick = (e) => {
            if (e.target === this.modal) this.closeModal();
        };
    }

    async loadContactTypes() {
        try {
            const response = await fetch(`${this.BACKEND_URL}/contact-types`);
            if (response.ok) {
                this.contactTypes = await response.json();
                this.updateContactTypeSelect();
            }
        } catch (error) {
            console.error('Ошибка загрузки типов контактов:', error);
        }
    }

    updateContactTypeSelect() {
        const select = document.getElementById('contactTypeSelect');
        select.innerHTML = '<option value="">-- Выберите тип --</option>';

        this.contactTypes.forEach(type => {
            const option = document.createElement('option');
            option.value = type.id;
            option.textContent = type.contactType;
            select.appendChild(option);
        });
    }

    async showContacts(personId, personName) {
        this.currentPersonId = personId;

        const nameElement = document.getElementById('contactsPersonName');
        if (nameElement) {
            nameElement.textContent = personName;
        }

        await this.loadContactTypes();
        await this.loadContacts();

        this.modal.style.display = 'block';
    }

    closeModal() {
        this.modal.style.display = 'none';
        this.currentPersonId = null;
        this.clearForm();
    }

    clearForm() {
        document.getElementById('contactTypeSelect').value = '';
        document.getElementById('contactInput').value = '';
    }

    async loadContacts() {
        if (!this.currentPersonId) return;

        try {
            const response = await fetch(`${this.BACKEND_URL}/contacts/person/${this.currentPersonId}`);
            if (response.ok) {
                const contacts = await response.json();
                this.renderContacts(contacts);
            }
        } catch (error) {
            console.error('Ошибка загрузки контактов:', error);
            this.showAlert('Ошибка', 'Не удалось загрузить контакты');
        }
    }

    renderContacts(contacts) {
        const container = document.getElementById('contactsList');
        const noContactsMsg = document.getElementById('noContactsMessage');

        container.innerHTML = '';

        if (contacts && contacts.length > 0) {
            noContactsMsg.style.display = 'none';

            contacts.forEach(contact => {
                const contactElement = this.createContactElement(contact);
                container.appendChild(contactElement);
            });
        } else {
            noContactsMsg.style.display = 'block';
        }
    }

    createContactElement(contact) {
        const element = document.createElement('div');
        element.className = 'contact-item';

        // Исправлено: правильно обращаемся к contactType.contactType
        const typeDiv = document.createElement('div');
        typeDiv.style.cssText = 'flex: 1; font-weight: 600; color: #495057;';
        typeDiv.textContent = contact.contactType.contactType + ':';

        const valueDiv = document.createElement('div');
        valueDiv.style.cssText = 'flex: 2; color: #212529;';
        valueDiv.textContent = contact.contact;

        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'action-btn delete-btn';
        deleteBtn.title = 'Удалить контакт';
        deleteBtn.textContent = '×';
        deleteBtn.onclick = () => this.deleteContact(contact.id);

        // ИСПРАВЛЕНИЕ: убрано дублирование добавления кнопки
        element.appendChild(typeDiv);
        element.appendChild(valueDiv);
        element.appendChild(deleteBtn);

        return element;
    }

    async addContact() {
        const typeSelect = document.getElementById('contactTypeSelect');
        const contactInput = document.getElementById('contactInput');

        const typeId = typeSelect.value;
        const contactValue = contactInput.value.trim();

        if (!typeId || !contactValue) {
            this.showAlert('Внимание', 'Заполните все поля');
            return;
        }

        try {
            const response = await fetch(`${this.BACKEND_URL}/contacts/person/${this.currentPersonId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    contactTypeId: typeId,
                    contact: contactValue
                })
            });

            if (response.ok) {
                this.clearForm();
                await this.loadContacts();
            } else {
                const errorText = await response.text();
                this.showAlert('Ошибка', 'Не удалось добавить контакт: ' + errorText);
            }
        } catch (error) {
            console.error('Ошибка добавления контакта:', error);
            this.showAlert('Ошибка', 'Не удалось добавить контакт');
        }
    }

    async deleteContact(contactId) {
        const confirmed = await this.showConfirm(
            'Удаление контакта',
            'Вы уверены, что хотите удалить этот контакт?'
        );

        if (!confirmed) return;

        try {
            const response = await fetch(`${this.BACKEND_URL}/contacts/${contactId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                await this.loadContacts();
            } else {
                this.showAlert('Ошибка', 'Не удалось удалить контакт');
            }
        } catch (error) {
            console.error('Ошибка удаления контакта:', error);
            this.showAlert('Ошибка', 'Не удалось удалить контакт');
        }
    }

    showAlert(title, message) {
        if (typeof showAlert === 'function') {
            showAlert(title, message);
        } else {
            alert(`${title}: ${message}`);
        }
    }

    showConfirm(title, message) {
        if (typeof showConfirm === 'function') {
            return showConfirm(title, message);
        } else {
            return Promise.resolve(confirm(`${title}: ${message}`));
        }
    }
}

let contactsManager;