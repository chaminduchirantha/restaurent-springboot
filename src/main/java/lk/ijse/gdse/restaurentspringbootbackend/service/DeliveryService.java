package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.DeliveryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);
    List<DeliveryDto> getAllDelivery();
    List<DeliveryDto> getDeliveryByPage(int page, int size);

    int getTotalPages(int size);
}
