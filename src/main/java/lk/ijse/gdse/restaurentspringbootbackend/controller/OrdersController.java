package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto>place(@RequestBody OrdersDto ordersDto) {
        OrdersDto place  = orderService.createOrder(ordersDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "Order Saved Successfully", place)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllOrders() {
        List<OrdersDto> order = orderService.getAllOrders();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", order)
        );
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponseDto> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<OrdersDto> ordersDtos = orderService.getOrdersByPage(page, size);
        return ResponseEntity.ok(new ApiResponseDto(200, "OK", ordersDtos));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = orderService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> getOrdersByCustomer(@PathVariable("email") String email) {
        List<Order> orders = orderService.getOrdersByCustomer(email);

        List<OrdersDto> dtos = orders.stream()
                .map(o -> {
                    OrdersDto dto = new OrdersDto();
                    dto.setOrderId(o.getOrderId());
                    dto.setName(o.getName());
                    dto.setEmail(o.getEmail());
                    dto.setPrice(o.getPrice());
                    dto.setOrderType(o.getOrderType());
                    dto.setOrderQty(o.getOrderQty());
                    dto.setOrderDatetime(o.getOrderDatetime());
                    dto.setStatus(o.getStatus());
                    dto.setTotal(o.getTotal());
                    dto.setCustomerId(o.getCustomer().getId());
                    return dto;
                })
                .collect(Collectors.toList());

        ApiResponseDto response = new ApiResponseDto(
                200,
                "OK",
                dtos
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> changeStatus(@PathVariable("id") Long id){
        orderService.changeStatus(id);
        return ResponseEntity.ok(
                new ApiResponseDto(
                        200,
                        "order change Status update",
                        null
                )
        );
    }
}
