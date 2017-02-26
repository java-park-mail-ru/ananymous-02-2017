package controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.RequestException;
import models.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import responses.ErrorResponse;
import responses.UserResponse;
import services.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
public class UserController {
    @NotNull
    private final AccountService accountService;

    public UserController(@NotNull AccountService accountService)
    {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity signup(@RequestBody User body, HttpSession httpSession)
    {
        if (getUserID(httpSession) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User logged in this session"));
        }
//        UserProfile userProfile;
        try {
            /*userProfile = */accountService.signup(body);
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
//        httpSession.setAttribute(SESSION_ID, userProfile.getId());
        return ResponseEntity.ok(null);
    }

    @RequestMapping(path = "/api/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity login(@RequestBody User body, HttpSession httpSession)
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
        return ResponseEntity.ok(null);
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET, produces = "application/json")
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

    @RequestMapping(path = "/api/change-pass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity changePassword(@RequestBody GetPasswordRequest body, HttpSession httpSession)
    {
        User user;
        try {
            user = getUserFromDB(httpSession);
            accountService.changePassword(user, body.getOldPassword(), body.getNewPassword());
        } catch (RequestException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
        }
        return ResponseEntity.ok(null);
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession)
    {
        if (getUserID(httpSession) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User logged out"));
        }
        httpSession.removeAttribute(httpSession.getId());
        return ResponseEntity.ok(null);
    }

    private User getUserFromDB(HttpSession httpSession) throws RequestException
    {
        String userID = getUserID(httpSession);
        return accountService.getUser(userID);
    }

    private String getUserID(HttpSession httpSession)
    {
        return (String) httpSession.getAttribute(httpSession.getId());
    }

    private static final class GetPasswordRequest
    {
        private String oldPassword, newPassword;

        @JsonCreator
        public GetPasswordRequest(@JsonProperty("oldPassword") String oldPassword,
                                  @JsonProperty("newPassword") String newPassword)
        {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        public String getOldPassword()
        {
            return oldPassword;
        }

        public String getNewPassword()
        {
            return newPassword;
        }
    }

}
