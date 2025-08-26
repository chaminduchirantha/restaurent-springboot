package lk.ijse.gdse.restaurentspringbootbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EventBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(cascade = CascadeType.ALL)
    private Customer user;
}
