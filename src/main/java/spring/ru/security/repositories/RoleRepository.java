package spring.ru.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ru.security.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
