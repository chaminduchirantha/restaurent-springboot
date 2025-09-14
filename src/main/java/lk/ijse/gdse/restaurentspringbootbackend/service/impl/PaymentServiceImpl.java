package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.PaymentDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Payment;
import lk.ijse.gdse.restaurentspringbootbackend.repo.OrderRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.PaymentRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

}
