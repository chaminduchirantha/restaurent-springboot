package lk.ijse.gdse.restaurentspringbootbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;


    private String name;
    private double price;
    private String email;
    private String orderType;
    private int orderQty;
    private Date orderDatetime;
    private String status;
    private double total;
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Payment> payments;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Delivery> deliveries;
}
