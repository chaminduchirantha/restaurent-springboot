let currentPage = 0;
const pageSize = 5;
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
        url: `http://localhost:8080/api/v1/orders/paginated?page=${currentPage}&size=${pageSize}`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const orders = res.data || []; // <-- important
            let rows = "";
            orders.forEach(order => {
                rows +=
                    `<tr>
                     <td>${order.name}</td>
                     <td>${order.price}</td>
                     <td>${order.orderType}</td>
                     <td>${order.orderQty}</td>
                     <td>${order.orderDatetime}</td>
                     <td>${order.email}</td>
                     <td>${order.status}</td>
                     <td>${order.notes}</td>
                     <td>Rs.${order.total}.00</td>
                       <td>
                         <button class="btn btn-sm btn-primary sendMailBtn">Send Mail</button>
                        </td>
                  </tr>`;
            });
            $('#orderTableBody').html(rows);
            $('.sendMailBtn').on('click', function () {
                const row = $(this).children('td');

                const name = row.eq(0).text();
                const email = row.eq(5).text();
                const notes = row.eq(7).text();
                const total = row.eq(8).text();

                $('#to').val(email);
                $('#subject').val("Regarding Your Order");
                $('#message').val(
                    `Hello ${name},\n\n` +
                    `This is regarding your order:\n` +
                    `Notes: ${notes}\n` +
                    `Total: ${total}\n\n` +
                    `Thank you,\nGolden Spoon Restaurant`
                );

                // Smooth scroll to mail form
                $('html, body').animate({
                    scrollTop: $("#mailForm").offset().top
                }, 500);
            });

            loadPagination(); // refresh pagination
        },
        error: function (err) {
            console.log(err);
            alert("Failed to load Orders");
        }
    });
}

function loadPagination() {
    $.ajax({
        url: `http://localhost:8080/api/v1/orders/total-pages?size=${pageSize}`,
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