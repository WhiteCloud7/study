package com.whitecloud.provider.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order")
public class Order {
    @Id
    private Long orderId;
    private String orderTitle;
    private String orderNum;
    private String orderContent;
    private String orderDate;
    @OneToOne
    private User user;

    public Order() {
    }

    public Order(String orderTitle, String orderNum, String orderContent, String orderDate) {
        this.orderTitle = orderTitle;
        this.orderNum = orderNum;
        this.orderContent = orderContent;
        this.orderDate = orderDate;
    }

    public Order(String orderTitle, String orderNum, String orderContent, String orderDate, User user) {
        this.orderTitle = orderTitle;
        this.orderNum = orderNum;
        this.orderContent = orderContent;
        this.orderDate = orderDate;
        this.user = user;
    }

    public Order(Long orderId, String orderTitle, String orderNum, String orderContent, String orderDate) {
        this.orderId = orderId;
        this.orderTitle = orderTitle;
        this.orderNum = orderNum;
        this.orderContent = orderContent;
        this.orderDate = orderDate;
    }

    public Order(Long orderId, String orderTitle, String orderNum, String orderContent, String orderDate, User user) {
        this.orderId = orderId;
        this.orderTitle = orderTitle;
        this.orderNum = orderNum;
        this.orderContent = orderContent;
        this.orderDate = orderDate;
        this.user = user;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
