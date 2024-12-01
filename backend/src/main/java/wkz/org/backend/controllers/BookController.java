package wkz.org.backend.controllers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import wkz.org.backend.entity.Book;
import wkz.org.backend.services.BookService;
import wkz.org.backend.utils.NetworkUtil;
import wkz.org.backend.utils.FileUtil;

import java.util.Iterator;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping("/home")
    public String home(@RequestParam String content) {
        logger.info("...Fetching book by content: " + content);
        long startTime = System.currentTimeMillis();

        // 从数据库查找书籍信息
        List<Book> books;
        try{
            books = bookService.findByBookNameContaining(content);
        }catch (Exception e){
            e.printStackTrace();
            return NetworkUtil.response("error", "查找书籍失败");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms");
        logger.info("...Got book by content: " + content);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok","书籍查询成功", books);
    }

    @GetMapping("/book/category")
    public String category(@RequestParam String content) {
        logger.info("...Fetching book by category: " + content);
        long startTime = System.currentTimeMillis();

        // 从数据库查找书籍信息
        List<Book> books;
        try{
            books = bookService.findByCategory(content);
        }catch (Exception e){
            e.printStackTrace();
            return NetworkUtil.response("error", "查找书籍失败");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms");
        logger.info("...Got book by category: " + content);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok","书籍查询成功", books);
    }


    @GetMapping("/book/{bookId}")
    public String getBookById(@PathVariable Long bookId) {
        logger.info("...Fetching book by id: " + bookId);
        long startTime = System.currentTimeMillis();

        // 从数据库查找特定ID的书籍信息
        Book book = null;
        try{
            book = bookService.getBookById(bookId);
        }catch (Exception e){
            e.printStackTrace();
            return NetworkUtil.response("error", "查找书籍失败");
        }


        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms");
        logger.info("...Got book by id: " + bookId);

        // 没有找到？
        if (book == null) {
            System.out.println("book is null");
            // 格式: {code:, message:, data:[]}
            return NetworkUtil.response("error", "书籍不存在");
        }

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "书籍查询成功", book);
    }

    @PostMapping("/admin/book/delete")
    public String delete(@RequestBody JSONObject data) {
        Long bookId = data.getLong("bookId");

        // 删除书籍信息
        bookService.deleteBook(bookId);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "书籍删除成功");
    }

    @PostMapping("/admin/book/save")
    public String save(@RequestBody JSONObject data) {
        System.out.println("data: " + data);

        logger.info("...Saving book: " + data);
        float startTime = System.currentTimeMillis();

        if (data.containsKey("bookId")) {
            Long bookId = data.getLong("bookId");

            if(bookId == -1){
                // 将图片由base64转为文件
                String cover = data.getString("cover");
                // 如果图片路径以 http://localhost:8080/ 开头，说明是本地图片，不需要保存
                if (cover.startsWith("http://localhost:8080/")) {
                    cover = cover.substring(21);
                }else {
                    cover = FileUtil.saveImage(cover);
                }

                Book book = new Book();
                book.setTitle(data.getString("title"));
                book.setAuthor(data.getString("author"));
                book.setPrice(data.getInteger("price"));
                book.setISBN(data.getString("ISBN"));
                book.setInventory(data.getInteger("inventory"));
                book.setCover(cover);
                book.setDescription(data.getString("description"));
                book.setCategory(data.getString("category"));
                bookService.saveBook(book);
                return NetworkUtil.response("ok", "书籍保存成功");
            }

            Book book = bookService.getBookById(bookId);
            if (book == null) {
                // 格式: {code:, message:, data:[]}
                return NetworkUtil.response("error", "书籍不存在");
            }

            // 将图片由base64转为文件
            String cover = data.getString("cover");
            System.out.println("cover: " + cover);
            // 如果图片路径以 http://localhost:8080/ 开头，说明是本地图片，不需要保存
            if (cover.startsWith("http://localhost:8080/")) {
                cover = cover.substring(21);
            }else {
                cover = FileUtil.saveImage(cover);
            }

            // 设置书籍信息
            // data: {"bookId":1,"title":"神经网络与机器学习","author":"（美）Simon Haykin","price":7200,"ISBN":"9787111324133","inventory":"200","cover":"/bookImage/1.jpg"}
            book.setTitle(data.getString("title"));
            book.setAuthor(data.getString("author"));
            book.setPrice(data.getInteger("price"));
            book.setISBN(data.getString("ISBN"));
            book.setInventory(data.getInteger("inventory"));
            book.setCover(cover);
            book.setDescription(data.getString("description"));
            book.setCategory(data.getString("category"));

            System.out.println("book: " + book);

            // 保存书籍信息
            bookService.saveBook(book);
        } else {
            return NetworkUtil.response("error", "不含有bookId");
        }

        float endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms");
        logger.info("...Saved book: " + data);

        // 格式: {code:, message:, data:[]}
        return NetworkUtil.response("ok", "书籍保存成功");
    }
}
