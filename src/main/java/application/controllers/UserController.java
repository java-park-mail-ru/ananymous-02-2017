package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.PasswordRequest;
import application.utils.responses.FullUserResponse;
import application.utils.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin/*(origins = {"https:/soul-hunting.ru", "localhost"})*/
@RequestMapping("/api")
public class UserController extends BaseController {
    private static final int PAGE_COUNT = 5;

    public UserController(@NotNull AccountService accountService)
    {
        super(accountService);
    }

    @GetMapping(path = "/cur-user", produces = "application/json")
    public ResponseEntity getCurrentUser(HttpSession httpSession) {
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User not logged in"));
        }
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, bad cookies", id)));
        }
        return ResponseEntity.ok(new FullUserResponse(id, user.getLogin(), user.getEmail()));
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public ResponseEntity getUser(@PathVariable Long id)
    {
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, user not found", id)));
        }
        return ResponseEntity.ok(new FullUserResponse(user.getId(), user.getLogin(), user.getEmail()));
    }

    @GetMapping(path = "/users", produces = "application/json")
    public ResponseEntity getUsers(@RequestParam(value = "page", defaultValue = "0") int page)
    {
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid page"));
        } else if (page == 0) {
            return ResponseEntity.ok(accountService.getUsers());
        } else {
            return ResponseEntity.ok(accountService.getUsers((page - 1) * PAGE_COUNT, PAGE_COUNT));
        }
    }

    @PostMapping(path = "/change-pass", consumes = "application/json", produces = "application/json")
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
        final User user = accountService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(String.format("id: %s, bad cookies", id)));
        }
        final boolean isSuccess = accountService.changePassword(user, body.getOldPassword(), body.getNewPassword());
        if (!isSuccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse(String.format("id: %s, wrong password", id)));
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
}
