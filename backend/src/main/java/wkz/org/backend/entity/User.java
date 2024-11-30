package wkz.org.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "`user`")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn // User的主键作为UserAuth的外键
    private UserAuth userAuth;

    // 用户权限
    @Column(name = "role")
    private String role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // 电话
    @Column(name = "phone")
    private String phone;

    // 签名
    @Column(name = "signature")
    private String signature;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.userAuth = new UserAuth(password);
        this.userAuth.setUser(this);
        this.role = "GUEST";
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User() {
    }

    public User(String firstName, String lastName, String email, String phone, String signature) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.signature = signature;
    }
}
