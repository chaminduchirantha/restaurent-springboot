package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackDto {
    private Long id;
    private String fullname;
    private String email;
    private String services;
    private String ratings;
    private String message;
    private Long customerId;

    private String customerUsername;
    private String customerEmail;
}

