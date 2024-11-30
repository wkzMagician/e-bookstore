package wkz.org.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.CartItem;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndBookId(Long userId, Long bookId);


    CartItem findByUserIdAndBookId(Long userId, Long bookId);
}
