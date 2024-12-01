package wkz.org.backend.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.Book;

import java.util.List;

@Service
public interface BookService {
    Book getBookById(Long bookId);

    List<Book> getBooks();

    List<Book> findByBookNameContaining(String bookName);

    List<Book> findByCategory(String category);

    void checkBookInventory(Long bookId, Integer quantity);

    void updateInventory(JSONArray orderInfo);

    void deleteBook(Long bookId);

    void saveBook(Book book);

//    JSONObject toJson(Book book);
//
//    JSONArray toJson(List<Book> books);
}
