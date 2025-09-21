package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.NotificationDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class NotificationController {

    private final NotificationService notificationService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto> createNotification(@RequestBody NotificationDto notificationDto) {
        NotificationDto notificationDto1 = notificationService.createNotification(notificationDto);
        return ResponseEntity.ok(
                new ApiResponseDto(201, "Notification Saved Successfully", notificationDto1)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

}
