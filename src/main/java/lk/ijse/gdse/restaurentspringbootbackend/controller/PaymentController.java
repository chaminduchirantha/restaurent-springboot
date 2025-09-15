package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                new ApiResponseDto(201, "Payment Saved Successfully", savePayment)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllOrders() {
        List<PaymentDto> paymentDtos = paymentService.getAllPayment();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", paymentDtos)
        );
    }
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponseDto> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<PaymentDto> paymentDtos = paymentService.getPaymentByPage(page, size);
        return ResponseEntity.ok(new ApiResponseDto(200, "OK", paymentDtos));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = paymentService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }
}

