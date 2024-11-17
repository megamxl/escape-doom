package com.escapedoom.lector.portal.dataaccess;

import com.escapedoom.lector.portal.dataaccess.config.TestApplication;
import com.escapedoom.lector.portal.dataaccess.config.TestJpaConfig;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.model.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllUsers() {
        var users = userRepository.findAll();
        assertThat(users)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    public void testFindUserById() {
        long userId = 1L;
        Optional<User> user = userRepository.findById(userId);

        assertThat(user)
                .isPresent()
                .get()
                .satisfies(u -> {
                    assertThat(u.getUserId()).isEqualTo(userId);
                    assertThat(u.getFirstname()).isEqualTo("John");
                    assertThat(u.getEmail()).isEqualTo("john.doe@example.com");
                    assertThat(u.getRole()).isEqualTo(Role.USER);
                });
    }

    @Test
    public void testFindUserByEmail() {
        String email = "leon@doom.at";
        Optional<User> user = userRepository.findByEmail(email);

        assertThat(user)
                .isPresent()
                .get()
                .satisfies(u -> {
                    assertThat(u.getEmail()).isEqualTo(email);
                    assertThat(u.getFirstname()).isEqualTo("fre");
                    assertThat(u.getLastname()).isEqualTo("leon");
                    assertThat(u.getRole()).isEqualTo(Role.LECTOR);
                });
    }

    @Test
    @Transactional
    public void testAddUser() {
        User newUser = User.builder()
                .firstname("Alice")
                .lastname("Smith")
                .email("alice.smith@example.com")
                ._password("password123")
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(userRepository.findById(savedUser.getUserId()))
                .isPresent()
                .get()
                .satisfies(user -> {
                    assertThat(user.getFirstname()).isEqualTo("Alice");
                    assertThat(user.getLastname()).isEqualTo("Smith");
                    assertThat(user.getEmail()).isEqualTo("alice.smith@example.com");
                    assertThat(user.getRole()).isEqualTo(Role.USER);
                });
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User newUser = User.builder()
                .firstname("Bob")
                .lastname("Brown")
                .email("bob.brown@example.com")
                ._password("password456")
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);

        userRepository.delete(savedUser);

        assertThat(userRepository.findById(savedUser.getUserId())).isNotPresent();
    }
}
