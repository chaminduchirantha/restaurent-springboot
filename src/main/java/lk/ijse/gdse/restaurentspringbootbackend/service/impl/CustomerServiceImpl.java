package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Role;
import lk.ijse.gdse.restaurentspringbootbackend.exception.ResourceNotFoundException;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerDto> getAllCustomer() {

        List<Customer> customers = customerRepo.findAll();

        List<CustomerDto>customerDtos = new ArrayList<>();
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No customer Found");
        }
        for (Customer customer : customers) {
            customerDtos.add(modelMapper.map(customer, CustomerDto.class));

        }
        return customerDtos;
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));

        Customer customer = modelMapper.map(customerDto, Customer.class);

        Customer saved = customerRepo.save(customer);
        CustomerDto response = modelMapper.map(saved, CustomerDto.class);
        response.setPassword("********");
        return response;
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepo.findById(customerDto.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID "));

        customer.setUsername(customerDto.getUsername());
        customer.setEmail(customerDto.getEmail());
        customer.setRole(Role.valueOf(customerDto.getRole()));

        if (customerDto.getPassword() != null && !customerDto.getPassword().isBlank()) {
            customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        }

        Customer updated = customerRepo.save(customer);

        CustomerDto response = modelMapper.map(updated, CustomerDto.class);
        response.setPassword("********");
        return response;
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepo.delete(customer);
    }

    @Override
    public List<CustomerDto> getCustomersByPage(int page, int size) {
        int offset = page * size;
        List<Customer> customers = customerRepo.findCustomerPaginated(size, offset);
        return modelMapper.map(customers, new TypeToken<List<CustomerDto>>() {}.getType());
    }


    @Override
    public int getTotalPages(int size) {
        long totalCustomers = customerRepo.getTotalCustomerCount();
        return (int) Math.ceil((double) totalCustomers / size);
    }


}
