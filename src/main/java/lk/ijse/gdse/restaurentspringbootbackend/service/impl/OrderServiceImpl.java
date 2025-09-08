package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Feedback;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.OrderRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;


    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setName(ordersDto.getName());
        order.setEmail(ordersDto.getEmail());
        order.setPrice(ordersDto.getPrice());
        order.setOrderType(ordersDto.getOrderType());
        order.setOrderQty(ordersDto.getOrderQty());
        order.setOrderDatetime(ordersDto.getOrderDatetime());
        order.setStatus(ordersDto.getStatus());
        order.setNotes(ordersDto.getNotes());
        order.setCustomer(customer);

        Order savedOrder = orderRepo.save(order);

        OrdersDto savedDto = new OrdersDto(
                savedOrder.getOrderId(),
                savedOrder.getName(),
                savedOrder.getEmail(),
                savedOrder.getPrice(),
                savedOrder.getOrderType(),
                savedOrder.getOrderQty(),
                savedOrder.getOrderDatetime(),
                savedOrder.getStatus(),
                savedOrder.getNotes(),
                customer.getId()
        );

        return savedDto;
    }

    public List<OrdersDto> getAllOrders() {
        List<Order>orders = orderRepo.findAll();
        List<OrdersDto> ordersDtos = new ArrayList<>();
        for (Order order : orders) {
            ordersDtos.add(modelMapper.map(order, OrdersDto.class));
        }
        return ordersDtos;
    }
}