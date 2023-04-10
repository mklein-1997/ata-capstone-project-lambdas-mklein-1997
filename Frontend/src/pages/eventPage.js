import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import EventClient from "../api/eventClient";

/**
 * Logic needed for the view playlist page of the website.
 */

class EventPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderEvent', 'onDelete'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-id-form').addEventListener('submit', this.onGet);
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        document.getElementById('delete-form').addEventListener('submit', this.onDelete);
        this.client = new EventClient();

        //this.dataStore.addChangeListener(this.renderEvent)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderEvent(mess) {
        let resultArea = document.getElementById("result-info");
                const event = this.dataStore.get("event");

                if (event) {
                  resultArea.innerHTML = `
                      <div class="results">
                      <h2>${mess}</h2>
                        <p>Id: ${event.eventId}</p>
                        <p>Date: ${event.date}</p>
                        <p>Name: ${event.customerName}</p>
                        <p>Email: ${event.customerEmail}</p>
                        <p>Status: ${event.status}</p>
                      </div>
                  `
                } else {
                    resultArea.innerHTML = "No Item";
                }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(e) {
        e.preventDefault();

        let id = document.getElementById("id-field").value;
        this.dataStore.set("event", null);

        let result = await this.client.getEvent(id, this.errorHandler);
        this.dataStore.set("event", result);
        if (result) {
            this.showMessage(`Got ${result.customerName}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
        this.renderEvent("Event Retrieved");
    }

    async onDelete(e) {
            e.preventDefault();

            let id = document.getElementById("delete-id-field").value;
            this.dataStore.set("event", null);

            let result = await this.client.deleteEvent(id, this.errorHandler);
            if (result) {
                this.showMessage(`Deleted`)
                document.getElementById("delete-result").innerHTML = id + " Deleted!";
            } else {
                this.errorHandler("Error doing Delete!  Try again...");
            }
        }

    async onCreate(e) {
        e.preventDefault();
        this.dataStore.set("event", null);

        let name = document.getElementById("create-name-field").value;
        let date = document.getElementById("create-date-field").value;
        let email = document.getElementById("create-email-field").value;
        let status = document.getElementById("create-status-field").value;

        const createdEvent = await this.client.createEvent(date, status, name, email, this.errorHandler);
        this.dataStore.set("event", createdEvent);

        if (createdEvent) {
            this.showMessage(`Created ${createdEvent.date}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
        this.renderEvent("Event Created");
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
