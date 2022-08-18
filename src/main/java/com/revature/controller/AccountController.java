package com.revature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exception.InvalidParameterException;
import com.revature.model.User;
import com.revature.service.AccountService;
import io.javalin.Javalin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class AccountController implements Controller{
    private AccountService accountService;

    public AccountController() {
        accountService = new AccountService();
    }
    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/accounts", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            User myUser = (User) session.getAttribute("logged_in_user");

            ObjectMapper om = new ObjectMapper();
            Map<String, String> newAccount = om.readValue(ctx.body(), Map.class);
            try {
                ctx.json(accountService.openAccount(newAccount));
                ctx.status(201);
            } catch (InvalidParameterException e) {
                ctx.json(e.getMessages());
                ctx.status(400);
            }
        });

        app.put("/accounts", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            User myUser = (User) session.getAttribute("logged_in_user");
            ctx.json(accountService.linkUserToAccount(4, 5));
            ctx.status(200);
        });

        app.delete("/accounts", ctx -> {
           HttpServletRequest req = ctx.req;
           HttpSession session = req.getSession();
           User myUser = (User) session.getAttribute("logged_in_user");
           ctx.json(accountService.unlinkUserFromAccount(4,5));
           ctx.status(200);
        });

        app.get("/accounts", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            User myUser = (User) session.getAttribute("logged_in_user");

            ctx.json(accountService.getAccountsByEmail("jd80@a.ca"));
            ctx.status(200);
        });

        app.get("/account", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            User myUser = (User) session.getAttribute("logged_in_user");
            ctx.json(accountService.getAccountByEmailAndAccountId("jd80@a.ca", 1));
            ctx.status(200);
        });
    }


}