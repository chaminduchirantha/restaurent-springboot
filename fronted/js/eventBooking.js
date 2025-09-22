$(document).ready(function () {

    loadOccupiedFromLocal();
    setInterval(syncOccupiedTables, 5000);


    $(".event-item").on("click", function () {
        if ($(this).hasClass("occupied")) {
            Swal.fire({
                icon: 'warning',
                title: 'Table Occupied',
                text: 'This hall is already booked by another user!'
            });
            return;
        }
        $(".event-item").removeClass("selected");
        $(this).addClass("selected");
    });

    $("#eventBookingButton").on("click", function (e) {
        e.preventDefault();

        const eventBookingData = {
            fullname: $("#eventName").val().trim(),
            phoneNumber: $("#eventPhone").val().trim(),
            email: $("#eventEmail").val().trim(),
            time: $('#eventTime').val(),
            date: $('#eventDate').val(),
            duration: $('#duration').val(),
            services: $('#services').val(),
            hallNo: $(".event-item.selected").data("table") || "",
            requests: $("#eventRequest").val().trim()   // <-- fixed id
        };

        if (!eventBookingData.fullname || !eventBookingData.phoneNumber || !eventBookingData.email ||
            !eventBookingData.time || !eventBookingData.date || !eventBookingData.duration ||
            !eventBookingData.services || !eventBookingData.hallNo) {
            Swal.fire({
                icon: 'warning',
                title: 'Missing Fields',
                text: 'Please fill all required fields and select a hall.'
            });
            return;
        }

        $.ajax({
            url: "http://localhost:8080/api/v1/event/booking",
            type: "POST",
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            data: JSON.stringify(eventBookingData),
            success: function (res) {
                Swal.fire({
                    icon: 'success',
                    title: 'Event Booked!',
                    text: res.message,
                    confirmButtonText: 'OK'
                });

                const selectedTable = $(".event-item.selected").data("table");

                $(".event-item.selected")
                    .addClass("occupied")
                    .removeClass("selected");

                let occupiedTables = JSON.parse(localStorage.getItem("occupiedTables")) || [];
                if (!occupiedTables.includes(selectedTable)) {
                    occupiedTables.push(selectedTable);
                    localStorage.setItem("occupiedTables", JSON.stringify(occupiedTables));
                }

                $("#eventBookingForm")[0].reset();
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
        let occupiedEvent = JSON.parse(localStorage.getItem("occupiedTables")) || [];
        $(".event-item").removeClass("occupied");
        occupiedEvent.forEach(function (eventId) {
            $(`.event-item[data-table='${eventId}']`).addClass("occupied");
        });
    }

    function syncOccupiedTables() {
        $.ajax({
            url: "http://localhost:8080/api/v1/event/all",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            success: function (res) {
                console.log("Server events:", res.data);

                const serverTables = (res.data || []).map(ev => ev.hallNo);

                localStorage.setItem("occupiedTables", JSON.stringify(serverTables));

                $(".event-item").removeClass("occupied selected");
                serverTables.forEach(id => {
                    $(`.event-item[data-table='${id}']`).addClass("occupied");
                });
            },
            error: function () {
                console.warn("Failed to sync occupied tables");
            }
        });
    }

});
