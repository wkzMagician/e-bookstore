package wkz.org.backend.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wkz.org.backend.dao.OrderItemDao;
import wkz.org.backend.entity.OrderItem;
import wkz.org.backend.repository.OrderItemRepository;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {
	@Autowired
	private OrderItemRepository orderItemRepository;

		@Override
		@Transactional(propagation = Propagation.REQUIRED)
		public void save(OrderItem orderItem) {
//			int a = 0 / 0;
			orderItemRepository.save(orderItem);
		}

}
