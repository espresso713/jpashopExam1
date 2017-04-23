package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Member extends BaseEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

    private String name;

    @Embedded
    Address address;
    
    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<Orders>();
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	// 양방향 관계 패턴(member - order)
	public List<Orders> getOrders() {
		return Collections.unmodifiableList(orders);
	}

	public void addOrder(Orders order){
		order.setMember(this);
	}

	public void removeOrder(Orders order) {
		order.setMember(null);
	}

	void internalAddOrder(Orders order) {
		orders.add(order);
	}

	void internalRemoveOrder(Orders order) {
		orders.remove(order);
	}

}
