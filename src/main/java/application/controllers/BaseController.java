package application.controllers;

import application.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class BaseController {
    @NotNull
    protected final AccountService accountService;
    public static final String USER_ID = "userId";

    public BaseController(AccountService accountService) {
        this.accountService = accountService;
    }
}
