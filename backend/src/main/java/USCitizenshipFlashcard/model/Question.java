package USCitizenshipFlashcard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "question_id")
    private Long id;
    @Column (name = "questions")
    private String question;
    @Column(name = "correct_count")
    private int correctCount;
    @Column(name = "wrong_count")
    private int wrongCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
