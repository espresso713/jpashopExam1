package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Orders;
import jpabook.jpashop.searchDto.OrdersSearch;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyun on 2017-01-15.
 */
@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Orders order) {
        em.persist(order);
    }

    public Orders findOne(Long id) {
        return em.find(Orders.class, id);
    }

    public List<Orders> findAll(OrdersSearch ordersSearch) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> o = cq.from(Orders.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        if( !StringUtils.isEmpty(ordersSearch.getOrderStatus()) ) {
            Predicate status = cb.equal(o.get("orderStatus"), ordersSearch.getOrderStatus());
            criteria.add(status);
        }

        if( StringUtils.hasText(ordersSearch.getMemberName()) ) {
            Join<Order, Member> m = o.join("member", JoinType.INNER);
            Predicate name = cb.like(m.<String>get("name"), "%" + ordersSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

        TypedQuery<Orders> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();

    }
}
