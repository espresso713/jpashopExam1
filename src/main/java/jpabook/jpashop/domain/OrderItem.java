package jpabook.jpashop.domain;



import jpabook.jpashop.domain.item.Item;

import javax.persistence.*;

@Entity
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "ORDER_ITEM_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID")
	private Orders order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_ID")
	private Item item;
	
	private int orderPrice; 
	
	private int count;

	public static OrderItem createOrderItem(Item item, int orderPrice, int count){

		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);

		item.removeStock(count);

		return orderItem;
	}

	public void cancel(){
		getItem().addStock(count);
	}

	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	// 단방향 관계 패턴(order - item)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	// 양방향 관계 패턴(order - orderItem)
	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		if( this.order != null ){
			this.order.internalRemoveOrderItem(this);
		}

		this.order = order;

		if( this.order != null ){
			this.order.internalAddOrderItem(this);
		}
	}
	
}
