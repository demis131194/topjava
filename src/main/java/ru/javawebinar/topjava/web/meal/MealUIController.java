package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/profile/meals")
public class MealUIController extends AbstractMealController {

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MealTo> getAll() {

		return super.getAll();
	}

	@Override
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(int id) {

		super.delete(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Meal createMeal(@RequestParam Integer id, @RequestParam LocalDateTime dateTime, @RequestParam String description, @RequestParam Integer calories) {

		Meal meal = new Meal(id, dateTime, description, calories);
		return super.create(meal);
	}
}
