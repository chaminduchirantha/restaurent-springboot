package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepo extends JpaRepository<Menus, Long> {
    boolean existsByName(String name);
}
