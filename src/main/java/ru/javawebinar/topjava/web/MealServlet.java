package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = applicationContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        applicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("save".equals(action)) {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    SecurityUtil.authUserId());

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            controller.create(meal, SecurityUtil.authUserId());

            response.sendRedirect("meals");

        } else if ("filter".equals(action)) {
            String fromDate = request.getParameter("fromDate");
            LocalDate parsedFromDate = LocalDate.parse(fromDate);
            request.setAttribute("fromDate", parsedFromDate);

            String toDate = request.getParameter("toDate");
            LocalDate parsedToDate = LocalDate.parse(toDate);
            request.setAttribute("toDate", parsedToDate);

            String fromTime = request.getParameter("fromTime");
            LocalTime parsedFromTime = LocalTime.parse(fromTime);
            request.setAttribute("fromTime", parsedFromTime);

            String toTime = request.getParameter("toTime");
            LocalTime parsedToTime = LocalTime.parse(toTime);
            request.setAttribute("toTime", parsedToTime);

            List<MealTo> allByUserId = controller.getAllByUserId(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
            List<MealTo> meals = allByUserId.stream()
                    .filter(mealTo -> mealTo.getDateTime().toLocalDate().compareTo(parsedFromDate) >= 0 && mealTo.getDateTime().toLocalDate().compareTo(parsedToDate) <= 0)
                    .filter(mealTo -> DateTimeUtil.isBetweenHalfOpen(mealTo.getDateTime().toLocalTime(), parsedFromTime, parsedToTime))
                    .collect(Collectors.toList());

            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", SecurityUtil.authUserCaloriesPerDay(), SecurityUtil.authUserId()) :
                        controller.get(getId(request), SecurityUtil.authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        controller.getAllByUserId(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
