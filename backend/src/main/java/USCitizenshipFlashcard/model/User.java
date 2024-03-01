package USCitizenshipFlashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "user_id")
    private long id;
    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 3, max = 15)
    private String username;
    @Column(name = "firstname", nullable = false)
    @Size (min = 3, max = 15)
    private String firstname;
    @Column(name = "lastname", nullable = false)
    @Size (min = 3, max = 15)
    private String lastname;
    @Column(name = "password_hash", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "question")
    private String question;


    @OneToMany (mappedBy = "user")
    private Set<Question> questions;

}
