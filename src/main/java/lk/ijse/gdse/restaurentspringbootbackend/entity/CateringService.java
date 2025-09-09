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
    private String email;
    private String date;
    private String time;
    private String type;
    private String guests;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
