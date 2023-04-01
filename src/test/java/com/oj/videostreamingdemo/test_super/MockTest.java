package com.oj.videostreamingdemo.test_super;


import com.oj.videostreamingdemo.config.TestProfiles;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


/**
 * Base test class for testing with Mock
 * Mainly used for service testing.
 *
 * testing with Mock
 * is faster than integration test and
 * helps you do a unit test that focuses on one function
 *
 * you can use Mockito's function and annotation :
 * ex: {@code @Mock} and {@code @InjectMocks} and {@code @Spy}
 *
 * https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md#%EC%84%9C%EB%B9%84%EC%8A%A4-%ED%85%8C%EC%8A%A4%ED%8A%B8
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(TestProfiles.TEST)
@Disabled
public class MockTest {
}
