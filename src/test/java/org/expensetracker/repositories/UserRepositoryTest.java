package org.expensetracker.repositories;

import org.expensetracker.model.users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testAddAndFindUser() {
        users user = new users();
        user.setUsername("repoUser");
        user.setPassword("repoPass");
        userRepository.save(user);

        users found = userRepository.findByUsername("repoUser");
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("repoUser");
        assertThat(found.getPassword()).isEqualTo("repoPass");
    }
}

