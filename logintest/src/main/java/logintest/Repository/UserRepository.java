package logintest.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import logintest.Domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUserNo(Long userNo);
}