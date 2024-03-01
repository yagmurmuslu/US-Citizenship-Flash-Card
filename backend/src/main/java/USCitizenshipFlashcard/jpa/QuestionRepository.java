package USCitizenshipFlashcard.jpa;

import USCitizenshipFlashcard.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    //List<Question> findAll(); //get all question
    //void delete(Boolean correctOne); // for make restart

}
