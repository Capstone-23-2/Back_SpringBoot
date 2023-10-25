package capstone.rtou.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.File;

/**
 * User DB
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15, unique = true)
    private String userId;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 16)
    private String name;
    @Lob
    @Column(name = "img")
    private byte[] img;
    @Column(name = "age")
    private int age;

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public byte[] getImg() {
        return img;
    }

    public int getAge() {
        return age;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
