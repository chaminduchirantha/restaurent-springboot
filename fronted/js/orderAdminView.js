let currentPage = 0;
const pageSize = 6;
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
            const orders = res.data || [];
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
                        <td>Rs.${order.total}.00</td>
                        <td>${order.paymentMethod}</td>
                        <td>
                            <button class="btn btn-sm btn-primary sendMailBtn" style="margin-bottom: 10px">Send Mail</button>
                             <button class="btn btn-success btn-sm mark-delivered" data-id="${order.orderId}">Complete</button>

                        </td>
                    </tr>`;
            });
            $('#orderTableBody').html(rows);

            // Bind event after rows load
            $('.sendMailBtn').on('click', function () {
                const row = $(this).closest("tr").children("td");

                const name = row.eq(0).text();
                const price = row.eq(1).text();
                const orderType = row.eq(2).text();
                const orderQty = row.eq(3).text();
                const orderDatetime = row.eq(4).text();
                const email = row.eq(5).text();
                const status = row.eq(6).text();
                const total = row.eq(7).text();

                // Set values in mail form
                $('#to').val(email);
                $('#subject').val("Regarding Your Order");
                $('#message').val(
                    `Hello ,\n\n` +
                    `This is regarding your order:\n` +
                    `---------------------------------\n` +
                    `Item: ${name}\n` +
                    `Price: Rs.${price}\n` +
                    `Type: ${orderType}\n` +
                    `Quantity: ${orderQty}\n` +
                    `Date: ${orderDatetime}\n` +
                    `Status: ${status}\n` +
                    `Total: ${total}\n` +
                    `---------------------------------\n\n` +
                    `Thank you,\nGolden Spoon Restaurant`
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
            alert("Failed to load Orders");
        }
    });
}

// Delegate click event for dynamically loaded rows
$(document).on("click", ".mark-delivered", function() {
    const orderId = $(this).data("id");
    const token = localStorage.getItem("token");

    Swal.fire({
        title: 'Change Status?',
        text: "Do you want to mark this order as complete?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Complete',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/orders/status/${orderId}`,
                type: "PATCH",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function(res) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Status Updated!',
                        text: 'Order has been marked as complete.'
                    });
                    // Refresh table after status change
                    loadOrders();
                },
                error: function(xhr) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Failed to update status!'
                    });
                }
            });
        }
    });
});

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