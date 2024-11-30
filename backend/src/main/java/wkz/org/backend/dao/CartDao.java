package wkz.org.backend.dao;

import wkz.org.backend.entity.CartItem;

import java.util.List;

public interface CartDao {
    CartItem findByUserIdAndBookId(Long userId, Long bookId);

    List<CartItem> findByUserId(Long userId);

    void save(CartItem cartItem);

    void delete(CartItem cartItem);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
