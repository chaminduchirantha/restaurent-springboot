$(document).ready(function () {

    loadOccupiedFromLocal();

    setInterval(syncOccupiedTables, 5000);

    $(".table-item").on("click", function () {
        if ($(this).hasClass("occupied")) {
            Swal.fire({
                icon: 'warning',
                title: 'Table Occupied',
                text: 'This table is already booked!'
            });
            return;
        }
        $(".table-item").removeClass("selected");
        $(this).addClass("selected");
    });

    $("#table-booking").on("click", function (e) {
        e.preventDefault();

        const bookingData = {
            fullname: $("#fullName").val().trim(),
            phoneNumber: $("#phoneNumber").val().trim(),
            email: $("#email").val().trim(),
            time: $('#time').val(),
            orderDatetime: $('#date').val(),
            guests: $("#guests").val(),
            tables: $(".table-item.selected").data("table") || "",
            requests: $("textarea").val().trim(),
        };

        if (!bookingData.fullname || !bookingData.phoneNumber || !bookingData.email ||
            !bookingData.time || !bookingData.orderDatetime || !bookingData.guests ||
            !bookingData.tables) {
            Swal.fire({
                icon: 'warning',
                title: 'Missing Fields',
                text: 'Please fill all required fields and select a table.'
            });
            return;
        }

        $.ajax({
            url: "http://localhost:8080/api/v1/table/booking",
            type: "POST",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            data: JSON.stringify(bookingData),
            success: function (res) {
                Swal.fire({
                    icon: 'success',
                    title: 'Table Booked!',
                    text: res.message,
                    confirmButtonText: 'OK'
                });

                const selectedTable = $(".table-item.selected").data("table");

                $(".table-item.selected")
                    .addClass("occupied")
                    .removeClass("selected");

                let occupiedTables = JSON.parse(localStorage.getItem("occupiedTables")) || [];
                if (!occupiedTables.includes(selectedTable)) {
                    occupiedTables.push(selectedTable);
                    localStorage.setItem("occupiedTables", JSON.stringify(occupiedTables));
                }

                $("#table-form")[0].reset();
            },
            error: function (xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Booking Failed',
                    text: xhr.responseText
                });
            }
        });
    });

    function loadOccupiedFromLocal() {
        let occupiedTables = JSON.parse(localStorage.getItem("occupiedTables")) || [];
        $(".table-item").removeClass("occupied");
        occupiedTables.forEach(function (tableId) {
            $(`.table-item[data-table='${tableId}']`).addClass("occupied");
        });
    }

    function syncOccupiedTables() {
        $.ajax({
            url: "http://localhost:8080/api/v1/table/all",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            success: function (res) {
                const serverTables = (res.data || []).map(tb => tb.tables);

                localStorage.setItem("occupiedTables", JSON.stringify(serverTables));

                $(".table-item").removeClass("occupied");
                serverTables.forEach(id => {
                    $(`.table-item[data-table='${id}']`).addClass("occupied");
                });
            },
            error: function () {
                console.warn("Failed to sync occupied tables");
            }
        });
    }

});