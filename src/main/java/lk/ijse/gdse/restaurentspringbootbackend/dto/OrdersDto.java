package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto {
    private Long orderId;
    private String name;
    private double price;
    private String orderType;
    private int orderQty;
    private Date orderDatetime;
    private String status;
    private String notes;
    private Long customerId;
}
