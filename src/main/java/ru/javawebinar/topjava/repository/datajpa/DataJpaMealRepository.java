package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private  CrudMealRepository crudRepository;

    private  CrudUserRepository crudUserRepository;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(crudUserRepository.getReferenceById(userId));
            return crudRepository.save(meal);
        } else {
            Meal existingMeal = get(meal.id(), userId);
            if (existingMeal != null) {
                updateMeal(existingMeal, meal);
                return crudRepository.save(existingMeal);
            }
            return null;
        }
    }

    private void updateMeal(Meal existingMeal, Meal meal) {
        existingMeal.setDescription(meal.getDescription());
        existingMeal.setCalories(meal.getCalories());
        existingMeal.setDateTime(meal.getDateTime());
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && crudRepository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllForUserSorted(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
