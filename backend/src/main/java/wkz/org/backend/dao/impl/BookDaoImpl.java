package wkz.org.backend.dao.impl;

import java.util.ArrayList;
import java.util.Optional;
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
import wkz.org.backend.entity.BookDescription;
import wkz.org.backend.entity.Category;
import wkz.org.backend.repository.BookDescriptionRepository;
import wkz.org.backend.repository.BookRepository;

import java.util.List;
import wkz.org.backend.repository.CategoryRepository;

@Repository
public class BookDaoImpl implements BookDao {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BookDescriptionRepository bookDescriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Cacheable(value = "book", key = "#id")
    public Book findOne(Long id) {
        System.out.println("dao findOne");
        logger.info("...Fetching without cache: " + id);

//        return bookRepository.findById(id).orElse(null);

        Book book = bookRepository.findById(id).orElse(null);
        if(book != null){
            Optional<BookDescription> bookDescription = bookDescriptionRepository.findById(id);
					bookDescription.ifPresent(
							description -> book.setDescription(description.getDescription()));
          System.out.println("bookDescription: " + book.getDescription());
        }

        return book;
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

    public List<Book> findByCategory(String category, Pageable pageable) {
        List<Category> categories = categoryRepository.findWithinTwoSubCategories(category);
        List<Book> books = new ArrayList<>();
        for (Category c : categories) {
            books.addAll(bookRepository.findByCategory(c.getTag(), pageable).getContent());
        }
        return books;
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
        // 构造BookDescription

        Long id = book.getId();
        String description = book.getDescription();
        BookDescription bookDescription = new BookDescription(id, description);
        bookDescriptionRepository.save(bookDescription);
    }

    @CacheEvict(value = "book", key = "#id")
    public void delete(Long id) {
        bookRepository.deleteById(id);

        bookDescriptionRepository.deleteById(id);
    }
}
