package wkz.org.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wkz.org.backend.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    // 根据书名查找书籍
    Page<Book> findByTitleContaining(String bookName, Pageable pageable);

    // 根据类别
    Page<Book> findByCategory(String category, Pageable pageable);
}
