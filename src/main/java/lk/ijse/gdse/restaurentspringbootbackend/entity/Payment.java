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
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private String orderAmount;
    private String cardHolderName;
    private String email;
    private Long cardNumber;
    private String expireDate;
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
