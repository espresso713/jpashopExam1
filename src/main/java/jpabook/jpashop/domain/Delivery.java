package jpabook.jpashop.domain;

import javax.persistence.*;

/**
 * Created by hyun on 2016-12-30.
 */
@Entity
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Orders order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public Delivery(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    // 양방향 관계 패턴(order - delivery)
    public Orders getOrder() {
        return order;
    }

    public void addOrder(Orders order) {
        order.setDelivery(this);
    }

    public void removeOrder(Orders order) {
        order.setDelivery(null);
    }

    void internalSetOrder(Orders order) {
        this.order = order;
    }


}
