let currentPage = 0;
const pageSize = 4;

function loadDeliveries() {
    $.ajax({
        url: `http://localhost:8080/api/v1/delivery/paginated?page=${currentPage}&size=${pageSize}`,
        type: 'GET',
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (res) {
            const deliveries = res.data || [];
            let rows = "";
            deliveries.forEach(delivery => {
                rows += `
            <tr>
                <td>${delivery.fullName}</td>
                <td>${delivery.phoneNumber}</td>
                <td>${delivery.email}</td>
                <td>${delivery.address}</td>
                <td>${delivery.date}</td>
                <td>${delivery.time}</td>
                <td>
                    <button class="btn btn-sm btn-primary sendMailBtn" style="margin-bottom: 10px">
                        Send Mail
                    </button>
                </td>
            </tr>
        `;
            });
            $("#deliveryTableBody").html(rows);

            // Bind mail button clicks
            $(".sendMailBtn").on("click", function () {
                const row = $(this).closest("tr").children("td");

                const fullName = row.eq(0).text();
                const phone = row.eq(1).text();
                const email = row.eq(2).text();
                const address = row.eq(3).text();
                const date = row.eq(4).text();
                const time = row.eq(5).text();

                $("#to").val(email);
                $("#subject").val("Delivery Confirmation");
                $("#message").val(
                    `Hello ${fullName},\n\n` +
                    `Your delivery details are as follows:\n` +
                    `---------------------------------\n` +
                    `Name: ${fullName}\n` +
                    `Phone: ${phone}\n` +
                    `Address: ${address}\n` +
                    `Date: ${date}\n` +
                    `Time: ${time}\n` +
                    `---------------------------------\n\n` +
                    `Thank you for choosing Golden Spoon Restaurant!`
                );

                $("html, body").animate({
                    scrollTop: $("#mailForm").offset().top
                }, 500);
            });

            loadDeliveryPagination();
        },
        error: function (err) {
            console.error("Error loading deliveries:", err);
            alert("Failed to load deliveries");
        }
    });
}

function loadDeliveryPagination() {
    $.ajax({
        url: `http://localhost:8080/api/v1/delivery/total-pages?size=${pageSize}`,
        method: "GET",
        success: function (totalPages) {
            let paginationHTML = "";
            for (let i = 0; i < totalPages; i++) {
                paginationHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="goToDeliveryPage(${i})">${i + 1}</a>
                    </li>
                `;
            }
            $(".pagination").html(paginationHTML);
        },
        error: function (xhr) {
            console.error("Error loading pagination:", xhr.responseText);
        }
    });
}

function goToDeliveryPage(page) {
    currentPage = page;
    loadDeliveries();
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

$(document).ready(function () {
    loadDeliveries();
});


