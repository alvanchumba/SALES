const HOTDOG_PRICE = 18;
let salesData = JSON.parse(localStorage.getItem('salesData')) || [];

document.getElementById('sales-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const day = document.getElementById('day').value;
    const quantity = parseInt(document.getElementById('quantity').value);
    const amount = parseFloat(document.getElementById('amount').value);
    const totalSales = quantity * HOTDOG_PRICE;

    const salesEntry = { day, quantity, amount, totalSales };
    salesData.push(salesEntry);

    localStorage.setItem('salesData', JSON.stringify(salesData));
    displaySales();
    displayTotals();

    document.getElementById('sales-form').reset();
});

function displaySales() {
    const salesRecords = document.getElementById('sales-records');
    salesRecords.innerHTML = '';

    if (salesData.length === 0) {
        salesRecords.innerHTML = '<p>No sales data available.</p>';
        return;
    }

    salesData.forEach((entry, index) => {
        const div = document.createElement('div');
        div.classList.add('sales-entry');
        div.innerHTML = `
            <p><strong>Day:</strong> ${entry.day}</p>
            <p><strong>Quantity Sold:</strong> ${entry.quantity}</p>
            <p><strong>Amount Spent:</strong> $${entry.amount.toFixed(2)}</p>
            <p><strong>Total Sales:</strong> $${entry.totalSales.toFixed(2)}</p>
            <button onclick="deleteEntry(${index})">Delete</button>
        `;
        salesRecords.appendChild(div);
    });
}

function displayTotals() {
    let totalQuantity = 0;
    let totalAmount = 0;
    let totalSales = 0;

    salesData.forEach(entry => {
        totalQuantity += entry.quantity;
        totalAmount += entry.amount;
        totalSales += entry.totalSales;
    });

    document.getElementById('total-quantity').innerText = totalQuantity;
    document.getElementById('total-amount').innerText = totalAmount.toFixed(2);
    document.getElementById('total-sales').innerText = totalSales.toFixed(2);
}



function deleteEntry(index) {
    salesData.splice(index, 1);
    localStorage.setItem('salesData', JSON.stringify(salesData));
    displaySales();
    displayTotals();
}

document.addEventListener('DOMContentLoaded', () => {
    displaySales();
    displayTotals();
});
