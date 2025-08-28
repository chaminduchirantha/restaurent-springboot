$(document).ready(function () {
    function filterCards(category) {
        $('[data-category]').each(function () {
            const categories = $(this).data('category').split(' ');
            if (categories.includes(category)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    $('#rice').on('click', function () {
        filterCards('rice');
    });

    $('#kottu').on('click', function () {
        filterCards('kottu');
    });

    $('#indianfoods').on('click', function () {
        filterCards('indianfoods');
    });

    $('#bakery').on('click', function () {
        filterCards('bakery');
    });

    $('#baverages').on('click', function () {
        filterCards('baverages');
    });

    $('#all').on('click', function () {
        filterCards('all');
    });




let viewCounts = JSON.parse(localStorage.getItem('viewCounts')) || {};

    $(document).ready(function () {
        $('.view-details-btn').click(function () {
            const productId = $(this).data('id');
            const modalId = $(this).data('modal-id');

            // Increment the count
            viewCounts[productId] = (viewCounts[productId] || 0) + 1;

            // Save to localStorage
            localStorage.setItem('viewCounts', JSON.stringify(viewCounts));

            // Find and update the view count inside the correct modal
            const modal = $('#' + modalId);
            modal.find('.view-count[data-id="' + productId + '"]').text(viewCounts[productId]);
        });

        // On page load, update all modal view-count spans with saved data
        $('.view-count').each(function () {
            const id = $(this).data('id');
            if (viewCounts[id]) {
                $(this).text(viewCounts[id]);
            }
        });
    });
});

const token = localStorage.getItem('token');
function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

$('#submitFeedback').on('click', function (e) {
    e.preventDefault();

    if (!token) {
        Swal.fire({
            icon: 'warning',
            title: 'Login Required',
            text: 'Please login first to submit feedback.',
            confirmButtonText: 'Go to Login'
        }).then(() => {
            window.location.href = "signInPage.html"; // redirect to login page
        });
        return;
    }

    let feedBackData = {
        fullname: $('#fullName').val(),
        email: $('#email').val(),
        services: $('#service').val(),
        ratings: $('#ratings').val(),
        message: $('#message').val()
    };

    console.log("Sending feedback data:", feedBackData);

    $.ajax({
        url: "http://localhost:8080/api/v1/feedback/save",
        type: "POST",
        headers: authHeaders(),
        data: JSON.stringify(feedBackData),
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Feedback submitted successfully!',
                showConfirmButton: false,
                timer: 2000
            });
            $('#addFeedbackForm')[0].reset();
        },
        error: function (xhr) {
            if (xhr.status === 403) {
                Swal.fire({
                    icon: 'error',
                    title: 'Unauthorized',
                    text: 'You are not authorized. Please login.',
                    confirmButtonText: 'Login'
                }).then(() => {
                    window.location.href = "signInPage.html";
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText || 'Something went wrong!',
                });
            }
        }
    });
});


