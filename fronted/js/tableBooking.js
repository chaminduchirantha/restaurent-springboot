$(document).ready(function () {
    // select table-item click
    $(".table-item").on("click", function () {
        if ($(this).hasClass("occupied")) {
            alert("This table is already booked!");
            return;
        }
        $(".table-item").removeClass("selected");
        $(this).addClass("selected");
    });

    $("#table-booking").on("click", function (e) {
        e.preventDefault();


        const bookingData = {
            fullname: $("#fullName").val(),
            phoneNumber: $("#phoneNumber").val(),
            email: $("#email").val(),
            time:$('#time').val(),
            orderDatetime: $('#date').val(),
            guests: $("#guests").val(),
            tables: $(".table-item.selected").data("table") || "",
            requests: $("textarea").val(),
        };

        $.ajax({
            url: "http://localhost:8080/api/v1/table/booking",
            type: "POST",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            data: JSON.stringify(bookingData),
            success: function (res) {
                alert(res.message);

                // Mark table as occupied
                $(".table-item.selected")
                    .addClass("occupied")
                    .removeClass("selected");

                $("#table-form")[0].reset();
            },
            error: function (xhr) {
                alert("Error: " + xhr.responseText);
            }
        });
    });
});
