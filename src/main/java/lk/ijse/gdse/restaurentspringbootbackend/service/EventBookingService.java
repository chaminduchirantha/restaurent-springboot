package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.EventBookingDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventBookingService {
    EventBookingDto createBooking(EventBookingDto eventBookingDto);
    List<EventBookingDto> getAllEventBooking();

    void deleteBooking(Long id);
}
