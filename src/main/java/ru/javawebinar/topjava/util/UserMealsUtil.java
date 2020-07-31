package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> userMealWithExcesses = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        userMealWithExcesses.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, List<UserMealWithExcess>> mealsForDate = new HashMap<>();
        Map<LocalDate, Integer> caloriesForDate = new HashMap<>();

        for (UserMeal meal : meals) {

            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);

            LocalDate mealLocalDate = meal.getDateTime().toLocalDate();

            mealsForDate.putIfAbsent(mealLocalDate, new ArrayList<>());
            mealsForDate.get(mealLocalDate).add(userMealWithExcess);

            caloriesForDate.merge(mealLocalDate, meal.getCalories(), Integer::sum);

            if (caloriesForDate.get(mealLocalDate) > caloriesPerDay) {
                mealsForDate.get(mealLocalDate).forEach(curMeal -> curMeal.setExcess(true));
            }

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(userMealWithExcess);
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values().stream()
                .flatMap(userMeals -> {
                    if(userMeals.stream().map(UserMeal::getCalories).reduce(Integer::sum).orElse(0) > caloriesPerDay) {
                        return userMeals.stream()
                                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                    } else {
                        return userMeals.stream()
                                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
                    }
                }).filter(userMealWithExcess -> TimeUtil.isBetweenHalfOpen(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
    }
}
