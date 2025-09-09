package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.TableBookingDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.TableBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/table")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")

public class TableBookingController {

    private final TableBookingService tableBookingService;

    @PostMapping("booking")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> tableBooking(@RequestBody TableBookingDto tableBookingDto) {
        TableBookingDto tableBookings=tableBookingService.createBooking(tableBookingDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "table Booking Saved Successfully", tableBookings)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDto> getAllBooking() {
        List<TableBookingDto> tableBooking = tableBookingService.getAllTableBooking();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "SuccessFull All the Table booking details;", tableBooking)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> deleteBooking(@PathVariable Long id) {
        tableBookingService.deleteBooking(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Booking deleted successfully", null));
    }
}
