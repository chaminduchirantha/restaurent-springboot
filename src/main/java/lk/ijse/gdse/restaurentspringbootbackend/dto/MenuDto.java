package lk.ijse.gdse.restaurentspringbootbackend.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
}
