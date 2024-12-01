package wkz.org.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wkz.org.backend.entity.Book;

import java.util.List;

public interface BookDao {
    Book findOne(Long id);

    List<Book> findByCategory(String category, Pageable pageable);

    Page<Book> findAll(Pageable pageable);

    List<Book> findByBookNameContaining(String bookName, Pageable pageable);

    void save(Book book);

    void delete(Long id);
}
