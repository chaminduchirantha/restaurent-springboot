
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
            <div class="row mb-3 g-3">
                <div class="col-md-6 mb-3">
                    <label for="menuName" class="form-label">Menu Item</label>
                    <input type="text" class="form-control" id="menuName" value="${selectedName}" readonly>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="menuPrice" class="form-label">Price</label>
                    <input type="text" class="form-control" id="menuPrice" value="Rs. ${selectedPrice}" readonly>
                </div>
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

    const orderData = {
        name: $("#menuName").val().trim(),
        price: parseFloat($("#menuPrice").val().replace("Rs.", "").trim()),
        orderType: $("#orderType").val().trim(),
        orderQty: parseInt($("#qty").val()),
        orderDatetime: $("#orderDatetime").val().trim(),
        status: 'pending',
        email: $("#email").val().trim(),
        total: total,
        paymentMethod:$("#paymentType").val().trim()
    };

    if (!orderData.name || !orderData.price || !orderData.orderType ||
        !orderData.orderQty || !orderData.orderDatetime || !orderData.email || !orderData.paymentMethod) {
        Swal.fire({
            icon: 'warning',
            title: 'Missing Fields',
            text: 'Please fill all required fields before placing the order.'
        });
        return;
    }

    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to submit this order?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Submit Order',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
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
                        confirmButtonText:
                            orderData.paymentMethod === "Card Payment" ? 'Proceed to Payment' :
                            orderData.paymentMethod === "Cash On Delivery" ? 'Enter Your Delivery Details' :
                                'OK'
                    }).then(() => {
                        sessionStorage.setItem("lastOrderTotal", total);

                        if (orderData.paymentMethod === "Card Payment") {
                            window.location.href = "payment.html";

                        } else if (orderData.paymentMethod === "Cash On Delivery") {
                            const deliveryModal = new bootstrap.Modal(document.getElementById('deliveryModal'));
                            deliveryModal.show();

                        } else {
                            $("#orderForm")[0].reset();
                            localStorage.removeItem("selectedMenuName");
                            localStorage.removeItem("selectedMenuPrice");
                        }
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
        }
    });
});

$("#orderType").on("change", function () {
    const orderType = $(this).val();

    if (orderType === "Delivery") {
        $("#paymentType option[value='Card Payment']").prop("disabled", true);
        $("#paymentType option[value='Cash Payment']").prop("disabled", true);
        $("#paymentType option[value='Cash On Delivery']").prop("disabled", false);
    } else {
        $("#paymentType option[value='Card Payment']").prop("disabled", false);
        $("#paymentType option[value='Cash Payment']").prop("disabled", false);
        $("#paymentType option[value='Cash On Delivery']").prop("disabled", true);

    }
});



$("#qty").on("input", function () {
    const price = parseFloat($("#menuPrice").val().replace("Rs.", "").trim());
    const qty = parseInt($("#qty").val()) || 0;
    const total = price * qty;

    if ($("#total").length === 0) {
        $("#orderForm .d-flex").before(`
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
    const email = localStorage.getItem("email");

    if (!token || !email) {
        Swal.fire({
            icon: 'error',
            title: 'Not Logged In',
            text: 'Please log in first!'
        });
        return;
    }

    console.log("Email from localStorage:", email);

    $.ajax({
        url: `http://localhost:8080/api/v1/orders/user/${encodeURIComponent(localStorage.getItem("email"))}`,
        type: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (res) {
            console.log("Full API Response:", res);
            console.log("Orders Array:", res.data);
            let rows = "";
            res.data.forEach((order, index) => {
                rows += `
                    <tr>
                        <td>${order.name}</td>
                        <td>${order.orderQty}</td>
                        <td>Rs. ${order.total}</td>
                        <td>${order.status}</td>
                    </tr>
                `;
            });
            $("#ordersTableBody").html(rows);


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

