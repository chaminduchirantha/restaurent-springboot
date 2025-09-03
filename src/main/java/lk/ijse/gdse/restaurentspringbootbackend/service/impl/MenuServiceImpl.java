package lk.ijse.gdse.restaurentspringbootbackend.service.impl;

import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Menus;
import lk.ijse.gdse.restaurentspringbootbackend.repo.MenuRepo;
import lk.ijse.gdse.restaurentspringbootbackend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final ModelMapper modelMapper;
    private final MenuRepo menuRepo;

    @Override
    public void saveMenu(MenuDto menuDto) {
        Menus menu;

        if (menuDto.getMenuid() != null) {
            menu = menuRepo.findById(menuDto.getMenuid())
                    .orElse(new Menus());
        } else {
            menu = new Menus();
        }

        menu.setName(menuDto.getName());
        menu.setCategory(menuDto.getCategory());
        menu.setDescription(menuDto.getDescription());
        menu.setPrice(menuDto.getPrice());
        menu.setImageUrl(menuDto.getImageUrl());

        menuRepo.save(menu);
    }

    @Override
    public void updateMenu(MenuDto menuDto) {
        if (menuDto.getMenuid() == null) {
            saveMenu(menuDto);
            return;
        }

        Menus menu = menuRepo.findById(menuDto.getMenuid())
                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + menuDto.getMenuid()));

        menu.setName(menuDto.getName());
        menu.setCategory(menuDto.getCategory());
        menu.setDescription(menuDto.getDescription());
        menu.setPrice(menuDto.getPrice());
        menu.setImageUrl(menuDto.getImageUrl());

        menuRepo.save(menu);
    }

    @Override
    public List<MenuDto> getAllCustomer() {
        List<Menus>menus = menuRepo.findAll();
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menus menu : menus) {
            menuDtos.add(modelMapper.map(menu, MenuDto.class));
        }
        return menuDtos;
    }

    @Override
    public void deleteCustomer(Long id) {
        Menus menus = menuRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
        menuRepo.delete(menus);
    }

    @Override
    public int getTotalPages(int size) {
        long totalMenus = menuRepo.getTotalMenuCount();
        return (int) Math.ceil((double) totalMenus / size);
    }

    @Override
    public List<MenuDto> getMenuByPage(int page, int size) {
        int offset = page * size;
        List<Menus> menus = menuRepo.findMenuPaginated(size, offset);
        return modelMapper.map(menus, new TypeToken<List<MenuDto>>() {}.getType());
    }

}

