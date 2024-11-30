package wkz.org.backend.services.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import wkz.org.backend.dao.BookDao;
import wkz.org.backend.entity.Book;
import wkz.org.backend.services.BookService;

import java.util.Iterator;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    static final int PAGE_SIZE = 100;

    @Autowired
    private BookDao bookDao;

    @Override
    public Book getBookById(Long bookId) {

        return bookDao.findOne(bookId);
    }

    @Override
    public List<Book> getBooks() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        return bookDao.findAll(pageable).getContent();
    }

    @Override
    public List<Book> findByBookNameContaining(String bookName) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        return bookDao.findByBookNameContaining(bookName, pageable);
    }

    @Override
    public void checkBookInventory(Long bookId, Integer quantity) {
        Book book = bookDao.findOne(bookId);
        if (book.getInventory() < quantity) {
            throw new RuntimeException("库存不足");
        }
    }

    @Override
    public void updateInventory(JSONArray orderInfo) {
        for (Object o : orderInfo) {
            JSONObject order = (JSONObject) o;
            Long bookId = order.getLong("bookId");
            Integer quantity = order.getInteger("bookAmount");
            Book book = bookDao.findOne(bookId);
            book.setInventory(book.getInventory() - quantity);
            bookDao.save(book);
        }
    }

    @Override
    public void deleteBook(Long bookId) {
        bookDao.delete(bookId);
    }

    @Override
    public void saveBook(Book book) {
        bookDao.save(book);
    }

//    @Override
//    public JSONObject toJson(Book book) {
//        JSONObject bookjson = new JSONObject();
//        bookjson.put("id", book.getId());
//        bookjson.put("cover", book.getCover());
//        bookjson.put("title", book.getTitle());
//        bookjson.put("author", book.getAuthor());
//        bookjson.put("price", book.getPrice());
//        bookjson.put("category", book.getCategory());
//        bookjson.put("description", book.getDescription());
//        return bookjson;
//    }
//
//    @Override
//    public JSONArray toJson(List<Book> books) {
//        JSONArray booksJson = new JSONArray();
//        Iterator<Book> it = books.iterator();
//        while (it.hasNext()) {
//            Book book = it.next();
//            JSONObject bookJson = toJson(book);
//            booksJson.add(bookJson);
//        }
//        return booksJson;
//    }
}
