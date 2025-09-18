package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Order;
import lk.ijse.gdse.restaurentspringbootbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order , Long> {
    @Query(value = "SELECT * FROM orders LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> findOrderPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
    long getTotalOrdersCount();
    List<Order> findByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "UPDATE orders SET status='complete' WHERE order_id = :id", nativeQuery = true)
    void updateStatus(@Param("id") Long id);

    Optional<Object> findTopByCustomer_UsernameOrderByOrderDatetimeDesc(String username);
}
