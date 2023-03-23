import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import EventClient from "../api/eventClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class EventPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderEvent'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-id-form').addEventListener('submit', this.onGet);
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        this.client = new EventClient();

        this.dataStore.addChangeListener(this.renderEvent)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderEvent() {
        let resultArea = document.getElementById("result-info");

        const event = this.dataStore.get("event");

        if (event) {
            resultArea.innerHTML = `
                <div>ID: ${event.id}</div>
                <div>Name: ${event.customerEmail}</div>
            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("id-field").value;
        this.dataStore.set("event", null);

        let result = await this.client.getEvent(id, this.errorHandler);
        this.dataStore.set("event", result);
        if (result) {
            this.showMessage(`Got ${result.customerName}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("event", null);

        let name = document.getElementById("create-name-field").value;
        let date = document.getElementById("create-date-field").value;
        let email = document.getElementById("create-email-field").value;
        let status = document.getElementById("create-status-field").value;

        const createdEvent = await this.client.createEvent(name,email,date, status, this.errorHandler);
        this.dataStore.set("event", createdExample);

        if (createdEvent) {
            this.showMessage(`Created ${createdEvent.date}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const eventPage = new EventPage();
    eventPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
