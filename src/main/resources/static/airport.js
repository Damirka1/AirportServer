let stepper;

document.addEventListener('DOMContentLoaded', function () {
    stepper = new Stepper(document.querySelector('.bs-stepper'))

    findTickets();
})

function createTable() {
    const table = document.createElement('table');
    table.id = 'search-table';
    table.classList.add('table', 'table-light', 'table-hover', 'table-borderless', 'rounded');
    table.style = 'text-align: center';

    table.innerHTML = `
    <thead class="thead-dark">
        <tr>
            <th scope="col">№</th>
            <th scope="col">Дата вылета</th>
            <th scope="col">Модель самолета</th>
            <th scope="col">Город отправления</th>
            <th scope="col">Город прибытия</th>
            <th scope="col">Стоимость</th>
            <th scope="col">Скидка</th>
            <th scope="col">Итого</th>
        </tr>
    </thead>
    <tbody id="flight-table-body">
    </tbody>
    `;

    const searchContent = document.getElementById('search-content');
    searchContent.appendChild(table);
}

function calculatePrice(flightData) {
    let price = 19999;

    if(flightData.end.name === 'Москва')
        price = 29999
    else if(flightData.end.name === 'Мальдивы')
        price = 54999

    var dateNow = new Date();
    var dateFlight = new Date(flightData.time);

    var timeDiff = Math.abs(dateFlight.getTime() - dateNow.getTime());

    var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

    //console.log(diffDays);

    // if diff is more than 2 weeks -> 20% free
    // if diff more than 1 week -> 10% free


    const diffWeeks = Math.floor(diffDays / 7);
    let discount = 0;

    if (diffWeeks >= 2) {
        discount = 20;
    } else if (diffWeeks >= 1) {
        discount = 10;
    }

    return {
        'price': price.toLocaleString('ru-RU') + ' тг',
        'discount': discount + "%",
        'summary': (price - (price * (discount / 100.0)).toFixed(0)).toLocaleString('ru-RU') + ' тг'
    };
}

function onClickBack() {
    stepper.previous();
}



function onClickOnRow(index, flightData, priceData) {

    var date = document.getElementById('flight-date');
    var time = document.getElementById('flight-time');
    var plane = document.getElementById('flight-plane');
    var start = document.getElementById('flight-start');
    var end = document.getElementById('flight-end');
    var status = document.getElementById('flight-status');

    date.innerHTML = `
        <strong>Дата вылета:</strong>
        ${new Date(flightData.time)}
    `;

    time.innerHTML = `
        <strong>Время вылета:</strong>
        ${new Date(flightData.time).toLocaleTimeString('ru-RU')}
    `;

    plane.innerHTML = `
        <strong>Модель самолета:</strong>
        ${flightData.plane.model}
    `;

    start.innerHTML = `
        <strong>Начальный пункт:</strong>
        ${flightData.start.name}
    `;

    end.innerHTML = `
        <strong>Конечный пункт:</strong>
        ${flightData.end.name}
    `;

    status.innerHTML = `
        <strong>Статус рейса:</strong>
        Покупка билетов
    `;

    stepper.next()
}

function addFlightCard(index, flightData) {
    const row = document.createElement('tr');
    row.scope = 'row';
    row.classList.add('align-middle');

    const priceData = calculatePrice(flightData)

    row.addEventListener('click', function(event) {
        // console.log(index);
        // console.log(flightData);
        // console.log(priceData);

        onClickOnRow(index, flightData, priceData);
    })

    const cells = [
        index,
        flightData.time,
        flightData.plane.model,
        flightData.start.name,
        flightData.end.name,
        priceData.price,
        priceData.discount,
        priceData.summary
    ];

    cells.forEach(function(cellData) {
        var cell = document.createElement('td');
        cell.textContent = cellData;
        row.appendChild(cell);
    });

    var tableBody = document.getElementById('flight-table-body');
    tableBody.appendChild(row);
}

function clearSearchContent() {
    var searchContent = document.getElementById('search-content');
    var childNodes = searchContent.childNodes;

    for (var i = 0; i < childNodes.length; i++) {
        var child = childNodes[i];
        if (child.classList && child.classList.contains('table')) {
            searchContent.removeChild(child);
        }
    }
}

function findTickets() {
    const vl = document.getElementById("scheduleId").value.trim().split(" - ");
    const source = vl[0];
    const destination = vl[1];
    const dateStart = document.getElementById("startDate").value;
    const dateEnd = document.getElementById("endDate").value;

    var api = '/api/airport/' + source + '/schedule';

    if(destination !== 'Все') {
        api += '/' + destination;
    }

    clearSearchContent();

    // console.log(source)
    // console.log(destination)
    // console.log(dateStart)
    // console.log(dateEnd)

    const progressSpinner = document.getElementById("progress-spinner");

    progressSpinner.hidden = false

    fetch(api)
        .then(function(response) {
            progressSpinner.hidden = true
            if (!response.ok) {

                throw new Error('Request failed:', response.status);
            }
            return response.json();
        })
        .then(function(data) {
            //console.log(data[0]);

            createTable();

            for (let i = 0; i < data.length; i++) {
                addFlightCard(i+1, data[i]);
            }
        })
        .catch(function(error) {
            console.error(error);
        });
}

let passengerCount = 1;

function deletePassenger() {
    const passengers = document.getElementById('passengerData');
    const childNodes = passengers.childNodes;

    for (var i = 0; i < childNodes.length; i++) {
        var child = childNodes[i];
        if (child.id === 'passenger' + passengerCount ) {
            passengers.removeChild(child);
        }
    }

    passengerCount -= 1;

    if(passengerCount === 1) {
        const button = document.getElementById('removePassengerButton')
        button.hidden = true;
    }

}

function addPassenger() {
    passengerCount += 1;

    const passengers = document.getElementById('passengerData');

    const passenger = document.createElement('div')
    passenger.classList.add('mt-5')
    passenger.id = 'passenger' + passengerCount;

    passenger.innerHTML = `
    <h5 class="mb-2 mx-4" >Данные пассажира №${passengerCount}</h5>
    <div class="row mx-4">
        <div class="col-md-6">
            <label for="fullName" class="form-label">ФИО</label>
            <input type="text" class="form-control" id="fullName" placeholder="Введите ФИО">

            <label for="passportNumber" class="form-label">Номер паспорта</label>
            <input type="text" class="form-control" id="passportNumber" placeholder="Введите номер паспорта">
        </div>
        
        <div class="col-md-6">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" placeholder="Введите email">

            <label for="phoneNumber" class="form-label">Номер телефона</label>
            <input type="tel" class="form-control" id="phoneNumber" placeholder="Введите номер телефона">
        </div>
    </div>
    `;
    passengers.appendChild(passenger);

    if(passengerCount >= 1) {
        const button = document.getElementById('removePassengerButton')
        button.hidden = false;
    }
}
