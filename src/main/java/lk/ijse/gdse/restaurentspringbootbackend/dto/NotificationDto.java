package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String message;
    private String username;
    private boolean seen = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long customerId;
}
