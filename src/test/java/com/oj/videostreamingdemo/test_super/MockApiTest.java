package com.oj.videostreamingdemo.test_super;


import com.oj.videostreamingdemo.config.TestProfiles;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * It is almost identical to {@code @WebMvcTest}
 *
 * Conduct a test that is difficult to proceed with in the integrated test.
 * It is usually used when rollback is difficult (like an external payment API.)
 * or you need to use a Mock Service or Object.
 *
 *
 * Use {@code @MockBean} or {@code @SpyBean} annotation to create Mock.
 */
@WebMvcTest
@ActiveProfiles(TestProfiles.TEST)
@Disabled
public class MockApiTest {
}
