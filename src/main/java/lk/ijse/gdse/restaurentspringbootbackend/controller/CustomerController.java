package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
@Slf4j
public class CustomerController {
    private final CustomerServiceImpl customerService;
    private final CustomerRepo userRepo;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllUser() {
        List<CustomerDto> customer = customerService.getAllCustomer();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", customer)
        );
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        log.info("INF - Customer created");
        log.debug("DEBUG - Customer debug");
        log.error("ERROR - Customer error");
        log.warn("WARN - Customer warn");
        log.trace("TRACE - Customer trace");
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "Customer Saved Successfully", savedCustomer)
        );
    }

    @PutMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto updatedCustomer = customerService.updateCustomer(customerDto);
        return ResponseEntity.ok(
                new ApiResponseDto(200, "Customer Updated Successfully", updatedCustomer)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Customer deleted successfully", null));
    }


    @GetMapping("/paginated")
    public ResponseEntity<ApiResponseDto> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<CustomerDto> customers = customerService.getCustomersByPage(page, size);
        return ResponseEntity.ok(new ApiResponseDto(200, "OK", customers));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = customerService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

}
