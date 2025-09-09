package lk.ijse.gdse.restaurentspringbootbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableBookingDto{
    private Long tableid;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String orderDatetime;
    private String time;
    private String guests;
    private String tables;
    private String requests;
    private Long customerId;
}
