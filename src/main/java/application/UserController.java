package application;

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

    public UserController(@NotNull AccountService accountService)
    {
        this.accountService = accountService;
    }

    @GetMapping("/api/status")
    public ResponseEntity status()
    {
        return ResponseEntity.ok(new StatusResponse("OK"));
    }

    @PostMapping(path = "/api/signup", produces = "application/json", consumes = "application/json")
    public ResponseEntity signup(@RequestBody User body, HttpSession httpSession)
    {
        if (getUserID(httpSession) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User logged in this session"));
        }
        try {
            accountService.signup(body);
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping(path = "/api/signin", produces = "application/json", consumes = "application/json")
    public ResponseEntity signin(@RequestBody User body, HttpSession httpSession)
    {
        if (getUserID(httpSession) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User logged in this session"));
        }
        String userID;
        try {
            userID = accountService.login(body.getLogin(), body.getPassword());
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
        httpSession.setAttribute(httpSession.getId(), userID);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/api/user", produces = "application/json")
    public ResponseEntity getUser(HttpSession httpSession)
    {
        User user;
        try {
            user = getUserFromDB(httpSession);
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new UserResponse(user.getLogin(), user.getEmail()));
    }

    @PostMapping(path = "/api/change-pass", produces = "application/json", consumes = "application/json")
    public ResponseEntity changePassword(@RequestBody PasswordRequest body, HttpSession httpSession)
    {
        User user;
        try {
            user = getUserFromDB(httpSession);
            accountService.changePassword(user, body.getOldPassword(), body.getNewPassword());
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok("Success");
    }

    @PostMapping(path = "/api/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if (getUserID(httpSession) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User logged out"));
        }
        httpSession.removeAttribute(httpSession.getId());
        return ResponseEntity.ok("Success");
    }

    private User getUserFromDB(HttpSession httpSession) throws RequestException
    {
        String userID = getUserID(httpSession);
        if (userID == null) {
            throw new RequestException(HttpStatus.UNAUTHORIZED, "User logged out");
        }
        return accountService.getUser(userID);
    }

    private String getUserID(HttpSession httpSession)
    {
        return (String) httpSession.getAttribute(httpSession.getId());
    }


}
