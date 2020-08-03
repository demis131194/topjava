package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);
    void delete(long id);
    Meal find(long id);
    List<Meal> findAll();
}
