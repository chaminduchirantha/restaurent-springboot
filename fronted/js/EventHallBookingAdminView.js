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
        url: `http://localhost:8080/api/v1/event/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const eventBooking = res.data || []; // <-- important
            let rows = "";
            eventBooking.forEach(eb => {
                rows +=
                    `<tr data-id="${eb.eventId}">
                             <td>${eb.fullname}</td>
                             <td>${eb.phoneNumber}</td>
                             <td>${eb.email}</td>
                             <td>${eb.date}</td>
                             <td>${eb.time}</td>
                             <td>${eb.duration}</td>
                             <td>${eb.services}</td>
                             <td>${eb.hallNo}</td>
                             <td>${eb.requests}</td>
                             <td>
                                 <button class="btn btn-sm btn-danger cancel-btn">Cancel</button>
                                 <button class="btn btn-sm btn-primary sendMailBtn">Send Mail</button>
                             </td>
                        </tr>`;
            });
            $('#eventBookingTable').html(rows);

            $(".sendMailBtn").on("click", function () {
                const row = $(this).closest("tr").children("td");

                const fullname = row.eq(0).text();
                const phone = row.eq(1).text();
                const email = row.eq(2).text();
                const date = row.eq(3).text();
                const time = row.eq(4).text();
                const duration = row.eq(5).text();
                const services = row.eq(6).text();
                const hallNo = row.eq(7).text();
                const requests = row.eq(8).text();

                // Autofill mail form
                $("#to").val(email);
                $("#subject").val("Booking Confirmation - Event Reservation");
                $("#message").val(
                    `Hello ${fullname},\n\n` +
                    `This is regarding your event hall booking:\n\n` +
                    `Date: ${date}\n` +
                    `Time: ${time}\n` +
                    `Duration: ${duration} hours\n` +
                    `Services: ${services}\n` +
                    `Hall Number: ${hallNo}\n` +
                    `Special Request: ${requests}\n\n` +
                    `Thank you,\nGolden Spoon Restaurant`
                );

                // Smooth scroll to mail form
                $('html, body').animate({
                    scrollTop: $("#mailForm").offset().top
                }, 500);
            });
        },
        error: function(err){
            console.log(err);
            alert("Failed to load bookings");
        }
    });

    $(document).on("click", ".cancel-btn", function () {
        const row = $(this).closest("tr");
        const eventId = row.data("id");

        if (!confirm("Are you sure you want to cancel this booking?")) return;

        $.ajax({
            url: `http://localhost:8080/api/v1/event/delete/${eventId}`,
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