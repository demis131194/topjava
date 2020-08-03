package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepo;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class MealsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealsServlet.class);

    private MealRepo repo = new MealRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String mealStrId = req.getParameter("mealId");
            Long id = getIdFromStr(mealStrId);
            repo.delete(id);
        }


        logger.debug("forward to meals");

        List<MealTo> meals = MealsUtil.filteredByStreams(repo.findAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("meals", meals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        logger.debug("post to meals");



        String mealStrId = req.getParameter("mealId");
        Long id = getIdFromStr(mealStrId);

        String description = req.getParameter("description");

        String mealDate = req.getParameter("mealDate");
        String mealTime = req.getParameter("mealTime");
        String dateTimeToParse = String.format("%sT%s", mealDate, mealTime);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeToParse);

        Integer calories = Integer.valueOf(req.getParameter("calories"));

        Meal meal = new Meal(id, dateTime, description, calories);

        repo.save(meal);

        List<MealTo> meals = MealsUtil.filteredByStreams(repo.findAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("meals", meals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private Long getIdFromStr(String mealStrId) {
        try {
            return Long.valueOf(mealStrId);
        } catch (NumberFormatException ignored) {

        }
        return null;
    }
}
