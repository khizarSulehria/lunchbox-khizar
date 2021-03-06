package com.example.lunchbox.service.Impl;

import com.example.lunchbox.model.entity.Foodmaker;
import com.example.lunchbox.repository.FoodmakerRepository;
import com.example.lunchbox.service.FoodmakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
public class FoodmakerServiceImpl implements FoodmakerService {

    private FoodmakerRepository foodmakerRepository;

    @Autowired
    public FoodmakerServiceImpl(FoodmakerRepository foodmakerRepository) {
        this.foodmakerRepository = foodmakerRepository;
    }

    @Override
    public void foodmakerSignup(Foodmaker foodmaker) {
        try {
            foodmaker.setFoodmakerpassword(getSHA256(foodmaker.getFoodmakerpassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        foodmakerRepository.saveAndFlush(foodmaker);
    }

    @Override
    public Foodmaker getFoodmakerById(Integer id) {
        return foodmakerRepository.findOne(id);
    }

    @Override
    public long countAllFoodMmkers() {
        return foodmakerRepository.count();
    }

    @Override
    public void deleteFoodmaker(String foodmakerEmail) {
        Foodmaker foodmaker = this.findByFoodmakerEmail(foodmakerEmail);
        foodmakerRepository.delete(foodmaker.getFoodmakerId());
    }

    @Override
    public List <Foodmaker> getFoodmakerByname(String foodmakerName) {
        return foodmakerRepository.findByFoodmakerName(foodmakerName);
    }

    @Override
    public List<Foodmaker> findAllFoodmakers() {
        return foodmakerRepository.findAll();
    }

    @Override
    public Foodmaker findByFoodmakerEmail(String foodmakerEmail) {
        return foodmakerRepository.findByFoodmakerEmail(foodmakerEmail);
    }

   @Override
    public Foodmaker login(String foodmakerEmail, String foodmakerPassword) {
        Foodmaker foodmaker = this.findByFoodmakerEmail(foodmakerEmail);
       try {
           if (foodmaker != null && getSHA256(foodmakerPassword).equals(foodmaker.getFoodmakerpassword())) {
               return foodmaker;
           }
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return null;
    }

    @Override
    public boolean updatePassword(String oldpassword, String newpassword , String foodmakerEmail) {
        Foodmaker foodmaker = this.findByFoodmakerEmail(foodmakerEmail);
        try {
            if(foodmaker != null && getSHA256(oldpassword).equals(foodmaker.getFoodmakerpassword()))
            {
                foodmaker.setFoodmakerpassword(getSHA256(newpassword));
                foodmakerRepository.saveAndFlush(foodmaker);
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getSHA256(String password) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest=MessageDigest.getInstance("SHA-512");

        messageDigest.update(password.getBytes());
        byte[] digest=messageDigest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((int) (b & 0xff)));
        }
        return sb.toString();
    }

    @Override
    public Foodmaker getFoodmakerByFoodmakerCreatedAt(Date date) {
        return foodmakerRepository.getFoodmakerByFoodmakerCreatedAt(date);
    }

    @Override
    public Foodmaker getFoodmakerByFoodmakerLastUpdated(Date date) {
        return foodmakerRepository.getFoodmakerByFoodmakerLastUpdated(date);
    }
}
