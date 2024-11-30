package wkz.org.backend.dao;

import org.springframework.stereotype.Repository;
import wkz.org.backend.entity.Order;

import java.util.Date;
import java.util.List;

public interface OrderDao {
    void save(Order order);

//    void delete(Order order);
//    void deleteById(Long id);
//
//    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndBookTitleContainingAndOrderDateBetween(Long userId, String filterWord, Date startDate, Date endDate);

    List<Order> findByBookTitleContainingAndOrderDateBetween(String filterWord, Date startDate, Date endDate);
}
