package lk.ijse.gdse.restaurentspringbootbackend.controller;


import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CateringServiceDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.CateringServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/catering")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class CateringServiceBookingController {

    private final CateringServices cateringServices;

    @PostMapping("booking")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseDto> tableBooking(@RequestBody CateringServiceDto cateringServiceDto) {
        CateringServiceDto cateringServiceDto1=cateringServices.createBooking(cateringServiceDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "catering Booking Saved Successfully", cateringServiceDto1)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllBooking() {
        List<CateringServiceDto> cateringServiceDtos = cateringServices.ggetAllCaterings();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "SuccessFull All the Catering booking details;", cateringServiceDtos)
        );
    }
}
