
let currentPage = 0;
const pageSize = 3;

const token = localStorage.getItem("token");

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

$(document).ready(function () {
    loadOrders();
});
function loadOrders() {
    $.ajax({
        url: `http://localhost:8080/api/v1/payment/paginated?page=${currentPage}&size=${pageSize}`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const payments = res.data || [];
            let rows = "";
            payments.forEach(payment => {
                rows +=
                    `<tr>
                        <td>Rs. ${payment.orderAmount}/=</td>
                        <td>${payment.cardHolderName}</td>
                        <td>${payment.email}</td>
                        <td>${payment.expireDate}</td>
                        <td>${payment.cvv}</td>
                        <td>
                            <button class="btn btn-sm btn-primary sendMailBtn" style="margin-bottom: 10px">Send Mail</button>
                        </td>
                    </tr>`;
            });
            $('#paymentTableBody').html(rows);

            // Bind event after rows load
            $('.sendMailBtn').on('click', function () {
                const row = $(this).closest("tr").children("td");

                const orderAmount = row.eq(0).text();
                const cardHolder = row.eq(1).text();
                const email = row.eq(2).text();
                const cardNumber = row.eq(3).text();
                const expireDate = row.eq(4).text();
                const cvv = row.eq(5).text();

                // Set values in mail form
                $('#to').val(email);
                $('#subject').val("Payment Confirmation");
                $('#message').val(
                    `Hello, ${cardHolder},\n\n` +
                    `Your payment has been received successfully:\n` +
                    `---------------------------------\n` +
                    `Amount: Rs.${orderAmount}\n` +
                    `Card Number: ${cardNumber}\n` +
                    `Expiry: ${expireDate}\n` +
                    `CVV: ${cvv}\n` +
                    `---------------------------------\n\n` +
                    `Thank you for your payment.\nGolden Spoon Restaurant`
                );

                // Smooth scroll to mail form
                $('html, body').animate({
                    scrollTop: $("#mailForm").offset().top
                }, 500);
            });

            loadPagination();
        },
        error: function (err) {
            console.log(err);
            alert("Failed to load Payments");
        }
    });
}


function loadPagination() {
    $.ajax({
        url: `http://localhost:8080/api/v1/payment/total-pages?size=${pageSize}`,
        method: "GET",
        success: function (totalPages) {
            let paginationHTML = "";
            for (let i = 0; i < totalPages; i++) {
                paginationHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
                    </li>
                `;
            }
            $('.pagination').html(paginationHTML);
        },
        error: function (xhr) {
            console.error("Error loading pagination:", xhr.responseText);
        }
    });
}

function goToPage(page) {
    currentPage = page;
    loadOrders();
}


$("#send").on("click", function (e) {
    e.preventDefault();

    const mailData = {
        toMail: $("#to").val(),
        subject: $("#subject").val(),
        massage: $("#message").val()
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/mail/send",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(mailData),
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token") // auth token hadanneth
        },
        success: function (response) {
            alert(response); // Email sent successfully
        },
        error: function (xhr) {
            alert("Error sending mail: " + xhr.responseText);
        }
    });
});