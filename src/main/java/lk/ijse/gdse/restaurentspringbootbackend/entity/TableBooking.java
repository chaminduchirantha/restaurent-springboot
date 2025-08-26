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
public class TableBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableid;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String date;
    private String time;
    private String guests;
    private String tables;
    private String requests;

    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;
}
