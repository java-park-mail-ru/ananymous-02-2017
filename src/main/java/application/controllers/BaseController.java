package application.controllers;

import application.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class BaseController {
    @NotNull
    protected final AccountService accountService;
    protected static final String USER_ID = "userID";

    public BaseController(AccountService accountService) {
        this.accountService = accountService;
    }
}
