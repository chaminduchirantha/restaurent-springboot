let currentPage = 0;
const pageSize = 5;

const token = localStorage.getItem('token');
$(document).ready(function () {
    loadCustomers();
});

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

// Load all customers
function loadCustomers() {
    $.ajax({
        url: `http://localhost:8080/api/v1/user/paginated?page=${currentPage}&size=${pageSize}`,
        type: 'GET',
        headers: authHeaders(),
        success: function (res) {
            const customers = res.data || []; // <-- important
            let rows = "";
            customers.forEach(customer => {
                rows +=
                    `<tr>
                     <td>${customer.username}</td>
                     <td>${customer.email}</td>
                     <td>${customer.password}</td>
                     <td>${customer.role}</td>
                     <td>
                         <button class="btn btn-warning me-2" onclick="openEditModal('${customer.id}','${customer.username}', '${customer.email}', '${customer.password}', '${customer.role}')">Edit</button>
                         <button class="btn btn-sm btn-danger" onclick="deleteCustomer('${customer.id}')">Delete</button>
                     </td>
                  </tr>`;
            });
            $('#customerTableBody').html(rows);
            loadPagination(); // make sure pagination is called here
        },
        error: function(err){
            console.log(err);
            alert("Failed to load customers");
        }
    });
}


// Save new customer

$('#saveCustomerBtn').on('click', function (e) {
    e.preventDefault();

    let customerData = {
        username: $('#addUsername').val(),
        email: $('#addEmail').val(),
        password: $('#addPassword').val(),
        role: $('#addRole').val(),
    };

    console.log("Sending customer data:", customerData);

    $.ajax({
        url: "http://localhost:8080/api/v1/user/save",
        type: "POST",
        headers: authHeaders(),
        data: JSON.stringify(customerData),
        success: function (response) {
            alert("customer added Successfully!");
            $('#addCustomerForm')[0].reset();
            $('#addCustomerModal').modal('hide');
            loadCustomers();
        },
        error: function (xhr) {
            console.error("Error saving customer:", xhr.responseText);
            alert("Error: " + xhr.responseText);
        }
    });
});

function loadPagination() {
    $.ajax({
        url: `http://localhost:8080/api/v1/user/total-pages?size=${pageSize}`,
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
    loadCustomers();
}

$('#updateCustomerBtn').on('click', function(event){
    updateCustomer(event);
});

function openEditModal(id, username, email, password, role) {
    $('#editUserId').val(id);
    $('#editUsername').val(username);
    $('#editEmail').val(email);
    $('#editPassword').val(password);
    $('#editRole').val(role);
    new bootstrap.Modal(document.getElementById('editCustomerModal')).show();
}

function updateCustomer(event) {
    event.preventDefault();

    const userId = $('#editUserId').val();
    const username = $('#editUsername').val();
    const userEmail = $('#editEmail').val();
    const userPassword = $('#editPassword').val();
    const userRole = $('#editRole').val();

    const customerData = {
        id: userId,
        email: userEmail,
        password: userPassword,
        role: userRole,
        username: username,
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/user/update",
        type: "PUT",
        headers: authHeaders(),
        data: JSON.stringify(customerData),
        success: function () {
            alert("Customer updated successfully!");
            const modalEl = document.getElementById('editCustomerModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide(); // modal close karanne me widiyata

            loadCustomers(currentPage);
        },
        error: function (xhr) {
            alert("Error updating Customer: " + xhr.responseText);
        }
    });
}

function deleteCustomer(customerId) {
    if(!confirm("Are you sure you want to delete this customer?")) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/user/delete/${customerId}`,
        type: "DELETE",
        headers: authHeaders(),
        success: function(response) {
            alert("Customer deleted successfully!");
            loadCustomers(currentPage); // reload table
        },
        error: function(xhr) {
            alert("Error deleting customer: " + xhr.responseText);
        }
    });
}
