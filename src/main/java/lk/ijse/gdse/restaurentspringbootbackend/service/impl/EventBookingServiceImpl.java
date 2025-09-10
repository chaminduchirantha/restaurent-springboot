package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.EventBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.TableBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.EventBooking;
import lk.ijse.gdse.restaurentspringbootbackend.entity.TableBooking;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.EventBookingRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.TableBookingRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.EventBookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventBookingServiceImpl implements EventBookingService {
    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final EventBookingRepo eventBookingRepo;
    @Override
    public EventBookingDto createBooking(EventBookingDto eventBookingDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        EventBooking eventBooking = modelMapper.map(eventBookingDto, EventBooking.class);
        eventBooking.setCustomer(customer);

        EventBooking saveBooking = eventBookingRepo.save(eventBooking);

        EventBookingDto saveEventBookingDto = modelMapper.map(saveBooking, EventBookingDto.class);
        saveEventBookingDto.setCustomerId(customer.getId());

        return saveEventBookingDto;
    }

    @Override
    public List<EventBookingDto> getAllEventBooking() {
        List<EventBooking>eventBookings = eventBookingRepo.findAll();
        List<EventBookingDto> eventBookingDtos = new ArrayList<>();
        for (EventBooking eventBooking : eventBookings) {
            eventBookingDtos.add(modelMapper.map(eventBooking, EventBookingDto.class));
        }
        return eventBookingDtos;
    }

    @Override
    public void deleteBooking(Long id) {
        EventBooking booking = eventBookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setCustomer(null);

        eventBookingRepo.delete(booking);
    }
}
