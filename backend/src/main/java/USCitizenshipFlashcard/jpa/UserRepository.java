package USCitizenshipFlashcard.jpa;

import USCitizenshipFlashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    <S extends User> S save(S entity); //save, create, update user
    @Override
    Optional<User> findById(Long primaryKey );

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername (String username);
    @Override
    void delete(User entity);

}
