package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrdersDto createOrder(OrdersDto ordersDto);
}
