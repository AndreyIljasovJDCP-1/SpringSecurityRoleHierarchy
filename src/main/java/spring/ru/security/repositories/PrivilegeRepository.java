package spring.ru.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ru.security.models.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);
}
