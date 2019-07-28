package com.alfred.parkingalfred.service.impl;

import com.alfred.parkingalfred.dto.CreateOrderDto;
import com.alfred.parkingalfred.entity.Order;
import com.alfred.parkingalfred.enums.OrderStatusEnum;
import com.alfred.parkingalfred.enums.OrderTypeEnum;
import com.alfred.parkingalfred.enums.ResultEnum;
import com.alfred.parkingalfred.exception.OrderNotExistedException;
import com.alfred.parkingalfred.repository.OrderRepository;
import com.alfred.parkingalfred.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(orderRepository);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_add_new_order_when_create_new_order() throws JsonProcessingException {
        String carNumber = "123456";
        String address = "address";
        Long reservationTime = new Date().getTime();
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setCarNumber(carNumber);
        createOrderDto.setCustomerAddress(address);
        createOrderDto.setReservationTime(reservationTime);
        createOrderDto.setType(OrderTypeEnum.PARK_CAR.getCode());

        Order expectOrder = new Order();
        expectOrder.setCarNumber(carNumber);
        expectOrder.setCustomerAddress(address);
        expectOrder.setReservationTime(reservationTime);
        expectOrder.setType(OrderTypeEnum.PARK_CAR.getCode());
        expectOrder.setStatus(OrderStatusEnum.WAIT_FOR_RECEIVE.getCode());
        Mockito.when(orderRepository.save(any())).thenReturn(expectOrder);
        Order actualOrder = orderService.addOrder(createOrderDto);
        expectOrder.setOrderId(actualOrder.getOrderId());

        assertEquals(objectMapper.writeValueAsString(expectOrder), objectMapper.writeValueAsString(actualOrder));
    }

    @Test
    public void should_get_order_when_get_order_by_id() throws JsonProcessingException {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Order actualOrder = orderService.getOrderById(id);

        assertEquals(objectMapper.writeValueAsString(order), objectMapper.writeValueAsString(actualOrder));
    }

    @Test
    public void should_throw_exception_when_get_order_by_invalid_id() {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);

        when(orderRepository.findById(anyLong())).thenThrow(new OrderNotExistedException(ResultEnum.RESOURCES_NOT_EXISTED));

        assertThrows(OrderNotExistedException.class, () -> orderService.getOrderById(id));
    }
    @Test
    public void should_return_order_List_when_get_orders_by_status_and_type() throws JsonProcessingException {


        Order order = new Order();
        order.setCarNumber("xiangAAA");
        order.setCustomerAddress("address");
        order.setReservationTime(new Date().getTime());
        order.setType(OrderTypeEnum.PARK_CAR.getCode());
        order.setStatus(OrderStatusEnum.WAIT_FOR_RECEIVE.getCode());
        List<Order> orderList=new ArrayList<>();
        orderList.add(order);
        Mockito.when(orderRepository.findOrdersByTypeAndStatus(OrderTypeEnum.PARK_CAR.getCode(),OrderStatusEnum.WAIT_FOR_RECEIVE.getCode())).thenReturn(orderList);
        List<Order> actualOrderList = orderService.getOrdersByTypeAndStatus(OrderTypeEnum.PARK_CAR.getCode(),OrderStatusEnum.WAIT_FOR_RECEIVE.getCode());

        assertEquals(orderList.size(), actualOrderList.size());
    }
}