package ru.javawebinar.topjava.repository.datajpa;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.configuration.ApplicationConfig;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = ApplicationConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"datajpa", "jdbc"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaMealRepositoryTest {

    @Autowired
    @Qualifier("jpaMealRepository")
    private MealRepository mealRepository;

    @Autowired
    @Qualifier("jpaUserRepository")
    private UserRepository userRepository;

    private static Meal testMealForAdmin;

    private static Meal testMealForUser;

    private static User testAdmin;

    private static User testUser;

    @Before
    public void setUp() throws Exception {
        testAdmin = userRepository.save(new User(1, "Admin", "admin@mail.ru", "admin-admin", Role.ADMIN));
        testUser = userRepository.save(new User(2, "User", "user@mail.ru", "user-user", Role.USER));

        testMealForAdmin = mealRepository.save(new Meal(LocalDateTime.now(), "mealForAdmin", 200), testAdmin.id());
        testMealForUser = mealRepository.save(new Meal(LocalDateTime.now(), "mealForUser", 300), testUser.id());
    }

    @After
    public void tearDown() throws Exception {
        userRepository.delete(testUser.id());
        userRepository.delete(testAdmin.id());

    }

    @Test
    public void save_New_Meal() {
        Meal toSave = new Meal(LocalDateTime.now(), "toEat", 200);
        Meal saved = mealRepository.save(toSave, testUser.id());

        assertNotNull(saved);
        assertEquals(toSave, saved);
        assertTrue(mealRepository.delete(saved.id(), testUser.id()));
    }

    @Test
    public void delete_Meal() {
        Meal toSave = new Meal(LocalDateTime.now(), "test", 200);
        Meal saved = mealRepository.save(toSave, testAdmin.id());

        assertNotNull(saved);
        assertTrue(mealRepository.delete(saved.id(), testAdmin.id()));
    }

    @Test
    public void get_By_Id() {
        assertNotNull(mealRepository.get(testMealForUser.id(), testUser.id()));
        assertNotNull(mealRepository.get(testMealForAdmin.id(), testAdmin.id()));
    }

    @Test
    public void get_All() {
        assertFalse(mealRepository.getAll(testUser.id()).isEmpty());
        assertFalse(mealRepository.getAll(testAdmin.id()).isEmpty());
    }

    @Test
    public void get_Between() {
        LocalDateTime start = LocalDateTime.of(2025, 2, 24, 1, 25);
        LocalDateTime end = LocalDateTime.of(2025, 3, 24, 1, 50);

        mealRepository.save(new Meal(start, "testMealForAdmin", 200), testAdmin.id());
        mealRepository.save(new Meal(start, "testMealForUser", 300), testUser.id());

        assertFalse(mealRepository.getBetweenHalfOpen(start, end, testUser.id()).isEmpty());
        assertFalse(mealRepository.getBetweenHalfOpen(start, end, testAdmin.id()).isEmpty());
    }
}