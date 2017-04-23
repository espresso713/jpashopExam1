package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.searchDto.OrdersSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hyun on 2017-01-15.
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemService itemService;

    public Long order(Long memberId, Long itemId, int count) {

        Member member = memberService.findOne(memberId);
        Item item = itemService.findOne(itemId);

        Delivery delivery = new Delivery(member.getAddress());
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Orders order = Orders.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        return order.getId();
    }

    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findOne(orderId);

        order.cancel();
    }

    public Orders findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    public List<Orders> findOrders(OrdersSearch ordersSearch) {
        return orderRepository.findAll(ordersSearch);
    }
}
