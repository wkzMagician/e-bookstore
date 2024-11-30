package wkz.org.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wkz.org.backend.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.orderDate BETWEEN :startDate AND :endDate AND (:filterWord IS NULL OR :filterWord = '' OR EXISTS (SELECT 1 FROM OrderItem oi JOIN oi.book b WHERE oi.order = o AND b.title LIKE %:filterWord% ))" )
    List<Order> findByUserIdAndBookTitleContainingAndOrderDateBetween(Long userId, String filterWord, Date startDate, Date endDate);

    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND (:filterWord IS NULL OR :filterWord = '' OR EXISTS (SELECT 1 FROM OrderItem oi JOIN oi.book b WHERE oi.order = o AND b.title LIKE %:filterWord% ))" )
    List<Order> findByBookTitleContainingAndOrderDateBetween(String filterWord, Date startDate, Date endDate);
}
