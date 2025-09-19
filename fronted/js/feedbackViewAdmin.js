let currentPage = 0;
const pageSize = 5;

const token = localStorage.getItem('token');
$(document).ready(function () {
    loadFeeBack();
});

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}
function loadFeeBack() {
    $.ajax({
        url: `http://localhost:8080/api/v1/feedback/paginated?page=${currentPage}&size=${pageSize}`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const feedback = res || []; // backend returns raw array
            let rows = "";
            feedback.forEach(fb => {
                rows += `
                    <tr>
                        <td>${fb.email}</td>
                        <td>${fb.fullname}</td>
                        <td>${fb.services}</td>
                        <td>${fb.ratings}</td>
                        <td>${fb.message}</td>
                    </tr>`;
            });
            $('#feedbackTableBody').html(rows);
            loadPagination();
        },

        error: function(err){
            console.log(err);
            alert("Failed to load Feedback");
        }
    });
}

function loadPagination() {
    $.ajax({
        url: `http://localhost:8080/api/v1/feedback/total-pages?size=${pageSize}`,
        method: "GET",
        success: function (totalPages) {
            let paginationHTML = "";
            for (let i = 0; i < totalPages; i++) {
                paginationHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
                    </li>
                `;
            }
            $('.pagination').html(paginationHTML);
        },
        error: function (xhr) {
            console.error("Error loading pagination:", xhr.responseText);
        }
    });
}

function goToPage(page) {
    currentPage = page;
    loadFeeBack();
}