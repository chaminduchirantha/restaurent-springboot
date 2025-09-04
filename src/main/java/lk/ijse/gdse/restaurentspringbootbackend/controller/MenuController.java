package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.CustomerDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.MenuService;
import lk.ijse.gdse.restaurentspringbootbackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:63342")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials="true")
public class MenuController {

    private final MenuService menuService;
    private final String uploadDir = "uploads/";


    @PostMapping(value = "/addItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addMenuItem(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("file") MultipartFile file // single image
    ) {
        try {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            MenuDto menuDto = new MenuDto();
            menuDto.setName(name);
            menuDto.setCategory(category);
            menuDto.setDescription(description);
            menuDto.setPrice(price);
            menuDto.setImageUrl(fileName);

            menuService.saveMenu(menuDto);

            return ResponseEntity.status(201).body("Menu item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
    }

    @PutMapping(value = "/updateItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> upsertMenuItem(
            @RequestParam(value = "id", required = false) Long menuid, // optional
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam(value = "file", required = false) MultipartFile file // optional image
    ) {
        try {
            MenuDto menuDto=new MenuDto();

            if (menuid != null) {
               menuDto.setMenuid(menuid);
                if (menuDto == null) {
                    menuDto = new MenuDto();
                }
            } else {
                menuDto = new MenuDto();
            }

            menuDto.setName(name);
            menuDto.setCategory(category);
            menuDto.setDescription(description);
            menuDto.setPrice(price);

            if (file != null && !file.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                menuDto.setImageUrl(fileName);
            }

            menuService.updateMenu(menuDto);

            return ResponseEntity.status(200).body("Menu item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Operation failed: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDto> getAllMenu() {
        List<MenuDto> menus = menuService.getAllCustomer();
        return ResponseEntity.ok(
                new ApiResponseDto(200, "OK", menus)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only admin can delete
    public ResponseEntity<ApiResponseDto> deleteMenu(@PathVariable Long id) {
        menuService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponseDto(200, "Menu deleted successfully", null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponseDto> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<MenuDto> customers = menuService.getMenuByPage(page, size);
        return ResponseEntity.ok(new ApiResponseDto(200, "OK", customers));
    }

    @GetMapping("search/{keyword}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDto> searchMenus(@PathVariable("keyword") String keyword) {
        List<MenuDto>menuDtos =  menuService.searchMenus(keyword);
        return ResponseEntity.ok(
                new ApiResponseDto(
                        200,
                        "Menu found Successfully",
                        menuDtos
                )
        );
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = menuService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

}

