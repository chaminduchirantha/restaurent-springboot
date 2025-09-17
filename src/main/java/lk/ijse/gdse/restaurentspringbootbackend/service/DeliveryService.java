package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.DeliveryDto;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);
}
