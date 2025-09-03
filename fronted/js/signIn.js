$('#signIn').on('click', function () {
    var username = $('#loginName').val();
    var password = $('#loginPassword').val();

    if (username && password) {
        $.ajax({
            method: 'POST',
            url: 'http://localhost:8080/auth/login',
            contentType: 'application/json',
            data: JSON.stringify({
                username: username,
                password: password
            }),
            success: function(response) {
                console.log("Login API Response:", response); // debug

                const token = response.data?.accessToken;
                if (token) {
                    // Save to localStorage
                    localStorage.setItem('username', username);
                    localStorage.setItem('token', token);

                    $.ajax({
                        method: 'GET',
                        url: 'http://localhost:8080/role/api/user-info',
                        headers: {
                            'Authorization': 'Bearer ' + token
                        },
                        success: function(userInfo) {
                            const role = userInfo.role;
                            if (role === 'ADMIN') {
                                console.log("ADMIN Token:", token);
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Welcome Admin!',
                                    text: 'Successfully Logged In',
                                    confirmButtonText: 'Ok',
                                }).then(() => {
                                    window.location.href = 'admin/adminDashBoard.html';
                                });
                            } else if (role === 'USER') {
                                console.log("USER Token:", token);
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Welcome!',
                                    text: 'Successfully Logged In as User',
                                    confirmButtonText: 'Ok',
                                    timer: 2000
                                }).then(() => {
                                    window.location.href = 'index.html';
                                });
                            } else {
                                Swal.fire({
                                    icon: 'warning',
                                    title: 'Unknown Role',
                                    text: 'User role is not recognized!'
                                });                                }
                        },
                        error: function () {
                            Swal.fire({
                                icon: 'error',
                                title: 'Unauthorized',
                                text: 'Session expired or unauthorized!'
                            });                            }
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Login Failed',
                        text: 'No token received from server!'
                    });                    }
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON?.message || 'Login failed. Please check your username and password.';
                Swal.fire({
                    icon: 'error',
                    title: 'Login Failed',
                    text: 'Please to registered'
                });                }
        });
    } else {
        Swal.fire({
            icon: 'warning',
            title: 'Missing Fields',
            text: 'Please enter both username and password.'
        });
    }
});

$('#showPasswordCheck').on('change', function() {
    let passwordField = $('#loginPassword');
    if ($(this).is(':checked')) {
        passwordField.attr('type', 'text');
    } else {
        passwordField.attr('type', 'password');
    }
});