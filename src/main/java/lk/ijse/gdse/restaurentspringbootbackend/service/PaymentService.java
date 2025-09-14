package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
}
