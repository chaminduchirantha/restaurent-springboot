package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CateringServiceDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.EventBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.TableBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.EventBooking;
import lk.ijse.gdse.restaurentspringbootbackend.service.EventBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class EventController {

    private final EventBookingService eventBookingService;
    @PostMapping("booking")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> eventBooking(@RequestBody EventBookingDto eventBookingDto) {
        EventBookingDto eventBookingDto1=eventBookingService.createBooking(eventBookingDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "event Booking Saved Successfully", eventBookingDto1)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDto> getAllBooking() {
        List<EventBookingDto> eventBookingDtos = eventBookingService.getAllEventBooking();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "SuccessFull All the Table booking details;", eventBookingDtos)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> deleteBooking(@PathVariable Long id) {
        eventBookingService.deleteBooking(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Booking deleted successfully", null));
    }
}
