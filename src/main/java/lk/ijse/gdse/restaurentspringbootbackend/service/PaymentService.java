package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayment();
    List<PaymentDto> getPaymentByPage(int page, int size);
    int getTotalPages(int size);
}
