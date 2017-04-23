package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class Orders {

	@Id
	@GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	public static Orders createOrder(Member member, Delivery delivery, OrderItem... orderItems){
		Orders order = new Orders();

		order.setMember(member);
		order.setDelivery(delivery);

		for ( OrderItem orderItem : orderItems ) {
			order.addOrderItem(orderItem);
		}

		order.setOrderStatus(OrderStatus.ORDER);
		order.setOrderDate(new Date());

		return order;
	}

	public void cancel(){
		if( delivery.getStatus() == DeliveryStatus.COMP ) {
			throw new RuntimeException("이미 배송된 상품은 취소가 불가능합니다.");
		}

		this.setOrderStatus(OrderStatus.CANCEL);
		for ( OrderItem orderItem :  orderItems ) {
			orderItem.cancel();
		}
	}

	public int getTotalPrice() {
		int totalPrice = 0;

		for ( OrderItem orderItem :  orderItems ) {
			totalPrice += orderItem.getTotalPrice();
		}

		return totalPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	// 양방향 관계 패턴(order - delivery)
	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {

		if( this.delivery != null ){
			this.delivery.internalSetOrder(null);
		}

		this.delivery = delivery;

		if( this.delivery != null ){
			this.delivery.internalSetOrder(this);
		}

	}


	// 양방향 관계 패턴(member - order)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		if( this.member != null ){
			this.member.internalRemoveOrder(this);
		}

		this.member = member;

		if( this.member != null ){
			this.member.internalAddOrder(this);
		}
	}

	// 양방향 관계 패턴(order - orderItem)
	public List<OrderItem> getOrderItems() {
		return Collections.unmodifiableList(orderItems);
	}

	public void internalAddOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public void internalRemoveOrderItem(OrderItem orderItem) {
		orderItems.remove(orderItem);
	}

	public void addOrderItem(OrderItem orderItem){
		orderItem.setOrder(this);
	}

	public void removeOrderItem(OrderItem orderItem){
		orderItem.setOrder(null);
	}

}
