$("#cateringButton").on("click", function (e) {
    e.preventDefault();

    // Collect form data
    let cateringData = {
        fullname: $("#cateringName").val(),
        phoneNumber: $("#cateringPhone").val(),
        email: $("#cateringEmail").val(),
        date: $("#cateringDate").val(),
        time: $("#cateringTime").val(),
        type: $("#cateringType").val(),
        guests: $("#buffetGuests").val(),
    };

    // Send data via AJAX
    $.ajax({
        url: "http://localhost:8080/api/v1/catering/booking",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(cateringData),
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token") // if JWT is used
        },
        success: function (response) {
            alert(response.message);
            $("#cateringForm")[0].reset();
        },
        error: function (xhr) {
            console.error(xhr);
            alert("Booking failed: " + xhr.responseJSON.message);
        }
    });
});