package ua.vasylenko.library.v13.Spring.Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.vasylenko.library.v13.Spring.Boot.models.Book;
import ua.vasylenko.library.v13.Spring.Boot.models.Person;
import ua.vasylenko.library.v13.Spring.Boot.services.BooksService;
import ua.vasylenko.library.v13.Spring.Boot.services.PeopleService;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping
    public String bookList(Model model, @RequestParam(value = "page") Optional<Integer> pageNumber,
                           @RequestParam(value = "books_per_page", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "sort_by", defaultValue = "name") Optional<String> sort) {
            if (pageNumber.isEmpty())
                return "redirect:/books?page=0&books_per_page=5";
            Page page = booksService.findAll(pageNumber.get(), pageSize, sort.get());
            List<Book> bookList = page.getContent();
            model.addAttribute("bookList", bookList);
            model.addAttribute("currentPage", pageNumber.get() + 1);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("sortSuffix", sort.get());
        return "books/index";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchResult(Model model, @RequestParam("searchQuery") String query) {
        model.addAttribute("bookList", booksService.findByNameContainingIgnoreCase(query));
        return "books/search";
    }



    @PostMapping()
    public String newBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/newBook";
        booksService.addNewBook(book);
        return "redirect:/books?page=0&books_per_page=5";
    }

    @GetMapping("/new")
    public String addNewBook(@ModelAttribute("book") Book book) {
        return "books/newBook";
    }

    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, @ModelAttribute("editedPerson") Person person, Model model) {
        Book book = booksService.getBook(id);
        model.addAttribute("book", book);
        Optional<Person> assignedPerson = booksService.getPerson(book);
        if (assignedPerson.isPresent()) model.addAttribute("owner", assignedPerson.get());
        else model.addAttribute("people", peopleService.getPeople());
        return "books/bookPage";
    }

    @PatchMapping("/{id}/assign")
    public String bookAssign(@PathVariable("id") int bookId, @ModelAttribute("editedPerson") Person person) {
        booksService.assignPerson(bookId, person.getId());
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        booksService.releaseBook(bookId);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.deleteBook(id);
        return "redirect:/books?page=0&books_per_page=5";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int bookId, Model model) {
        model.addAttribute("book", booksService.getBook(bookId));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int bookId, @ModelAttribute("book") Book book) {
        booksService.update(bookId, book);
        return "redirect:/books/{id}";
    }
}
