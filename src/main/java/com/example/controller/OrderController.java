package com.example.controller;

import com.example.customresponse.CustomResponse;
import com.example.dto.OrderDto;
import com.example.entity.Order;
import com.example.exception.OrderNotFoundException;
import com.example.service.IOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private String code;

    private Object data;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private ModelMapper modelMapper;

    @SuppressWarnings("finally")
    @GetMapping
    public ResponseEntity<Object> getAllOrder() {
        try {
            List<Order> list = iOrderService.getAllOrder();

            data = list;
            code = "SUCCESS";
        } catch (Exception e) {
            code = "EXCEPTION"; data = null;
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @PostMapping
    public ResponseEntity<Object> placeOrder(@Valid @RequestBody final OrderDto OrderDto) {
        try {
            Order DtoToEntity = modelMapper.map(OrderDto, Order.class);
            Order orderEntity = iOrderService.createOrder1(DtoToEntity);
            OrderDto ordertoDto = modelMapper.map(orderEntity, OrderDto.class);
            data = ordertoDto;
            code = "CREATED";
        } catch (OrderNotFoundException orderNotFoundException) {
            code = "DATA_NOT_CREATED"; data = null;
        } catch (RuntimeException order) {
            code = "RUNTIME_EXCEPTION"; data = null;
        } catch (Exception e) {
            code = "EXCEPTION"; data = null;
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @GetMapping("{orderNumber}")
    public ResponseEntity<Object> showOrderById(@PathVariable final Integer orderNumber) {
        try {
            Order orderFound = iOrderService.findOrder(orderNumber);

            data = orderFound;
            code = "SUCCESS";
        } catch (OrderNotFoundException orderNotFoundException) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException r) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception e) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @DeleteMapping("{orderNumber}")
    public ResponseEntity<Object> deleteOrder(@PathVariable final Integer orderNumber) {
        try {
            String deletedOrder = iOrderService.deleteOrder(orderNumber);
            data = deletedOrder;
            code = "SUCCESS";
        } catch (OrderNotFoundException e) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception e) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    /*@SuppressWarnings("finally")
    @PatchMapping("/updateShippingDate/{orderNumber}")
    public ResponseEntity<Object> updateShippingDate(@Valid @PathVariable Integer orderNumber,
                                                     @RequestBody @Valid Order order) {
        try {
            Order updatedDate = iOrderService.updateOrder(orderNumber, order);
            data = updatedDate;
            code = "SUCCESS";
        } catch (OrderNotFoundException e) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception r) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }

    @SuppressWarnings("finally")
    @PatchMapping("/updateStatus/{orderNumber}")
    public ResponseEntity<Object> updateStatus(@PathVariable @Valid Integer orderNumber,
                                               @RequestBody @Valid Order order) {
        try {
            Order updatedStatus = iOrderService.(orderNumber, order);
            data = updatedStatus;
            code = "SUCCESS";
        } catch (OrderNotFoundException orderNotFoundException) {
            data = null;
            code = "ORDER_NOT_FOUND_ERROR";
        } catch (RuntimeException runtimeException) {
            data = null;
            code = "RUNTIME_EXCEPTION";
        } catch (Exception exception) {
            data = null;
            code = "EXCEPTION";
        } finally {
            return CustomResponse.response(code, data);
        }
    }*/
}