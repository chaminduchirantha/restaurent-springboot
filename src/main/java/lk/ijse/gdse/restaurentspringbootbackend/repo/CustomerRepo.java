package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
    Optional<Customer>existsByUsername(String userEmail);
    Optional<Customer> findByEmail(String username);
    @Query(value = "SELECT * FROM customer LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Customer> findCustomerPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM customer", nativeQuery = true)
    long getTotalCustomerCount();
}
