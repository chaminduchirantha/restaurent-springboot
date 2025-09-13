package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.OrderRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.OrderService;
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

        double total = ordersDto.getPrice() * ordersDto.getOrderQty();
        ordersDto.setTotal(total);

        // DTO → Entity
        Order order = modelMapper.map(ordersDto, Order.class);
        order.setCustomer(customer);
        order.setTotal(total);// ensure entity also gets total
        order.setStatus("pending");


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

    @Override
    public List<OrdersDto> getOrdersByPage(int page, int size) {
        int offset = page * size;
        List<Order> orders = orderRepo.findOrderPaginated(size, offset);
        return modelMapper.map(orders, new TypeToken<List<OrdersDto>>() {}.getType());    }

    @Override
    public int getTotalPages(int size) {
        long totalOrders = orderRepo.getTotalOrdersCount();
        return (int) Math.ceil((double) totalOrders / size);
    }

    @Override
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepo.findByCustomerId(customerId);
    }

    @Override
    public void changeStatus(Long id) {
        orderRepo.updateStatus(id);
    }
}