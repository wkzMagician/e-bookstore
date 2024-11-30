package wkz.org.backend.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wkz.org.backend.dao.CartDao;
import wkz.org.backend.entity.Book;
import wkz.org.backend.entity.CartItem;
import wkz.org.backend.services.BookService;
import wkz.org.backend.services.CartService;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BookService bookService;

    @Autowired
    CartDao cartDao;
    public void addBookToCart(Long userId, Long bookId, Long bookAmount) {
//        // 插入订单信息
//        String sql = "INSERT INTO `cart` (user_id, book_id, book_amount) VALUES (?, ?, ?)";
//        try{
//            jdbcTemplate.update(sql, userId, bookId, bookAmount);
//        } catch (Exception e) {
//            // 如果是因为主键冲突，那么就更新数量
//            if(e.getMessage().contains("Duplicate entry")) {
//                sql = "UPDATE `cart` SET book_amount = book_amount + ? WHERE user_id = ? AND book_id = ?";
//                jdbcTemplate.update(sql, bookAmount, userId, bookId);
//            } else {
//                e.printStackTrace();
//            }
//        }
        CartItem cartItem = cartDao.findByUserIdAndBookId(userId, bookId);
        if (cartItem == null) {
            cartItem = new CartItem(userId, bookId, bookAmount);
            cartDao.save(cartItem);
        } else {
            cartItem.setBookAmount(cartItem.getBookAmount() + bookAmount);
            cartDao.save(cartItem);
        }
    }

    public void removeBooksFromCart(Long userId, List<Long> bookIds) {
        for (Long bookId : bookIds) {
            removeBookFromCart(userId, bookId);
        }
    }

    public void removeBooksFromCart(Long userId, JSONArray bookIds) {
        for (int i = 0; i < bookIds.size(); i++) {
            Long bookId = bookIds.getJSONObject(i).getLong("bookId");
            removeBookFromCart(userId, bookId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeBookFromCart(Long userId, Long bookId) {
        // 使用jpa
        cartDao.deleteByUserIdAndBookId(userId, bookId);
    }

    public void updateBookAmount(Long userId, Long bookId, Long bookAmount) {
        // 使用jpa
        CartItem cartItem = cartDao.findByUserIdAndBookId(userId, bookId);
        cartItem.setBookAmount(bookAmount);
        cartDao.save(cartItem);
    }

    public void clearCart(Long userId) {
        // 使用jpa
        cartDao.deleteByUserId(userId);
    }

    public List<CartItem> getCartItems(Long userId) {
        // 使用jpa
        return cartDao.findByUserId(userId);
    }

    public JSONObject toJson(CartItem cartItem) {
        JSONObject result = new JSONObject();
        result.put("amount", cartItem.getBookAmount());
        Book book = cartItem.getBook();
        result.put("id", book.getId());
        result.put("cover", book.getCover());
        result.put("title", book.getTitle());
        result.put("price", book.getPrice());
        return result;
    }

    public JSONArray toJson(List<CartItem> cartItems) {
        JSONArray result = new JSONArray();
        for (CartItem cartItem : cartItems) {
            result.add(toJson(cartItem));
        }
        return result;
    }
}
