package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Autowired
    @Qualifier("jdbcMealRepository")
    private MealRepository mealRepository;

    @Test
    public void create() {
        Meal newMeal = getNewMeal();
        Meal created = mealService.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatchMeal(created, newMeal);
        assertMatchMeal(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateUserDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(USER_MEAL_1.getDateTime(), "Test", 1000), USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_1.getId(), USER_ID);
        assertNull(mealRepository.get(USER_MEAL_1.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDateTime startDateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusDays(1);

        List<Meal> actualMeals = mealRepository.getBetweenHalfOpen(startDateTime, endDateTime, USER_ID);

        assertMatchMeals(actualMeals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void get() {
        Meal meal = mealService.get(USER_MEAL_1.getId(), USER_ID);
        assertMatchMeal(meal, USER_MEAL_1);
    }

    @Test
    public void getAllUser() {
        List<Meal> actualUserMeals = mealRepository.getAll(USER_ID);
        ArrayList<Meal> expected = new ArrayList<>(USER_MEALS);
        Collections.reverse(expected);
        assertMatchMeals(actualUserMeals, expected);
    }

    @Test
    public void update() {
        Meal updated = getUpdatedUserMeal();
        mealService.update(updated, USER_ID);
        assertMatchMeal(mealService.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void getOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_1.getId(), ADMIN_ID));
    }

    @Test
    public void deleteOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> mealService.delete(USER_MEAL_1.getId(), ADMIN_ID));
    }

    @Test
    public void updateOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> mealService.update(getUpdatedUserMeal(), ADMIN_ID));
    }
}