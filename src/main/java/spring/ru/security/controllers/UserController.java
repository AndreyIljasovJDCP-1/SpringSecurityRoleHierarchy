package spring.ru.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.ru.security.models.User;
import spring.ru.security.services.UserService;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model, @RequestParam String role) {
        if (!userService.createUser(user,role)) {
            model.addAttribute("errorCreateUser", user.getEmail());
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String welcomePage(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "welcomepage";
    }

    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "about";
    }

    @GetMapping("/adminpage")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "adminpage";
    }

    @GetMapping("/leadpage")
    @PreAuthorize("hasRole('LEAD')")
    public String leadPage() {
        return "leadpage";
    }

    @GetMapping("/userpage")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public String userPage() {
        return "userpage";
    }

    @GetMapping("/allpage")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public String allPage() {
        return "allpage";
    }

    @GetMapping("/user/{user}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public String userInfo(@PathVariable User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "user-info";
    }
}
