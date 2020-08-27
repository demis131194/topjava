package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {


        if (meal.isNew()) {

            User userRef = entityManager.getReference(User.class, userId);
            meal.setUser(userRef);
            entityManager.persist(meal);
            return meal;

        } else {

            return meal.getUser() != null && meal.getUser().getId() == userId ? entityManager.merge(meal) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal deletedMeal = entityManager.find(Meal.class, id);

        if (deletedMeal != null && deletedMeal.getUser().getId() == userId) {
            return entityManager.createNamedQuery(Meal.DELETE)
                    .setParameter("id", id)
                    .executeUpdate() != 0;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = entityManager.find(Meal.class, id);

        return (meal != null) && (meal.getUser().getId() == userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.ALL_ORDERED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.ALL_HALF_OPEN_ORDERED, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}