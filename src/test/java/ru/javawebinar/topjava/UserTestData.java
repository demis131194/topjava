package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_NOT_FOUND_ID = 10;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

    public static final Meal USER_MEAL_1 = new Meal(START_SEQ + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal USER_MEAL_5 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal USER_MEAL_6 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal USER_MEAL_7 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final List<Meal> USER_MEALS = Collections.unmodifiableList(
            Arrays.asList(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3, USER_MEAL_4, USER_MEAL_5, USER_MEAL_6, USER_MEAL_7)
    );

    public static final Meal ADMIN_MEAL_1 = new Meal(START_SEQ + 9, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL_2 = new Meal(START_SEQ + 10, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static final List<Meal> ADMIN_MEALS = Collections.unmodifiableList(
            Arrays.asList(ADMIN_MEAL_1, ADMIN_MEAL_2)
    );

    private static final String[] IGNORED_USER_FIELDS_FOR_EQUALS = {"registered", "roles"};
    private static final String[] IGNORED_MEAL_FIELDS_FOR_EQUALS = {};

    public static User getNewUser() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
    }

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.now(), "New Meal", 1000);
    }

    public static User getUpdatedUser() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        return updated;
    }

    public static Meal getUpdatedUserMeal() {
        Meal meal = new Meal(USER_MEAL_1);
        meal.setDescription("Updated Description");
        meal.setCalories(404);
        return meal;
    }



    public static void assertMatchUser(User actual, User expected) {
        assertMatch(actual, expected, IGNORED_USER_FIELDS_FOR_EQUALS);
    }

    public static void assertMatchUsers(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected), IGNORED_USER_FIELDS_FOR_EQUALS);
    }

    public static void assertMatchMeal(Meal actual, Meal expected) {
        assertMatch(actual, expected, IGNORED_MEAL_FIELDS_FOR_EQUALS);
    }

    public static void assertMatchMeals(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected), IGNORED_MEAL_FIELDS_FOR_EQUALS);
    }

    public static void assertMatchMeals(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertMatch(actual, expected, IGNORED_MEAL_FIELDS_FOR_EQUALS);
    }

    public static <T> void assertMatch(Iterable<? extends T> actual, Iterable<? extends T> expected, String... excludedFields) {
        assertThat(actual).usingElementComparatorIgnoringFields(excludedFields).isEqualTo(expected);
    }

    public static <T> void assertMatch(T actual, T expected, String... excludedFields) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, excludedFields);
    }
}
