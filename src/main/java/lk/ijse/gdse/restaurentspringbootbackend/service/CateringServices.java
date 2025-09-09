package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CateringServiceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CateringServices {
    CateringServiceDto createBooking(CateringServiceDto cateringServiceDto);
    List<CateringServiceDto> ggetAllCaterings();
}
