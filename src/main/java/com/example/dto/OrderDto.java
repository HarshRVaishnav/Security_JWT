package com.example.dto;

import com.example.entity.Customer;
import com.example.vo.OrderStatusVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto  {
    private Integer orderNumber;

    private LocalDate orderDate;

    private LocalDate shippedDate;

    private OrderStatusVo orderStatusVo;

    private String comments;

    private Integer customerNumber;

    @JsonIgnore
    private Customer customer;

    private List<OrderDetailsDto> orderDetails;

}
