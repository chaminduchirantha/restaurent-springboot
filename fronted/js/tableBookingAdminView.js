const token = localStorage.getItem('token');
$(document).ready(function () {
    loadTableBookings();
});

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}
function loadTableBookings() {
    $.ajax({
        url: `http://localhost:8080/api/v1/table/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const tableBooking = res.data || [];
            let rows = "";
            tableBooking.forEach((tb, index) => {
                rows +=
                    `<tr data-id="${tb.tableid}">
                         <td>${tb.fullname}</td>
                         <td>${tb.phoneNumber}</td>
                         <td>${tb.email}</td>
                         <td>${tb.date}</td>
                         <td>${tb.time}</td>
                         <td>${tb.guests}</td>
                         <td>${tb.tables}</td>
                         <td>${tb.requests}</td>
                         <td>
                             <button class="btn btn-sm btn-danger cancel-btn">Cancel Booking</button>
                             <button class="btn btn-sm btn-primary sendMailBtn">Send Mail</button>
                         </td>
                    </tr>`;
            });
            $('#tableBooking').html(rows);

            // --- Send Mail button click ---
            $(".sendMailBtn").on("click", function () {
                const row = $(this).closest("tr").children("td");

                const fullname = row.eq(0).text();
                const phone = row.eq(1).text();
                const email = row.eq(2).text();
                const date = row.eq(3).text();
                const time = row.eq(4).text();
                const guests = row.eq(5).text();
                const tables = row.eq(6).text();
                const requests = row.eq(7).text();

                // Autofill mail form
                $("#to").val(email);
                $("#subject").val("Booking Confirmation - Table Reservation");
                $("#message").val(
                    `Hello ${fullname},\n\n` +
                    `This is regarding your table booking:\n\n` +
                    `Date: ${date}\n` +
                    `Time: ${time}\n` +
                    `Guests: ${guests}\n` +
                    `Tables: ${tables}\n` +
                    `Special Request: ${requests}\n\n` +
                    `Thank you,\nGolden Spoon Restaurant`
                );

                // Smooth scroll to form
                $('html, body').animate({
                    scrollTop: $("#mailForm").offset().top
                }, 500);
            });
        },
        error: function (err) {
            console.log(err);
            alert("Failed to load customers");
        }
    });
}


$(document).on("click", ".cancel-btn", function () {
    const row = $(this).closest("tr");
    const bookingId = row.data("id");

    if (!confirm("Are you sure you want to cancel this booking?")) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/table/delete/${bookingId}`,
        type: 'DELETE',
        headers: authHeaders(),
        success: function (res) {
            alert(res.message || "Booking deleted successfully");
            row.remove(); // remove row from table
        },
        error: function (xhr) {
            alert(xhr.responseText || "Failed to cancel booking");
        }
    });
});

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