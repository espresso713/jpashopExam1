package jpabook.jpashop.domain.item;


import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    private String name;

    private int price;

    private int stockQuantity;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if( restStock < 0 ) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // 양방향 매핑 패턴(item - category)
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void internalAddCategory(Category category) {
        categories.add(category);
    }

    public void internalRemoveCategory(Category category) {
        categories.remove(category);
    }

    public void addCategory(Category category) {
        category.addItem(this);
    }

    public void removeCategory(Category category) {
        category.removeItem(this);
    }

}
