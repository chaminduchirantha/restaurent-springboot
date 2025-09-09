package lk.ijse.gdse.restaurentspringbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CateringServiceDto {
    private Long cateringServiceId;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String date;
    private String time;
    private String type;
    private String guests;
    private Long customerId;
}
