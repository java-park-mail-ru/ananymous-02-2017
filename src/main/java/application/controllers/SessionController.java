package application.controllers;

import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import application.utils.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = {"https:/soul-hunting.ru", "localhost"})
@RequestMapping("/api")
public class SessionController extends BaseController {

    public SessionController(@NotNull AccountService accountService)
    {
        super(accountService);
    }


    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody UserRequest body, HttpSession httpSession)
    {
        final String error = Validator.getUserError(body);

        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("User logged in this session"));
        } else if (accountService.isUniqueLogin(body.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(body.getLogin() + "already exist");
        } else if (accountService.isUniqueEmail(body.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(body.getEmail() + "already exist");
        }

        final Long id = accountService.signup(body);
        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new MessageResponse(id.toString()));
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody UsernameRequest body, HttpSession httpSession)
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
                    .body(new MessageResponse(String.format("username: %s, wrong password", username)));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new MessageResponse(id.toString()));
    }

    @PostMapping(path = "/logout", produces = "application/json")
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
