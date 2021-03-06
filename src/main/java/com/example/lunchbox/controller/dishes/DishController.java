package com.example.lunchbox.controller.dishes;

import com.example.lunchbox.model.entity.Dishes;
import com.example.lunchbox.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/dishes")
public class DishController  {

    private DishService dishService;

    @Autowired
    public DishController(DishService dishService)
    {
        this.dishService = dishService;
    }

    @RequestMapping(value = "/save-dishes" , method = RequestMethod.POST)
    public void addDishes(@RequestBody Dishes dish){
        dishService.addDish(dish);

    }

    @RequestMapping(value = "/get-dish" , method = RequestMethod.POST)
    public Dishes getDishById(@RequestParam Integer id){
        return dishService.getDishesById(id);

    }


    @RequestMapping(value = "/get-dish-count" , method = RequestMethod.POST)
    public long getDishById(){
        return dishService.countAllDishes();

    }

    @RequestMapping(value = "/delete-dish" , method = RequestMethod.POST)
    public void deleteDish(@RequestBody Dishes dish){
        dishService.deleteDishes(dish);

    }

    @RequestMapping(value = "/get-dish-list" , method = RequestMethod.GET)
    public List<Dishes> getDishesList(){
        return dishService.findAllDishes();

    }

    @RequestMapping(value = "/dishes-listing" , method = RequestMethod.GET)
    public ModelAndView adminDetail() {
        ModelAndView modelAndView = new ModelAndView("dishes-listing");
        return modelAndView;
    }


    @RequestMapping(value = "/add-dishes" ,method = RequestMethod.GET)
    public ModelAndView getAddAdminView(){
        ModelAndView modelAndView = new ModelAndView("add-dishes");
        return modelAndView;
    }

    @RequestMapping(value = "/add-dishes" ,method = RequestMethod.POST)
    public ModelAndView getOrderDetail(@RequestParam Integer rowId){
        Dishes dishes = dishService.getDishesById(rowId);
        ModelAndView modelAndView = new ModelAndView("add-dishes");
        modelAndView.addObject("dishDetail", dishes);

        return modelAndView;
    }

}
