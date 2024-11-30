package wkz.org.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
@Table(name = "`order`")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "orderId")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP) // 添加这个注解，指定映射到数据库的具体日期时间类型
    private Date orderDate;

    @Column(name = "total_price")
    private Long totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"}, allowGetters = true) // 允许读，不允许写
    private List<OrderItem> orderItems;


    public Order(Long userId, Date orderDate) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderItems = new ArrayList<>();
        this.totalPrice = 0L;
    }

    public Order() {
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        this.totalPrice += orderItem.getBookAmount() * orderItem.getBook().getPrice();
    }
    
}
