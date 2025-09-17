package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.DeliveryDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Delivery;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.repo.DeliveryRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.OrderRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final ModelMapper modelMapper;
    private final OrderRepo orderRepo;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Order order = (Order) orderRepo.findTopByCustomer_UsernameOrderByOrderDatetimeDesc(username)
                .orElseThrow(() -> new RuntimeException("No active order found for user: " + username));

        Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
        delivery.setOrder(order);

        Delivery saved = deliveryRepo.save(delivery);

        return modelMapper.map(saved, DeliveryDto.class);
    }
}
