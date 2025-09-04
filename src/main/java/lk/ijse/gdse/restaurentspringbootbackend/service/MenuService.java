package lk.ijse.gdse.restaurentspringbootbackend.service;

import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService{
    void saveMenu(MenuDto menuDto);
    void updateMenu(MenuDto menuDto);
    List<MenuDto> getAllCustomer();
    void deleteCustomer(Long id);
    int getTotalPages(int size);
    List<MenuDto> getMenuByPage(int page, int size);
    List<MenuDto> searchMenus(String keyword);
}
