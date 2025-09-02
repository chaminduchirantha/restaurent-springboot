package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.MenuService;
import lk.ijse.gdse.restaurentspringbootbackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:63342")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials="true")
public class MenuController {

    private final MenuService menuService;
        @PostMapping(value = "/addItem", consumes = {"multipart/form-data"})
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponseDto> saveItem(@RequestPart("item") MenuDto menuDto,
                                                    @RequestPart("file") MultipartFile[] multipartFiles) throws IOException {
            try {
                List<String> fileNames = new ArrayList<>();
                Path uploadDir = Paths.get("uploads/");
                Files.createDirectories(uploadDir);

                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                    Path filePath = uploadDir.resolve(fileName);
                    Files.write(filePath, multipartFile.getBytes());
                    fileNames.add(fileName);
                }
                menuDto.setImageUrls(fileNames);

                int response = menuService.addItem(menuDto);
                if (response == VarList.Created) {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ApiResponseDto(VarList.Created, "Menu saved", menuDto));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ApiResponseDto(VarList.Bad_Gateway, "Error While Saving", null));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponseDto(VarList.Internal_Server_Error, "file Upload Failed" + e.getMessage(), null));


            }
        }

    @GetMapping("/getAllMenus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> getAllItems(){
        try{
            List<MenuDto> menus = menuService.getAllMenu();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseDto(VarList.OK,"suceess", menus));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping(value = "/updateItem", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> updateItem(@RequestPart("item") MenuDto menuDto,
                                                     @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) {
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                List<String> fileNames = new ArrayList<>();
                Path uploadDir = Paths.get("uploads/");
                Files.createDirectories(uploadDir);

                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                    Path filePath = uploadDir.resolve(fileName);
                    Files.write(filePath, multipartFile.getBytes());
                    fileNames.add(fileName);
                }
                menuDto.setImageUrls(fileNames); // new image(s)
            }

            int response = menuService.updateItem(menuDto);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponseDto(VarList.OK, "Menu updated successfully", menuDto));
            } else if (response == VarList.Not_Found) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDto(VarList.Not_Found, "Menu not found", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ApiResponseDto(VarList.Bad_Gateway, "Error while updating", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(VarList.Internal_Server_Error, "Update failed: " + e.getMessage(), null));
        }
    }

}



//    @GetMapping
//    public List<MenuItem> getAllMenuItems() {
//        return repo.findAll();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
//        repo.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
