package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.DeliveryDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/delivery")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> save(@RequestBody DeliveryDto deliveryDto) {
        DeliveryDto saveDelivery  = deliveryService.createDelivery(deliveryDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "Payment Saved Successfully", saveDelivery)
        );
    }
}
