package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private String orderAmount;
    private String cardHolderName;
    private String email;
    private Long cardNumber;
    private String expireDate;
    private String cvv;
    private Long orderId;
}
