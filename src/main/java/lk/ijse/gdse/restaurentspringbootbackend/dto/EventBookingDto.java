package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventBookingDto {
    private Long eventId;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String date;
    private String time;
    private String duration;
    private String services;
    private String hallNo;
    private String requests;
    private Long customerId;
}
