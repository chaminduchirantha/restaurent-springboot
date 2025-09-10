$("#cateringButton").on("click", function (e) {
    e.preventDefault();

    // Collect form data
    let cateringData = {
        fullname: $("#cateringName").val().trim(),
        phoneNumber: $("#cateringPhone").val().trim(),
        email: $("#cateringEmail").val().trim(),
        date: $("#cateringDate").val(),
        time: $("#cateringTime").val(),
        type: $("#cateringType").val(),
        guests: $("#buffetGuests").val(),
    };

    // Validation: check required fields
    if (!cateringData.fullname || !cateringData.phoneNumber || !cateringData.email ||
        !cateringData.date || !cateringData.time || !cateringData.type || !cateringData.guests) {
        Swal.fire({
            icon: 'warning',
            title: 'Missing Fields',
            text: 'Please fill all required fields before booking.'
        });
        return;
    }

    // Send data via AJAX
    $.ajax({
        url: "http://localhost:8080/api/v1/catering/booking",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(cateringData),
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: 'Booking Successful!',
                text: response.message,
                confirmButtonText: 'OK'
            });
            $("#cateringForm")[0].reset();
        },
        error: function (xhr) {
            console.error(xhr);
            Swal.fire({
                icon: 'error',
                title: 'Booking Failed',
                text: xhr.responseJSON ? xhr.responseJSON.message : 'An error occurred!'
            });
        }
    });
});
