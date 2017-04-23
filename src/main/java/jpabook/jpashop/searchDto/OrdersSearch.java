package jpabook.jpashop.searchDto;

import jpabook.jpashop.domain.OrderStatus;

/**
 * Created by hyun on 2017-01-15.
 */
public class OrdersSearch {

    private String memberName;

    private OrderStatus orderStatus;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
