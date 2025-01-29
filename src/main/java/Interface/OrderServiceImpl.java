package Interface;

import model.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderServiceImpl {
    Order saveOrder(Order order);
    List<Order> getOrdersByUserId(Long userId,  HttpServletRequest req);
    List<Order> getAllOrders( HttpServletRequest req);
}
