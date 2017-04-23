package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

/**
 * Created by hyun on 2017-01-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appConfig.xml")
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    private Member member;

    private Item item;

    @Before
    public void setUp() throws Exception {
        member = createMember();
        item = createBook("시골 jpa", 10000, 10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));

        em.persist(member);

        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);

        em.persist(book);

        return book;
    }

    @Test
    public void order() throws Exception {

        //given
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Orders order = orderService.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getOrderStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, order.getOrderItems().size());
        assertEquals("주무가격은 가격 * 수량이다.", 10000 * 2 , order.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void NotEnoughStockQuantity() throws Exception {
        //given
       int orderCount = 11;

       //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량부족 예외가 발생해야 한다.");

    }

    @Test
    public void cancel() {
        //given
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Orders order = orderService.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL이다", OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());

    }

}