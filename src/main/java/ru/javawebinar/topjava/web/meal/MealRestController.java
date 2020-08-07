package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

@Controller
public class MealRestController {
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal, int userId) {
        return service.create(meal, userId);
    }

    public void delete(int id, int userId) {
        service.delete(id, userId);
    }

    public Meal get(int id, int userId) {
        return service.get(id, userId);
    }

    public List<MealTo> getAllByUserId(int userId, int userCalories) {
        return service.getAllByUserId(userId, userCalories);
    }

    public void update(Meal meal, int userId) {
        service.update(meal, userId);
    }
}