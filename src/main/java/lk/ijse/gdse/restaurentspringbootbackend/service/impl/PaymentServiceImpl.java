package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Payment;
import lk.ijse.gdse.restaurentspringbootbackend.repo.OrderRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.PaymentRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final PaymentRepo paymentRepo;
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Order order = (Order) orderRepo.findTopByCustomer_UsernameOrderByOrderDatetimeDesc(username)
                .orElseThrow(() -> new RuntimeException("No active order found for user: " + username));

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setOrder(order);

        Payment saved = paymentRepo.save(payment);

        return modelMapper.map(saved, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        List<Payment> payments = paymentRepo.findAll();
        List<PaymentDto> paymentDtos = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDtos.add(modelMapper.map(payment, PaymentDto.class));
        }
        return paymentDtos;
    }

    @Override
    public List<PaymentDto> getPaymentByPage(int page, int size) {
        int offset = page * size;
        List<Payment> payments = paymentRepo.findPaymentPaginated(size, offset);
        return modelMapper.map(payments, new TypeToken<List<PaymentDto>>() {}.getType());
    }

    @Override
    public int getTotalPages(int size) {
        long totalOrders = paymentRepo.getTotalPaymentCount();
        return (int) Math.ceil((double) totalOrders / size);    }
}
