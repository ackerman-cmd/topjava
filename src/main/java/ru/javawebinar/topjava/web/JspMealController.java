package ru.javawebinar.topjava.web;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = Logger.getLogger(JspMealController.class.getName());


    private final MealRestController restController;

    public JspMealController(MealRestController restController) {
        this.restController = restController;
    }

    @GetMapping
    public String getMealsForUser(Model model) {
        model.addAttribute("meals", restController.getAll());
        log.info("getMeals {}");
        return "meals";
    }

    @GetMapping("/create")
    public String addMeal(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        log.info("addMeal {}");
        return "mealForm";
    }

    @PostMapping()
    public String setMeal(@ModelAttribute Meal meal) {
        if (meal.isNew()) {
            restController.create(meal);
        } else {
            restController.update(meal, meal.id());
        }
        log.info("setMeal {}");
        return "redirect:meals";
    }

    @GetMapping("/update")
    public String editMeal(@RequestParam("id") int id, Model model) {
        Meal forEdit = restController.get(id);
        model.addAttribute(forEdit);
        log.info("editMeal {}");
        return "mealForm";
    }

    @GetMapping("/delete")
    public String deleteMeal(@RequestParam("id") int id) {
        restController.delete(id);
        log.info("deleteMeal {}");
        return "redirect:/meals";
    }

}
