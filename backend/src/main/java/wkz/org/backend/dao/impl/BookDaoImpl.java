package wkz.org.backend.dao.impl;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wkz.org.backend.controllers.BookController;
import wkz.org.backend.dao.BookDao;
import wkz.org.backend.entity.Book;
import wkz.org.backend.repository.BookRepository;

import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Cacheable(value = "book", key = "#id")
    public Book findOne(Long id) {
        logger.info("...Fetching without cache: " + id);

        return bookRepository.findById(id).orElse(null);
    }

    public Page<Book> findAll(Pageable pageable) {
        // 首先检查是否有缓存
        // 如果有缓存，直接返回缓存数据
        // 如果没有缓存，查询数据库，并将查询结果缓存
        String cacheKey = "books:" + pageable.getPageNumber();
        try{
            boolean hasKey = redisTemplate.hasKey(cacheKey);
            if (hasKey) {
                List<Long> ids = (List<Long>) redisTemplate.opsForValue().get(cacheKey);
                List<Book> books = new ArrayList<>();
                for (Long id : ids) {
                    books.add((Book) redisTemplate.opsForValue().get("book:" + id));
                }
                return new PageImpl<>(books, pageable, books.size());
            }else{
                Page<Book> books = bookRepository.findAll(pageable);
                // 将books每一项的id存入缓存
                ArrayList<Long> ids = new ArrayList<>();
                for (Book book : books) {
                    ids.add(book.getId());
                    redisTemplate.opsForValue().set("book:" + book.getId(), book);
                }
                redisTemplate.opsForValue().set(cacheKey, ids);
                return books;
            }
        }catch (Exception e){
            e.printStackTrace();
            // 直接从数据库查找
            return bookRepository.findAll(pageable);
        }

    }

    public List<Book> findByCategory(String category) {
        return null;
    }

    public List<Book> findByBookNameContaining(String bookName, Pageable pageable) {
        String cacheKey = "bookName:" + (bookName != null ? bookName : "unknown") + "_" + pageable.getPageNumber() + "_" + pageable.getPageSize();
        try{
            boolean hasKey = redisTemplate.hasKey(cacheKey);

            if (hasKey) {
                logger.info("...Fetching with cache: " + bookName);
                List<Long> ids = (List<Long>) redisTemplate.opsForValue().get(cacheKey);
                List<Book> books = new ArrayList<>();
                for (Long id : ids) {
                    books.add((Book) redisTemplate.opsForValue().get("book:" + id));
                }
                return books;
            }else{
                logger.info("...Fetching without cache: " + bookName);
                List<Book> books = bookRepository.findByTitleContaining(bookName, pageable).getContent();
                ArrayList<Long> ids = new ArrayList<>();
                for (Book book : books) {
                    ids.add(book.getId());
                    redisTemplate.opsForValue().set("book:" + book.getId(), book);
                }
                redisTemplate.opsForValue().set(cacheKey, ids);
                return books;
            }
        }catch (Exception e){
            e.printStackTrace();
            // 直接从数据库查找
            return bookRepository.findByTitleContaining(bookName, pageable).getContent();
        }

    }

    @CacheEvict(value = "book", key = "#book.id")
    public void save(Book book) {
        bookRepository.save(book);
    }

    @CacheEvict(value = "book", key = "#id")
    public void delete(Long id) {
        bookRepository.deleteById(id);

    }
}
