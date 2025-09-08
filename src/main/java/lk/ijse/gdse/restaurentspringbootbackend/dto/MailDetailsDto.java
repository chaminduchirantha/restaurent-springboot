package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailDetailsDto {
    private String toMail;
    private String massage;
    private String subject;
}
