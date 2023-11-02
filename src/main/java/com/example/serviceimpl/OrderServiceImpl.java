package com.example.serviceimpl;

import com.example.entity.Order;
import com.example.entity.OrderDetails;
import com.example.entity.Product;
import com.example.exception.OrderNotFoundException;
import com.example.exception.ProductNotFoundException;
import com.example.repository.IOrderDetailsRepo;
import com.example.repository.IOrderRepo;
import com.example.service.ICustomerService;
import com.example.service.IOrderService;
import com.example.service.IProductService;
import com.example.vo.OrderStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepo orderRepo;

    @Autowired
    private IOrderDetailsRepo orderDetailsRepo;

    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProductService productService;


    @Transactional
    @Override
    public Order createOrder1(Order order) {
        order.setComments(order.getComments());
        order.setOrderStatusVo(order.getOrderStatusVo());
        orderRepo.save(order);
        List<OrderDetails> orderDetailsDtoList = order.getOrderDetails();
        List<Integer> pid = orderDetailsDtoList.stream().map(OrderDetails::getProductCode).collect(Collectors.toList());
        List<Product> products = productService.getlistbyproductCode(pid);

        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductCode, product -> product));
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        //Product product1 = productMap.get(orderDetailsDto.getProductCode());

        for (OrderDetails orderDetailsDto : orderDetailsDtoList) {
            Product product = productMap.get(orderDetailsDto.getProductCode());
            if (product.getQuantityInStock() < orderDetailsDto.getQuantityOrdered()) {
                throw new ProductNotFoundException("Product is out of stock");
            }
            productService.updateProductQuantityInStock(product.getProductCode(), orderDetailsDto.getQuantityOrdered());
            OrderDetails orderDetails = modelMapper.map(orderDetailsDto, OrderDetails.class);
            orderDetails.setProductCode(product.getProductCode());
            orderDetails.setQuantityOrdered(orderDetailsDto.getQuantityOrdered());
            orderDetails.setPriceEach(product.getPrice());
            //orderDetails.setProduct(product);
            //orderDetails.setOrder(order);
            orderDetails.setOrderNumber(order.getOrderNumber());
            orderDetailsList.add(orderDetails);
        }
        orderDetailsRepo.saveAll(orderDetailsList);
        //order.setOrderDetails(orderDetailsList);
        //orderRepo.save(order);
        return order;
    }


    @Transactional
    @Override
    public Order createOrder2(Order order) {
        order.setComments(order.getComments());
        //order.set
        orderRepo.save(order);
        List<OrderDetails> orderDetailsList1 = new ArrayList<>();
        List<OrderDetails> orderDetailsDTOList = order.getOrderDetails();
        List<Integer> pid = orderDetailsDTOList.stream().map(OrderDetails::getProductCode).collect(Collectors.toList());
        List<Product> products = productService.getlistbyproductCode(pid);
        log.info("product entity : " + products.toString());
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductCode, product -> product));
        for (int i = 0; i < orderDetailsDTOList.size(); i++) {
            OrderDetails orderDetailsDTO = orderDetailsDTOList.get(i);
            Product product = productMap.get(orderDetailsDTO.getProductCode());
            if ((product.getQuantityInStock() >= orderDetailsDTO.getQuantityOrdered())) {
                OrderDetails orderDetails = modelMapper.map(orderDetailsDTOList, OrderDetails.class);
                productService.updateProductQuantityInStock(product.getProductCode(), orderDetailsDTO.getQuantityOrdered());
                orderDetails.setOrderNumber(order.getOrderNumber());
                orderDetails.setProductCode(product.getProductCode());
                orderDetails.setQuantityOrdered(orderDetailsDTO.getQuantityOrdered());
                orderDetails.setPriceEach(product.getPrice());
                orderDetails.setProduct(product);
                orderDetailsList1.add(orderDetails);      //orderDetailsRepo.save(orderDetails);
                log.info("orderDetails entity : " + orderDetails.toString());
            } else {
                throw new ProductNotFoundException("Product is out of stock");
            }
        }
        orderDetailsRepo.saveAll(orderDetailsList1);
        log.info("order entity : " + order.toString());
        return order;
    }


    @Override
    public Order findOrder(Integer orderNumber) {
        return orderRepo.findById(orderNumber).orElseThrow(() -> new IllegalArgumentException());
    }


    @Override
    public List<Order> getAllOrder() {
        return orderRepo.findAll();
    }


    @Override
    public String deleteOrder(Integer orderNumber) {
        Optional<Order> optionalOrder = orderRepo.findById(orderNumber);
        // Order orders = null;
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            // Update the quantity of each product that was ordered in this order
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                int productCode = orderDetails.getProductCode();
                int quantityOrdered = orderDetails.getQuantityOrdered();
                productService.incrementDecrementProductQuantityInStock(productCode, "increment", quantityOrdered);
            }
            orderRepo.delete(order);
            return "Order with order number " + orderNumber + " has been deleted";
        } else {
            throw new OrderNotFoundException("Order with order number " + orderNumber + " not found");
        }
    }


    public OrderStatusVo updateStatus(Integer orderNumber, OrderStatusVo newStatus) {
        Optional<Order> foundOrder = orderRepo.findById(orderNumber);
        if (foundOrder.isPresent()) {
            Order order = new Order();
            //order.setOrderStatusVo(newStatus);
            orderRepo.save(order);
            return newStatus;
        } else {
            throw new OrderNotFoundException();
        }
    }


    public Order updateShippingDate(Integer orderNumber, Order o) {
        Optional<Order> foundOrder = orderRepo.findById(orderNumber);
        if (foundOrder.isPresent()) {
            Order order2 = foundOrder.get();
            order2.setShippedDate(o.getShippedDate());
            return orderRepo.save(order2);
        } else {
            throw new OrderNotFoundException("Shipping Date not found for order number " + orderNumber);
        }
    }


    public Order updateComment(Integer orderNumber, Order o) {
        Optional<Order> foundOrder = orderRepo.findById(orderNumber);
        if (foundOrder.isPresent()) {
            Order order2 = foundOrder.get();
            order2.setComments(o.getComments());
            return orderRepo.save(order2);
        } else {
            throw new OrderNotFoundException("No Comment found for order number " + orderNumber);
        }
    }


    // @Override
    public Order updateOrder(Order order) {
        Order orders = orderRepo.findById(order.getOrderNumber()).get();
        try {
            orders.setOrderNumber(order.getOrderNumber());
            orders.setComments(order.getComments());
            orders.setOrderStatusVo(order.getOrderStatusVo());
            orderRepo.save(order);
            List<OrderDetails> orderDetailsDtoList = order.getOrderDetails();
            List<Integer> pid = orderDetailsDtoList.stream().map(OrderDetails::getProductCode).collect(Collectors.toList());
            List<Product> products = productService.getlistbyproductCode(pid);
            Map<Integer, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getProductCode, product -> product));
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            for (OrderDetails orderDetailsDto : orderDetailsDtoList) {
                Product product = productMap.get(orderDetailsDto.getProductCode());
                if (product.getQuantityInStock() < orderDetailsDto.getQuantityOrdered()) {
                    throw new ProductNotFoundException("Product is out of stock");
                }
                productService.updateProductQuantityInStock(product.getProductCode(), orderDetailsDto.getQuantityOrdered());
                OrderDetails orderDetails = modelMapper.map(orderDetailsDto, OrderDetails.class);
                orderDetails.setProductCode(product.getProductCode());
                orderDetails.setQuantityOrdered(orderDetailsDto.getQuantityOrdered());
                orderDetails.setPriceEach(product.getPrice());
                orderDetails.setOrderNumber(order.getOrderNumber());
                orderDetailsList.add(orderDetails);
            }
            orderDetailsRepo.saveAll(orderDetailsList);
            return orders;
        } catch (Exception e) {
            throw new ProductNotFoundException();
        }
    }
}
