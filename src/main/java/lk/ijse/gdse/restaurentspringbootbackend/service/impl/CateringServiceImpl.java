package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CateringServiceDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.CateringService;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CateringServiceRepo;
import lk.ijse.gdse.restaurentspringbootbackend.repo.CustomerRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.CateringServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CateringServiceImpl implements CateringServices {

    private final CustomerRepo customerRepo;
    private final ModelMapper modelMapper;
    private final CateringServiceRepo cateringServiceRepo;
    @Override
    public CateringServiceDto createBooking(CateringServiceDto cateringServiceDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CateringService cateringService = modelMapper.map(cateringServiceDto, CateringService.class);
        cateringService.setCustomer(customer);

        CateringService saveBooking = cateringServiceRepo.save(cateringService);

        CateringServiceDto saveCateringDto = modelMapper.map(saveBooking, CateringServiceDto.class);
        saveCateringDto.setCustomerId(customer.getId());

        return saveCateringDto;
    }

    @Override
    public List<CateringServiceDto> ggetAllCaterings() {
        List<CateringService> cateringServices = cateringServiceRepo.findAll();
        List<CateringServiceDto> cateringServiceDtos = new ArrayList<>();
        for (CateringService cateringService : cateringServices) {
            cateringServiceDtos.add(modelMapper.map(cateringService, CateringServiceDto.class));
        }
        return cateringServiceDtos;
    }
}
