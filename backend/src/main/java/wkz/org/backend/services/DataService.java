package wkz.org.backend.services;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import wkz.org.backend.entity.Order;

import java.util.List;

@Service
public interface DataService {
    JSONObject getStatistics(List<Order> orders);

    Object getSelling(List<Order> orders);

    Object getConsuming(List<Order> orders);
}
