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
}
