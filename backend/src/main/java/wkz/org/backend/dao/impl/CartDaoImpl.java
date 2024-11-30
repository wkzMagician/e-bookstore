package wkz.org.backend.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import wkz.org.backend.dao.CartDao;
import wkz.org.backend.entity.CartItem;
import wkz.org.backend.repository.CartRepository;

import java.util.List;

@Repository
public class CartDaoImpl implements CartDao {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartItem findByUserIdAndBookId(Long userId, Long bookId) {
        return cartRepository.findByUserIdAndBookId(userId, bookId);
    }

    @Override
    @Cacheable(value = "cart", key = "#userId")
    public List<CartItem> findByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    @Override
    @CacheEvict(value = "cart", key = "#cartItem.userId")
    public void save(CartItem cartItem) {
        cartRepository.save(cartItem);
    }

    @Override
    @CacheEvict(value = "cart", key = "#cartItem.userId")
    public void delete(CartItem cartItem) {
        cartRepository.delete(cartItem);
    }

    @Override
    @CacheEvict(value = "cart", key = "#userId")
    public void deleteByUserId(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    @Override
    @CacheEvict(value = "cart", key = "#userId")
    public void deleteByUserIdAndBookId(Long userId, Long bookId) {
        cartRepository.deleteByUserIdAndBookId(userId, bookId);
    }
}
