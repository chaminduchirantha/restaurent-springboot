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
                                 <button class="btn btn-sm btn-danger cancel-btn">Cancel Booking</button>
                             </td>
                        </tr>`;
            });
            $('#eventBookingTable').html(rows);
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