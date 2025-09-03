package lk.ijse.gdse.restaurentspringbootbackend.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long menuid;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
}
