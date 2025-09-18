package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrdersDto createOrder(OrdersDto ordersDto);
    List<OrdersDto> getAllOrders();
    List<OrdersDto> getOrdersByPage(int page, int size);
    int getTotalPages(int size);
    List<Order> getOrdersByCustomer(String email);
    void changeStatus(Long id);
}
