package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.TESTMEAL;
import static ru.javawebinar.topjava.MealTestData.TESTMEAL2;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-db.xml",
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now(),"TestFood",575);
        Meal created = service.create(newMeal,100001);
        newMeal.setId(created.getId());
        assertMatch(service.get(100004,100001), newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(106,106);
    }

    @Test
    public void delete() throws Exception {
        service.delete(100003,100001);
        assertMatch(service.getAll(100001), service.get(100002,100001));
    }


    @Test
    public void update() throws Exception {
        Meal updated = service.get(100003,100001);
        updated.setDescription("UpdatedName");
        updated.setCalories(1000);
        service.update(updated,100001);
        assertMatch(service.get(100003,100001), updated);
    }

    @Test
    public void get() {
        Meal expectedMeal = new Meal(100002,
                LocalDateTime.now(),
                "test_food",1333);
        Meal meal = service.get(100002,100001);
        assertMatch(meal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(106,106);
    }


    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(100001);
        assertMatch(all,TESTMEAL,TESTMEAL2);
    }


}
