package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepo customerRepo;

    @Override
    public List<CustomerDto> getAllCustomer() {

        List<Customer> customers = customerRepo.findAll();

        List<CustomerDto>customerDtos = new ArrayList<>();
        for (Customer customer : customers) {
            customerDtos.add(modelMapper.map(customer, CustomerDto.class));

        }
        return customerDtos;
    }
}
