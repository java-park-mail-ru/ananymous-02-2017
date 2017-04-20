package application.controllers;

import application.services.AccountService;
import application.utils.requests.UserRequest;
import application.utils.responses.IdResponse;
import application.utils.responses.MessageResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionControllerTest {
    @Autowired
    private AccountService usersService;
    @Autowired
    private TestRestTemplate template;

    @Test
    public void testAll() {
        final String login = "login";
        final String email = "email@mail.ru";
        final String password = "qwerty123";
        final ResponseEntity<MessageResponse> badResponse = template.postForEntity("/api/signup",
                new UserRequest("", email, password), MessageResponse.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, badResponse.getStatusCode());

        final ResponseEntity<IdResponse> signupResponse2 = template.postForEntity("/api/signup",
                new UserRequest(login, email, password), IdResponse.class);
        Assert.assertEquals(HttpStatus.OK, signupResponse2.getStatusCode());

//        final ResponseEntity<MessageResponse> loginResponse = template.postForEntity("/api/signin",
//                new UsernameRequest(login, password), MessageResponse.class);
//        Assert.assertEquals(HttpStatus.FORBIDDEN, loginResponse.getStatusCode());
    }

    @After
    public void clear() {
        usersService.clear();
    }
}
