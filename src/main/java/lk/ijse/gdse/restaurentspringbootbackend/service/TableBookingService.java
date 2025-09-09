package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.TableBookingDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TableBookingService {
    TableBookingDto createBooking(TableBookingDto tableBookingDto);

    List<TableBookingDto> getAllTableBooking();
}
