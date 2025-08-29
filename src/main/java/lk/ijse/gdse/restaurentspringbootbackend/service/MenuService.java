package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;

import java.util.List;

public interface MenuService{
    int addItem(MenuDto menuDto);
    List<MenuDto> getAllMenu();
}
