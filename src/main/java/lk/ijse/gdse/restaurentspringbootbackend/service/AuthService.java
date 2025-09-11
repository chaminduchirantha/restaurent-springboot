package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.AuthDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.AuthResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.RegisterDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Role;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepo userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    public AuthResponseDto authenticate(AuthDto authDto) {
        Customer user = userRepo.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        String token = jwtUtil.generateToken(authDto.getUsername());
        System.out.println("Login successful for user: " + authDto.getUsername());
        System.out.println(" Role: " + user.getRole());

        return new AuthResponseDto(token);
    }

    public String register(RegisterDto registerDTO) {
        if (userRepo.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already exist");
        }

        if (userRepo.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Customer user = Customer.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .role(Role.valueOf(registerDTO.getRole()))
                .build();
        userRepo.save(user);
        return "User registered successfully";
    }

    public void generateAndSendOtp(String email) {
        Customer user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email not found."));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digit OTP
        otpStore.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5)); // OTP is valid for 5 minutes

        // Send email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your Password Reset OTP Code");
            message.setText("Your OTP code is: " + otp + ". This code is valid for 5 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            // In a real app, you should log this error
            throw new RuntimeException("Failed to send OTP email.");
        }
    }

    public boolean verifyOtp(String email, String otp) {
        if (!otpStore.containsKey(email) || !otpStore.get(email).equals(otp)) {
            return false;
        }
        return otpExpiry.get(email).isAfter(LocalDateTime.now());
    }

    public boolean resetPassword(String email, String otp, String newPassword) {
        if (verifyOtp(email, otp)) {
            Customer user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);

            // Invalidate the OTP after use
            otpStore.remove(email);
            otpExpiry.remove(email);

            return true;
        }
        return false;
    }
}
