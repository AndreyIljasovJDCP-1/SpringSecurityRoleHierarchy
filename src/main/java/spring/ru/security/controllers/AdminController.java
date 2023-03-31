package spring.ru.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.ru.security.models.Role;
import spring.ru.security.models.User;
import spring.ru.security.repositories.PrivilegeRepository;
import spring.ru.security.repositories.RoleRepository;
import spring.ru.security.services.RoleService;
import spring.ru.security.services.UserService;

import java.security.Principal;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/user")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    RoleService roleService;

    @GetMapping()
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.all());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "admin";
    }

    @GetMapping("/edit/{user}")
    public String editUser(@PathVariable User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "user-edit";
    }

    @PostMapping("/edit/{user}")
    public String editUserRole(@PathVariable User user, @RequestParam("roles") String[] roles) {
        userService.setRoles(user, roles);
        return "redirect:/admin/user";
    }

    @GetMapping("/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUser(id);
        return "redirect:/admin/user";
    }

    @GetMapping("/role/edit/{role}")
    public String editRole(@PathVariable Role role, Model model, Principal principal) {
        model.addAttribute("role", role);
        model.addAttribute("listPrivileges", privilegeRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "role-edit";
    }

    @PostMapping("/role/edit/{role}")
    public String editRolePrivilege(@PathVariable Role role, @RequestParam("privileges") String[] privileges) {
        roleService.setRolePrivileges(role, privileges);
        return "redirect:/admin/user";
    }

    @GetMapping("/role/create")
    public String addRole(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "role-create";
    }

    @PostMapping("/role/create")
    public String createRole(@RequestParam("role") String role, @RequestParam("privileges") String[] privileges) {
        roleService.addRoleIfNotFound(role, privileges);
        return "redirect:/admin/user";
    }
}
