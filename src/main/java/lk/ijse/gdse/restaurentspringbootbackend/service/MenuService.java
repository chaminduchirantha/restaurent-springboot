package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;

import java.util.List;

public interface MenuService{
    void saveMenu(MenuDto menuDto);
    void updateMenu(MenuDto menuDto);
    List<MenuDto> getAllCustomer();
    void deleteCustomer(Long id);
}
