package wkz.org.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wkz.org.backend.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
