package application.controllers;

import application.services.AccountService;
import application.utils.requests.UserRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionControllerTest {
    static final String LOGIN = "login";
    static final String EMAIL = "email@mail.ru";
    static final String PASSWORD = "qwerty123";

    @Autowired
    private AccountService usersService;
    @Autowired
    private TestRestTemplate template;

    @NotNull
    private HttpEntity getHttpEntity(@NotNull Object body, @Nullable List<String> cookie) {
        if (cookie != null) {
            final HttpHeaders headers = new HttpHeaders();
            headers.put("Cookie", cookie);
            return new HttpEntity(body, headers);
        } else {
            return new HttpEntity(body);
        }
    }

    private @Nullable List<String> signup(@NotNull String login, @NotNull String email, @NotNull String password,
                                          @NotNull HttpStatus status, @Nullable List<String> cookie) {

        final UserRequest userRequest = new UserRequest(login, email, password);
        final HttpEntity entity = getHttpEntity(userRequest, cookie);

        final ResponseEntity<String> response = template.exchange("/api/signup", HttpMethod.POST, entity, String.class);
        Assert.assertEquals(status, response.getStatusCode());

        return response.getHeaders().get("Set-Cookie");
    }

    @Test
    public void testFullRegistration() {
        List<String> cookie = signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);

    }

    @Test
    public void testConflictSignup() {
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.CONFLICT, null);
    }

    @Test
    public void testCorrectSignup() {
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
    }

    @Test
    public void testAuthorizedSignup() {
        List<String> cookie = signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
        signup("uniq", "uniq@mail.ru", PASSWORD, HttpStatus.FORBIDDEN, cookie);
    }

    @Test
    public void testBadRequestSignup() {
        signup("", EMAIL, PASSWORD, HttpStatus.BAD_REQUEST, null);
    }

    @After
    public void clear() {
        usersService.clear();
    }
}