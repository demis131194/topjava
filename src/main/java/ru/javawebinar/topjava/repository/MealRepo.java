package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealIdGenerator;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MealRepo implements MealRepository {
    public Map<Long, Meal> meals = new ConcurrentHashMap<>();

    {
        List<Meal> mealsList = Arrays.asList(
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(MealIdGenerator.generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        mealsList.forEach(meal -> meals.put(meal.getId(), meal));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() != null && meals.containsKey(meal.getId())) {
            return meals.put(meal.getId(), meal);
        }

        Meal createdMeal = new Meal(MealIdGenerator.generateId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return meals.put(createdMeal.getId(), createdMeal);
    }

    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    @Override
    public Meal find(long id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }
}
