package wkz.org.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "`user_auth`")
public class UserAuth {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public UserAuth(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public UserAuth(String password) {
        this.password = password;
    }

    public UserAuth() {
    }
}
