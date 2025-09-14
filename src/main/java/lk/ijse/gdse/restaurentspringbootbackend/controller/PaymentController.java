package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/payment")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> save(@RequestBody PaymentDto paymentDto) {
        PaymentDto savePayment  = paymentService.createPayment(paymentDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "Order Saved Successfully", savePayment)
        );
    }
}

