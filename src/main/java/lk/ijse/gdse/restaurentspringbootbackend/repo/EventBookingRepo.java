package lk.ijse.gdse.restaurentspringbootbackend.repo;

import lk.ijse.gdse.restaurentspringbootbackend.entity.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventBookingRepo extends JpaRepository<EventBooking , Long> {
}
