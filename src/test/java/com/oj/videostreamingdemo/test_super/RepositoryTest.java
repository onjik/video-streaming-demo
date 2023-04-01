package com.oj.videostreamingdemo.test_super;



import com.oj.videostreamingdemo.config.TestProfiles;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * Repository test based on {@code @DataJpaTest}
 * and use database specified by the "test-profile"
 */
@DataJpaTest
@ActiveProfiles(TestProfiles.TEST)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Disabled
public class RepositoryTest {
}
