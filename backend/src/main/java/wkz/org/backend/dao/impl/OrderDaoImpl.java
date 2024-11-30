package wkz.org.backend.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wkz.org.backend.dao.OrderDao;
import wkz.org.backend.entity.Order;
import wkz.org.backend.repository.OrderRepository;

import java.util.Date;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private OrderRepository orderRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Order order) {
        //			int a = 0 / 0;

        orderRepository.save(order);
    }

//    @Override
//    public List<Order> findByUserId(Long userId) {
//        return orderRepository.findByUserId(userId);
//    }
//
//
//    @Override
//    public void delete(Order order) {
//    }
//
//    @Override
//    public void deleteById(Long id) {
//    }

    @Override
    public List<Order> findByUserIdAndBookTitleContainingAndOrderDateBetween(Long userId, String filterWord, Date startDate, Date endDate) {
        return orderRepository.findByUserIdAndBookTitleContainingAndOrderDateBetween(userId, filterWord, startDate, endDate);
    }

    @Override
    public List<Order> findByBookTitleContainingAndOrderDateBetween(String filterWord, Date startDate, Date endDate) {
        return orderRepository.findByBookTitleContainingAndOrderDateBetween(filterWord, startDate, endDate);
    }
}
