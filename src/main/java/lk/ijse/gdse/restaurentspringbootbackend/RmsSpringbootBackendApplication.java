package lk.ijse.gdse.restaurentspringbootbackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RmsSpringbootBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmsSpringbootBackendApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
