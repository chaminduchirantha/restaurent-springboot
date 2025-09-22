package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("role")
@CrossOrigin(origins = "*")
public class CheckRole {
    private final CustomerRepo customerRepo;

    public CheckRole(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @GetMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin(){
        return "Hello World Admin";
    }


    @GetMapping("/api/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN");


        Optional<Customer> customerOptional = customerRepo.findByUsername(username);
        Long customerId = customerOptional.map(Customer::getId).orElse(null);

        return ResponseEntity.ok(Map.of(
                "username", username,
                "role", role,
                "customerId", customerId
        ));
    }
}
