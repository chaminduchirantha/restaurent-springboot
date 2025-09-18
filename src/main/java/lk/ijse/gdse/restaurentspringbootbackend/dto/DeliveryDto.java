package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private Long deliveryId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String date;
    private String time;
    private Long orderId;

    private Double latitude;
    private Double longitude;
}
