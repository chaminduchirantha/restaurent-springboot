package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.ApiResponseDto;
import lk.ijse.gdse.restaurentspringbootbackend.dto.MenuDto;
import lk.ijse.gdse.restaurentspringbootbackend.service.MenuService;
import lk.ijse.gdse.restaurentspringbootbackend.service.impl.FileServiceImpl;
import lk.ijse.gdse.restaurentspringbootbackend.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MenuController {

    private final MenuService menuService;
    private final FileServiceImpl fileUploadService;

    @PostMapping(value = "/addMenuItem", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseDto> addMenuItem(
            @RequestPart("menuItem") MenuDto menuDto,
            @RequestPart("file") MultipartFile[] multipartFiles) throws IOException, IOException {

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
            menuDto.setImageUrl(fileNames.toString());

            int response = menuService.addMenu(menuDto);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponseDto(VarList.Created, "Workshop Saved", menuDto));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ApiResponseDto(VarList.Bad_Gateway, "Error While Saving", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


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
}
