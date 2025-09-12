const token = localStorage.getItem('token');
$(document).ready(function () {
    loadCateringBookings();
});

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}
function loadCateringBookings() {
    $.ajax({
        url: `http://localhost:8080/api/v1/catering/all`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const cateringBooking = res.data || [];
            let rows = "";
            cateringBooking.forEach((c, index) => {
                rows +=
                    `<tr data-id="${index}">
                        <td>${c.fullname}</td>
                        <td>${c.phoneNumber}</td>
                        <td>${c.email}</td>
                        <td>${c.date}</td>
                        <td>${c.time}</td>
                        <td>${c.type}</td>
                        <td>${c.guests}</td>
                        <td>
                            <button class="btn btn-sm btn-danger">Cancel</button>
                            <button class="btn btn-sm btn-primary sendMailBtn">Send Mail</button>
                        </td>
                    </tr>`;
            });
            $('#cateringBookingTableBody').html(rows);

            // Attach event for new buttons
            $(".sendMailBtn").on("click", function () {
                const row = $(this).closest("tr").children("td");

                // Extract details from row
                const fullname = row.eq(0).text();
                const phone = row.eq(1).text();
                const email = row.eq(2).text();
                const date = row.eq(3).text();
                const time = row.eq(4).text();
                const type = row.eq(5).text();
                const guests = row.eq(6).text();

                // Autofill form
                $("#to").val(email);
                $("#subject").val("Booking Confirmation - " + type);
                $("#message").val(
                    `Hello ${fullname},\n\n` +
                    `This is regarding your booking:\n\n` +
                    `Date: ${date}\n` +
                    `Time: ${time}\n` +
                    `Guests: ${guests}\n\n` +
                    `Thank you,\nGolden Spoon Restaurant`
                );

                // Scroll to form
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