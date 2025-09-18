package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Delivery;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, Long> {
    @Query(value = "SELECT * FROM delivery LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Delivery> findDeliveryPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM delivery", nativeQuery = true)
    long getTotalDeliveryCount();
}
