package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;


@Repository
public interface MenuRepo extends JpaRepository<Menus, Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM menuitem LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Menus> findMenuPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM menuitem", nativeQuery = true)
    long getTotalMenuCount();

}
