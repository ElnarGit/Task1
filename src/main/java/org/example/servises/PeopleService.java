package org.example.servises;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.PeopleRepositories;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> findAll(){
        return peopleRepositories.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepositories.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson){
        updatePerson.setId(id);

        peopleRepositories.save(updatePerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> getPersonByFIO(String FIO){
      return peopleRepositories.findByFIO(FIO);
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepositories.findById(id);

        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            //Мы внизу итерируемся по книгам, поэтому они точно будут загружены, но на всякий случай
            // не мешает всегда вызывать Hibernate.initialize()
            //(на случай, например, если код в дальнейшим поменяется и итерация по книгам удалится)

            person.get().getBooks().forEach(book -> {
                long diffInMills = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if(diffInMills > 864000000)
                    book.setExpired(true);
            });

            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
