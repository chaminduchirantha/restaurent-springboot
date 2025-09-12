
const token = localStorage.getItem('token');
const username = localStorage.getItem('username');
const customerId = parseInt(localStorage.getItem("customerId"));

if (!token || !username) {
    window.location.href = 'signInPage.html';
} else {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/role/api/user-info',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (response) {
            if (response.role === 'USER') {
                $('body').show(); // allow page to show
                console.log(" Access granted for USER:", username);
            } else {
                alert("Access Denied: You are not a regular user.");
                window.location.href ='signInPage.html';
            }
        },
        error: function () {
            alert("Session expired or unauthorized.");
            localStorage.clear();
            window.location.href = 'signInPage.html';
        }
    });
}




$(document).ready(function () {
    const selectedName = localStorage.getItem("selectedMenuName");
    const selectedPrice = localStorage.getItem("selectedMenuPrice");

    if (selectedName && selectedPrice) {
        if ($("#menuName").length === 0) {
            $("#orderForm").prepend(`
                <div class="mb-3">
                    <label for="menuName" class="form-label">Menu Item</label>
                    <input type="text" class="form-control" id="menuName" value="${selectedName}" readonly>
                </div>
                <div class="mb-3">
                    <label for="menuPrice" class="form-label">Price</label>
                    <input type="text" class="form-control" id="menuPrice" value="Rs. ${selectedPrice}" readonly>
                </div>
            `);
        }
    }
});

if (!token) {
    window.location.href = "signInPage.html";
}

$("#orderSave").on("click", function(e) {
    e.preventDefault();

    const token = localStorage.getItem("token");

    const price = parseFloat($("#menuPrice").val().replace("Rs.", "").trim());
    const qty = parseInt($("#qty").val());

    const total = price * qty;


    // Collect form values
    const orderData = {
        name: $("#menuName").val().trim(),
        price: parseFloat($("#menuPrice").val().replace("Rs.", "").trim()),
        orderType: $("#orderType").val().trim(),
        orderQty: parseInt($("#qty").val()),
        orderDatetime: $("#orderDatetime").val().trim(),
        status: $("#status").val().trim(),
        notes: $("#notes").val().trim(),
        email: $("#email").val().trim(),
        total: total,

    };

    // Validation: check required fields
    if (!orderData.name || !orderData.price || !orderData.orderType ||
        !orderData.orderQty || !orderData.orderDatetime || !orderData.status || !orderData.email) {
        Swal.fire({
            icon: 'warning',
            title: 'Missing Fields',
            text: 'Please fill all required fields before placing the order.'
        });
        return;
    }

    // AJAX request
    $.ajax({
        url: "http://localhost:8080/api/v1/orders/place",
        type: "POST",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(orderData),
        success: function(res) {
            Swal.fire({
                icon: 'success',
                title: 'Order Placed!',
                text: 'Your order has been saved successfully.',
                confirmButtonText: 'OK'
            });

            console.log("Order saved:", res.data);
            $("#orderForm")[0].reset();
            localStorage.removeItem("selectedMenuName");
            localStorage.removeItem("selectedMenuPrice");
        },
        error: function(xhr) {
            console.error("Order save failed:", xhr);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Failed to place the order!'
            });
        }
    });
});

$("#qty").on("input", function () {
    const price = parseFloat($("#menuPrice").val().replace("Rs.", "").trim());
    const qty = parseInt($("#qty").val()) || 0;
    const total = price * qty;

    if ($("#total").length === 0) {
        $("#orderForm").append(`
            <div class="mb-3">
                <label for="total" class="form-label">Total</label>
                <input type="text" class="form-control" id="total" value="Rs. ${total}" readonly>
            </div>
        `);
    } else {
        $("#total").val("Rs. " + total);
    }
});

$("#viewOrders").on("click", function () {
    const token = localStorage.getItem("token");
    const customerIdStr = localStorage.getItem("customerId"); // string
    const customerId = customerIdStr ? parseInt(customerIdStr) : null;

    if (!customerId || customerId === "null") {
        Swal.fire({
            icon: "error",
            title: "User not logged in",
            text: "Please log in first."
        });
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/v1/orders/user/${customerId}`,
        type: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (res) {
            let rows = "";
            res.data.forEach((order, index) => {
                rows += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${order.name}</td>
                        <td>${order.orderQty}</td>
                        <td>Rs. ${order.total}</td>
                        <td>${order.status}</td>
                    </tr>
                `;
            });
            $("#ordersTableBody").html(rows);

            // Modal show කරන්න
            const modal = new bootstrap.Modal(document.getElementById('ordersModal'));
            modal.show();
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Failed to load orders!'
            });
        }
    });
});
