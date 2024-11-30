package wkz.org.backend.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.CartItem;

import java.util.List;

@Service
public interface CartService {
    void addBookToCart(Long userId, Long bookId, Long bookAmount);

    void removeBooksFromCart(Long userId, List<Long> bookIds);

    void removeBooksFromCart(Long userId, JSONArray bookIds);

    void removeBookFromCart(Long userId, Long bookId);

    void updateBookAmount(Long userId, Long bookId, Long bookAmount);

    void clearCart(Long userId);

    List<CartItem> getCartItems(Long userId);

    JSONObject toJson(CartItem cartItem);
//
    JSONArray toJson(List<CartItem> cartItems);
}
