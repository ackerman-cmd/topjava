package ru.javawebinar.topjava.repository.datajpa;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.configuration.ApplicationConfig;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles({"jdbc", "datajpa"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaUserRepositoryTest {

    @Autowired
    @Qualifier("dataJpaUserRepository")
    private UserRepository userRepository;

    private static final Logger log = Logger.getLogger(DataJpaUserRepository.class.getName());

    private User userAdmin;

    private User userTest;

    @BeforeClass
    public static void beforeClass() throws Exception {
        log.info("Starting test");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        log.info("Test finished");
    }

    @Before
    public void setUp() throws Exception {
        userAdmin = userRepository.save(new User(1, "Admin", "Admin@mail.ru", "adminAdmin", Role.ADMIN));
        userTest = userRepository.save(new User(2, "User", "user@mail.ru", "user-user", Role.USER));
    }

    @After
    public void tearDown() throws Exception {
        userRepository.delete(userAdmin.id());
        userRepository.delete(userTest.id());
    }

    @Test
    public void save() {
        User user = new User(3, "Ackerman", "ac@mail.ru", "ac", Role.ADMIN);
        User userSaved = userRepository.save(user);
        assertNotNull(userSaved);
        assertEquals(user.getName(), userSaved.getName());
        assertEquals(user.getPassword(), userSaved.getPassword());
        assertEquals(user.getEmail(), userSaved.getEmail());
        assertEquals(user.getRoles().stream().findFirst(), userSaved.getRoles().stream().findFirst());



    }

    @Test
    public void delete() {
        User saved  = userRepository.save(new User(userTest));
        assertNotNull(saved);
        assertTrue(userRepository.delete(saved.id()));
    }

    @Test
    public void get() {
        assertNotNull(userRepository.get(userTest.id()));
        assertNotNull(userRepository.get(userAdmin.id()));
    }

    @Test
    public void getByEmail() {
        assertNotNull(userRepository.getByEmail(userAdmin.getEmail()));
        assertNotNull(userRepository.getByEmail(userTest.getEmail()));
    }

    @Test
    public void getAll() {
        List<User> users = userRepository.getAll();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }
}