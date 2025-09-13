package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.NotificationDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Notification;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.NotificationRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final CustomerRepo customerRepo;

    public NotificationDto createNotification(NotificationDto notificationDto) {
        Customer customer = customerRepo.findById(notificationDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + notificationDto.getCustomerId()));

        // Create Notification entity
        Notification notification = new Notification();
        notification.setMessage(notificationDto.getMessage());
        notification.setUsername(notificationDto.getUsername());
        notification.setSeen(notificationDto.isSeen());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setCustomer(customer);

        // Save notification
        notificationRepo.save(notification);

        return notificationDto;
    }


}
