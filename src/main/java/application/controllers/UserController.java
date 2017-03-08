package application.controllers;

import application.models.User;
import application.models.UserInfo;
import application.requests.PasswordRequest;
import application.requests.UserRequest;
import application.services.AccountService;
import application.utils.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

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

    @PostMapping(path = "/api/signup", consumes = "application/json")
    public ResponseEntity signup(@RequestBody User body, HttpSession httpSession)
    {
        String error = Validator.getUserError(body);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User logged in this session");
        } else if (accountService.isUserExists(body.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("login: {0}, email: {1}, user already exist", body.getLogin(), body.getEmail()));
        }

        Long id = accountService.signup(body);
        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok("Success");
    }

    @PostMapping(path = "/api/signin", consumes = "application/json")
    public ResponseEntity signin(@RequestBody UserRequest body, HttpSession httpSession)
    {
        String error = Validator.getUserRequestError(body);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User logged in this session");
        }
        String username = body.getUsername();
        Long id = accountService.getUserID(username);
        if (id == null || accountService.isUserExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("username: {0}, user not found", username));
        } else if (!accountService.checkUserAccount(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(String.format("username: {0}, wrong username and/or password", username));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/api/user", produces = "application/json")
    public ResponseEntity getUser(HttpSession httpSession)
    {
        Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        UserInfo user = accountService.getUserInfo(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("id: {0}, bad cookies", id));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/api/changePassword", consumes = "application/json")
    public ResponseEntity changePassword(@RequestBody PasswordRequest body, HttpSession httpSession)
    {
        if (!(Validator.isPassword(body.getOldPassword()) && Validator.isPassword(body.getNewPassword()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password(s)");
        }
        Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        if (!accountService.isUserExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("id: {0}, bad cookies", id));
        }
        boolean isSuccess = accountService.changePassword(id, body.getOldPassword(), body.getNewPassword());
        if (!isSuccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(String.format("id: {0}, wrong username and/or password", id));
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping(path = "/api/logout")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.ok("Success");
    }
}
