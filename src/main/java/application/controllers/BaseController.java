package application.controllers;

import application.services.AccountService;

import javax.validation.constraints.NotNull;

public class BaseController {
    @NotNull
    protected final AccountService accountService;
    protected static final String USER_ID = "userID";

    public BaseController(AccountService accountService) {
        this.accountService = accountService;
    }
}
