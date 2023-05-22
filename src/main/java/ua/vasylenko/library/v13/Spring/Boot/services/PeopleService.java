package ua.vasylenko.library.v13.Spring.Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.vasylenko.library.v13.Spring.Boot.models.Book;
import ua.vasylenko.library.v13.Spring.Boot.models.Person;
import ua.vasylenko.library.v13.Spring.Boot.repositories.PeopleRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getPeople() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void create(Person person) {
        peopleRepository.save(person);
    }

    public Person getPerson(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> getPerson(String email) {
        return peopleRepository.findByEmail(email);
    }

    @Transactional
    public void update(int userId, Person person) {
        person.setId(userId);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int userId) {
        peopleRepository.deleteById(userId);
    }

    public List<Book> getBooksByPerson(int id) {
        Person person = getPerson(id);
        Hibernate.initialize(person.getBooks());
        person.getBooks().forEach(book -> {
            long diffInMillis = new Date().getTime() - book.getTakenAt().getTime();
            if(diffInMillis > 864000000)  // 10 days
                book.setExpired(true);
        });
        return person.getBooks();
    }
}
