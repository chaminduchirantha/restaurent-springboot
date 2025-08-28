$('#signUp').on('click', function () {
    var username = $('#signupName').val();
    var email = $('#signupEmail').val();
    var password = $('#signupPassword').val();

    if (username && email && password) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/auth/register',
            data: JSON.stringify({
                username: username,
                email: email,
                password: password,
                role: "USER"
            }),
            contentType: 'application/json',
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Register successfully!',
                    showConfirmButton: false,
                    timer: 2000
                }).then(() => {
                    window.location.href = "signInPage.html";
                });
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON?.message || 'Error occurred';
                alert(errorMessage);
            }
        });
    } else {
        alert('Please fill in all fields');
    }
});

$('#showPasswordCheck').on('change', function() {
    let passwordField = $('#signupPassword');
    if ($(this).is(':checked')) {
        passwordField.attr('type', 'text');
    } else {
        passwordField.attr('type', 'password');
    }
});