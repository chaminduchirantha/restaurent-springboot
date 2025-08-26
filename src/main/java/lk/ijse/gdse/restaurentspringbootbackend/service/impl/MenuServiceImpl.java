package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Menus;
import lk.ijse.gdse.restaurentspringbootbackend.repo.MenuRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.MenuService;
import lk.ijse.gdse.restaurentspringbootbackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final ModelMapper modelMapper;
    private final MenuRepo menuRepo;

    @Override
    public int addMenu(MenuDto menuDto) {
        if(menuRepo.existsByName(menuDto.getName())) {
            return VarList.Not_Acceptable;
        } else {
            try {
                menuRepo.save(modelMapper.map(menuDto, Menus.class));
                return VarList.Created;
            } catch (Exception e) {
                return VarList.Bad_Gateway;
            }
        }
    }
}
