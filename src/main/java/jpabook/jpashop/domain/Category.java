package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hyun on 2016-12-30.
 */
@Entity
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "CATEGORY_ITEM"
            , joinColumns = @JoinColumn(name = "CATEGORY_ID")
            , inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
    )
    private List<Item> items = new ArrayList<Item>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childCategories = new ArrayList<Category>();

    private String name;

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

    public void setItems(List<Item> items) {

        this.items = items;
    }

    // 양방향 매핑 패턴(item - category)
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Item item) {
        items.add(item);
        item.internalAddCategory(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.internalRemoveCategory(this);
    }

    // 양방향 매핑패턴(parent category - child category)
    public List<Category> getCategories() {
        return Collections.unmodifiableList(childCategories);
    }

    public void addChildCategory(Category category) {
        category.addParentCategory(this);
    }

    public void removeChildCategory(Category category) {
        category.addParentCategory(null);
    }

    public void internalAddChildCategory(Category category) {
        childCategories.add(category);
    }

    public void internalRemoveChildCategory(Category category) {
        childCategories.remove(category);
    }

    public Category getParent() {
        return parent;
    }

    public void addParentCategory(Category category) {
        if (parent != null) {
            parent.internalRemoveChildCategory(category);
        }

        parent = category;

        if (parent != null) {
            parent.internalAddChildCategory(category);
        }
    }

}
