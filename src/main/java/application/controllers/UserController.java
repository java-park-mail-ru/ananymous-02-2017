package application.controllers;

import application.models.User;
import application.models.UserInfo;
import application.requests.PasswordRequest;
import application.requests.UserRequest;
import application.responses.MessageResponse;
import application.services.AccountService;
import application.utils.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@SuppressWarnings("MalformedFormatString")
@RestController
@CrossOrigin
public class UserController {
    @NotNull
    private final AccountService accountService;
    private static final String USER_ID = "userID";

    public UserController(@NotNull AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PostMapping(path = "/api/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody User body, HttpSession httpSession)
    {
        final String error = Validator.getUserError(body);

        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("User logged in this session"));
        } else if (accountService.isUserExists(body.getLogin()) || accountService.isUserExists(body.getEmail())) {
            final MessageResponse response =  new MessageResponse(String
                    .format("login: %s, email: %s, user already exist", body.getLogin(), body.getEmail()));
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(response);
        }

        final Long id = accountService.signup(body);
        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new MessageResponse(id.toString()));
    }

    @PostMapping(path = "/api/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody UserRequest body, HttpSession httpSession)
    {
        final String error = Validator.getUserRequestError(body);

        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("User logged in this session"));
        }

        final String username = body.getUsername();
        final Long id = accountService.getUserID(username);

        if (id == null || !accountService.isUserExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("username: %s, user not found", username)));
        } else if (!accountService.checkUserAccount(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(String.format("username: %s, wrong username and/or password", username)));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new MessageResponse(id.toString()));
    }

    @GetMapping(path = "/api/cur-user", produces = "application/json")
    public ResponseEntity getCurrentUser(HttpSession httpSession) {
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        final UserInfo user = accountService.getUserInfo(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, bad cookies", id)));
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/api/users/{id}", produces = "application/json")
    public ResponseEntity getUser(@PathVariable Long id)
    {
        final UserInfo user = accountService.getUserInfo(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, user not found", id)));
        }
        return ResponseEntity.ok(user);
    }

//    @GetMapping(path = "/api/users", produces = "application/json")
//    public ResponseEntity getAllUsers()
//    {
//        final User user = new User("aaa", "aaa@mail.ru", "aaaaaaaa");
//        return ResponseEntity.ok((UserInfo) user);
////        return ResponseEntity.ok(accountService.getAllUsers());
//    }

    @PostMapping(path = "/api/change-pass", consumes = "application/json", produces = "application/json")
    public ResponseEntity changePassword(@RequestBody PasswordRequest body, HttpSession httpSession)
    {
        if (!(Validator.isPassword(body.getOldPassword()) && Validator.isPassword(body.getNewPassword()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Invalid password(s)"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        if (!accountService.isUserExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, bad cookies", id)));
        }
        final boolean isSuccess = accountService.changePassword(id, body.getOldPassword(), body.getNewPassword());
        if (!isSuccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(String.format("id: %s, wrong password", id)));
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
    }

    @PostMapping(path = "/api/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
}
