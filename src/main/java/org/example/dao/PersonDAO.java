package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id){
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public void save(Person person){
        jdbcTemplate.update("INSERT INTO Person(FIO, year) VALUES (?, ?)",
                person.getFIO(), person.getYear());
    }

    public void update(int id, Person updatePerson){
        jdbcTemplate.update("UPDATE Person SET FIO=?, year=? WHERE id=?",
                updatePerson.getFIO(), updatePerson.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    //Для валидации уникальности ФИО
    public Optional<Person> getPersonByFullName(String FIO){
        return jdbcTemplate.query("SELECT * FROM Person WHERE FIO = ?", new Object[]{FIO},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    //Здесь Join не нужен. И так получили человека с помощью отдельного метода
    public List<Book> getBooksByPersonId(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
