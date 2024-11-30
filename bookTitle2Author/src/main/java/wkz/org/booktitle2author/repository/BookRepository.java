package wkz.org.booktitle2author.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wkz.org.booktitle2author.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b.author from Book b where b.title = ?1")
    Page<String> findAuthorByTitle(String title, Pageable pageable);
}
