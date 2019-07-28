package com.alfred.parkingalfred.controller;

import com.alfred.parkingalfred.entity.Employee;
import com.alfred.parkingalfred.entity.Order;
import com.alfred.parkingalfred.service.OrderService;
import com.alfred.parkingalfred.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private List<Order> orders;

    @Before
    public void setUp() {
        orders = new ArrayList<>();
        orders.add(new Order());
    }

    @Test
    public void should_return_orders_when_get_it() throws Exception {
        when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(jsonPath("$.data.length()")
                        .value(1));
    }

    @Test
    public void should_return_order_when_add_new_order() throws Exception {
        Order order = new Order();
        when(orderService.addOrder(order)).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_order_when_get_order_by_id() throws Exception {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        when(orderService.getOrderById(anyLong())).thenReturn(order);

        Employee employee = new Employee();
        employee.setId(id);
        String token = JwtUtil.generateToken(employee);

        mockMvc.perform(get("/orders/{id}", id)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_return_updated_order_when_update_by_id() throws Exception {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);

        Order orderExpected = new Order();
        orderExpected.setId(id);
        orderExpected.setStatus(2);

        when(orderService.updateOrderStatusById((long) 1,order))
                .thenReturn(orderExpected);

        Employee employee = new Employee();
        employee.setId(id);
        String token = JwtUtil.generateToken(employee);

        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
