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

        // DTO → Entity
        Order order = modelMapper.map(ordersDto, Order.class);
        order.setCustomer(customer);

        // Save order
        Order savedOrder = orderRepo.save(order);

        // Entity → DTO
        OrdersDto savedDto = modelMapper.map(savedOrder, OrdersDto.class);
        savedDto.setCustomerId(customer.getId());

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