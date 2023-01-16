package org.example.servises;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.BooksRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepositories booksRepositories;

    @Autowired
    public BooksService(BooksRepositories booksRepositories) {
        this.booksRepositories = booksRepositories;
    }

    public List<Book> findAll(boolean sortByYear){
        if(sortByYear)
            return booksRepositories.findAll(Sort.by("year"));
        else
            return booksRepositories.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear){
        if(sortByYear)
            return booksRepositories.findAll(PageRequest.of(page, booksPerPage,
                    Sort.by("year"))).getContent();
        else
            return booksRepositories.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }

    public Book findOne(int id){
        Optional<Book> foundBook = booksRepositories.findById(id);

        return foundBook.orElse(null);
    }

    public List<Book> searchByName(String query){
        return booksRepositories.findByNameStartingWith(query);

    }

    @Transactional
    public void save(Book book){
        booksRepositories.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook){
        Book bookToBeUpdated = booksRepositories.findById(id).get();

        updateBook.setId(id);
        updateBook.setOwner(bookToBeUpdated.getOwner()); // чтобы не терялась связь при обнавление

        booksRepositories.save(updateBook);
    }

    @Transactional
    public void delete(int id){
        booksRepositories.deleteById(id);
    }

    public Person getBookOwner(int id){
        //Здесь Hibernate.initialize не нужен, так как владелец (сторона One) загружается лениво

        return booksRepositories.findById(id).map(Book :: getOwner).orElse(null);
    }

    //Освобождает книгу (этот метод вызывается, когда человек возвращает книгу в библиотеку)
    @Transactional
   public void release(int id){
        booksRepositories.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });

   }
    //Назначает книгу человеку(этот метод вызывается когда человек забирает книгу из библиотеки)
    @Transactional
   public void assign(int id, Person selectedPerson){
        booksRepositories.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date()); // текущее время
                }
        );
   }
}
