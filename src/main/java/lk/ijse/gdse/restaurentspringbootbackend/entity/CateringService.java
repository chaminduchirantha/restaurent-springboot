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
public class CateringService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateringServiceId;
    private String fullname;
    private String phoneNumber;
    private String date;
    private String time;
    private String type;
    private String guests;

    @OneToOne(cascade = CascadeType.ALL)
    private Customer user;
}
