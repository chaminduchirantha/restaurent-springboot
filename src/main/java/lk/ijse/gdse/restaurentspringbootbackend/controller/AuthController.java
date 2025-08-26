package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.AuthDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.AuthResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.RegisterDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63343")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> registerUser(
            @RequestBody RegisterDto registerDTO) {
        return ResponseEntity.ok(
                new ApiResponseDto(
                        200,
                        "User registered successfully",
                        authService.register(registerDTO)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthDto authDTO) {
        try {
            AuthResponseDto auth = authService.authenticate(authDTO);

            String token = auth.getAccessToken();

            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)              // JS access karanna ba
                    .secure(false)               // dev walata false, prod walata true
                    .path("/")                   // api okkoma path walata valid
                    .maxAge(24 * 60 * 60)        // 1 day
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", jwtCookie.toString())
                    .body(new ApiResponseDto(200, "OK", auth));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDto(401, "Invalid username or password", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(500, "Server error", null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDto> forgotPassword(@RequestParam String email) {
        try {
            String otp = authService.generateAndSendOtp(email);
            return ResponseEntity.ok(new ApiResponseDto(200, "OTP sent to email", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(400, e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDto> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = authService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok(new ApiResponseDto(200, "OTP verified", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto(400, "Invalid or expired OTP", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto> resetPassword(@RequestParam String email,
                                                        @RequestParam String otp,
                                                        @RequestParam String newPassword) {
        boolean success = authService.resetPassword(email, otp, newPassword);
        if (success) {
            return ResponseEntity.ok(new ApiResponseDto(200, "Password reset successful", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto(400, "Invalid OTP or email", null));
    }


//    @GetMapping("/users")
//    public ResponseEntity<ApiResponse> getAllUsers() {
//        try {
//            List<User> users = authService.getAllUsers();
//            return ResponseEntity.ok(new ApiResponse(200, "Users fetched successfully", users));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse(500, "Server error", null));
//        }
//    }
}
