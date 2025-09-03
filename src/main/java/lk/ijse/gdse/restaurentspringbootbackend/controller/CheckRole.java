package lk.ijse.gdse.restaurentspringbootbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("role")
@CrossOrigin(origins = "*")
public class CheckRole {
    @GetMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")   // role based access danw
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
        return ResponseEntity.ok(Map.of(
                "username", username,
                "role", role
        ));
    }
}
