package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.OrdersDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.TableBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.entity.TableBooking;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.TableBookingRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.TableBookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableBookingServiceImpl implements TableBookingService {

    private final ModelMapper modelMapper;
    private final CustomerRepo customerRepo;
    private final TableBookingRepo tableBookingRepo;
    @Override
    public TableBookingDto createBooking(TableBookingDto tableBookingDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        TableBooking tableBooking = modelMapper.map(tableBookingDto, TableBooking.class);
        tableBooking.setCustomer(customer);

        TableBooking saveBooking = tableBookingRepo.save(tableBooking);

        TableBookingDto saveTableBookingDto = modelMapper.map(saveBooking, TableBookingDto.class);
        saveTableBookingDto.setCustomerId(customer.getId());

        return saveTableBookingDto;
    }

    @Override
    public List<TableBookingDto> getAllTableBooking() {
        List<TableBooking>tableBookings = tableBookingRepo.findAll();
        List<TableBookingDto> tableBookingDtos = new ArrayList<>();
        for (TableBooking tableBooking : tableBookings) {
            tableBookingDtos.add(modelMapper.map(tableBooking, TableBookingDto.class));
        }
        return tableBookingDtos;
    }

    @Override
    public void deleteBooking(Long id) {
        tableBookingRepo.deleteById(id);
    }
}
