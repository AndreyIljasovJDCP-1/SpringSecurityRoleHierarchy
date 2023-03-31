package spring.ru.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "email", unique = true)
    private String email;

    @Column(length = 1000)
    private String password;
    private String phoneNumber;
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
    private LocalDateTime createdDateTime;

    public User(String name, String email, String password, String phoneNumber, Collection<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    @PrePersist
    private void init() {
        createdDateTime = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    //security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        var listAuthorities = getRoleAuthorities();
        listAuthorities.addAll(getPrivilegeAuthorities());
        return listAuthorities;

    }

    private Collection<GrantedAuthority> getRoleAuthorities() {
        return new ArrayList<>(roles.stream()
                .map(Role::getRoleAuthority)
                .toList());
    }

    private Collection<GrantedAuthority> getPrivilegeAuthorities() {
        return roles.stream()
                .flatMap(role -> role.getPrivileges()
                        .stream()
                        .map(Privilege::getAuthority)
                )
                .toList();
    }
    public List<String> getRoleList() {
        return roles.stream()
                .map(Role::getName)
                .distinct()
                .toList();
    }
    public List<String> getPrivilegeList(String roleName) {
        return roles.stream()
                .distinct()
                .filter(role -> role.getName().equals(roleName))
                .flatMap(role -> role.getPrivileges().stream().map(Privilege::getName))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
