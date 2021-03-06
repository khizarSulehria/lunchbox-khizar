package com.example.lunchbox.service.Impl;

import com.example.lunchbox.model.entity.*;
import com.example.lunchbox.repository.*;
import com.example.lunchbox.service.CustomerService;
import com.example.lunchbox.service.OrderService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private FoodmakerRepository foodmakerRepository;
    private OrderDishesRepository orderDishesRepository;
    private DishRepository dishRepository;


    private Orderdishes orderdishes;
    private EntityManager entityManager;
    private CustomerService customerService;
    private Order order;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDishesRepository orderDishesRepository,CustomerRepository customerRepository,FoodmakerRepository foodmakerRepository,DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.orderDishesRepository = orderDishesRepository;
        this.customerRepository = customerRepository;
        this.foodmakerRepository = foodmakerRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public void saveOrder(Order order) {
        try {
            Order response = orderRepository.save(order);
            for(Orderdishes orderdishe: order.getOrderdishes()){
                orderdishe.setOrder(response);
                orderDishesRepository.save(orderdishe);
            }
            orderRepository.save(order);
        }catch (Exception e){

        }

    }

    @Override
    public void editOrder(Order order) {

    }

    @Override
    public Order getOrderById(Integer id) {
       Order order  = orderRepository.findOne(id);
        Integer customerId = order.getOrderCustomerId();
        Integer foodmakerId = order.getFoodmakerId();
        for (Orderdishes orderdishes : order.getOrderdishes()){
            Integer dishId = orderdishes.getDishId();
            Dishes dish = dishRepository.findOne(dishId);
            orderdishes.setDishes(dish);
        }
        Customer customer = customerRepository.findByCustomerId(customerId);
        Foodmaker foodmaker = foodmakerRepository.findOne(foodmakerId);
        order.setCustomer(customer);
        order.setFoodmaker(foodmaker);
        return order;
        //return orderRepository.findOne(id);
    }

    @Override
    public long countAllOrders() {
        return orderRepository.count();
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public List<Order> findAllOrders() {
        List<Order> returnList = new ArrayList<>();
        try {
            List<Order> orders  = orderRepository.findAll();

            /***
             * retrive customer through for customer order id
             * retrive foodmaker through for foodmaker id
             * retrive orderdishes through for order id
             * */
            for(Order order: orders){
                Integer customerId = order.getOrderCustomerId();
                Integer foodmakerId = order.getFoodmakerId();
                //List<Dishes> orderDishesList = new ArrayList<>();

                for (Orderdishes orderdishes : order.getOrderdishes()){
                    Integer dishId = orderdishes.getDishId();
                    Dishes dish = dishRepository.findOne(dishId);
                    orderdishes.setDishes(dish);
                }
                Customer customer = customerRepository.findByCustomerId(customerId);
                Foodmaker foodmaker = foodmakerRepository.findOne(foodmakerId);
                order.setCustomer(customer);
                order.setFoodmaker(foodmaker);
                returnList.add(order);
            }

        }catch (Exception e){

        }
         return returnList;
     //    return orderRepository.findAll();
    }
}
