package com.example.service;


import com.example.entity.Order;

import javax.validation.Valid;
import java.util.List;

public interface IOrderService {

    public Order createOrder1( Order order);

    public Order createOrder2(Order order);

    public List<Order> getAllOrder();

    public Order findOrder(Integer orderNumber);

    public String deleteOrder(Integer orderNumber);

    public Order updateOrder(Order order);

}