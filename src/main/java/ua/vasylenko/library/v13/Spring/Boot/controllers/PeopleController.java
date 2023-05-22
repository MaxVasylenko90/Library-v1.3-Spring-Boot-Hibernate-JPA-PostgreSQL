package ua.vasylenko.library.v13.Spring.Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.vasylenko.library.v13.Spring.Boot.models.Person;
import ua.vasylenko.library.v13.Spring.Boot.services.PeopleService;
import ua.vasylenko.library.v13.Spring.Boot.util.PersonValidator;


@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("peopleList", peopleService.getPeople());
        return "people/index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("person") Person person) {
        return "people/newUser";
    }

    @PostMapping()
    public String createNewUser(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/newUser";
        peopleService.create(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getPerson(id));
        model.addAttribute("books", peopleService.getBooksByPerson(id));
        return "people/userPage";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int userId, Model model) {
        model.addAttribute("user", peopleService.getPerson(userId));
        return "people/editUser";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user")Person person, @PathVariable("id") int userId) {
        peopleService.update(userId, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        peopleService.delete(userId);
        return "redirect:/people";
    }
}