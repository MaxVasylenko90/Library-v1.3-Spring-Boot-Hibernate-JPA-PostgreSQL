package ua.vasylenko.library.v13.Spring.Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.vasylenko.library.v13.Spring.Boot.models.Book;
import ua.vasylenko.library.v13.Spring.Boot.services.BooksService;


@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (booksService.getBook(book.getName()).isPresent())
            errors.rejectValue("name", "", "This name is already taken");
    }
}
