package application.controllers;

import application.services.AccountService;
import application.utils.requests.UserRequest;
import application.utils.responses.IdResponse;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionControllerTest {
    @Autowired
    private AccountService usersService;
    @Autowired
    private TestRestTemplate template;

    @NotNull
    private HttpEntity getHttpEntity(@NotNull Object body, @Nullable List<String> cookie) {
        if (cookie != null) {
            final HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookie);
            return new HttpEntity(body, headers);
        } else {
            return new HttpEntity(body);
        }
    }

    private @Nullable List<String> signup(@NotNull String login, @NotNull String email, @NotNull String password,
                                          @NotNull HttpStatus status, @Nullable List<String> cookie) {

        final UserRequest userRequest = new UserRequest(login, email, password);
        final HttpEntity entity = getHttpEntity(userRequest, cookie);

        final ResponseEntity<String> response = template.postForEntity("/api/signup", entity, String.class);
        Assert.assertEquals(status, response.getStatusCode());

        return response.getHeaders().get("Set-Cookie");
    }



    @Test
    public void testSignup() {
        final String login = "login";
        final String email = "email@mail.ru";
        final String password = "qwerty123";

        List<String> cookie = signup("", email, password, HttpStatus.BAD_REQUEST, null);

        cookie = signup(login, email, password, HttpStatus.OK, cookie);
        signup("uniq", "uniq@mail.ru", password, HttpStatus.OK, cookie);

    }

    @Before
    @After
    public void clear() {
        usersService.clear();
    }
}
