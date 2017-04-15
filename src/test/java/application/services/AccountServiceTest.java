package application.services;

import application.models.User;
import application.utils.requests.UserRequest;
import application.utils.responses.FullUserResponse;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountServiceTest {
    private static final String PASSWORD = "qwerty123";
    private static final Long INVALID_ID = Long.MAX_VALUE;
    private static final int INVALID_PAGE = Integer.MAX_VALUE;

    @Autowired
    private AccountService accountService;

    @Before
    public void before() {
        accountService.clear();
        addDefaultUser();
    }

    @NotNull
    private UserRequest getDefaultUserRequest() {
        return new UserRequest("test", "test@mail.ru", PASSWORD);
    }

    private @Nullable Long addUser(@NotNull String login, @NotNull String email, @NotNull String password) {
        return accountService.addUser(new UserRequest(login, email, password));
    }

    private @Nullable Long addDefaultUser() {
        return accountService.addUser(getDefaultUserRequest());
    }

    @Test
    public void testUsersNotEmpty() {
        Assert.assertFalse(accountService.getUsers().isEmpty());
    }

    @Test
    public void testConflict() {
        Assert.assertNull(addDefaultUser());
    }

    @Test
    public void testAddUser() {
        final Long id2 = addUser("test2", "test2@mail.ru", PASSWORD);
        Assert.assertNotNull(id2);
    }

    @Test
    public void testGetNonExistingUser() {
        Assert.assertNull(accountService.getUser(0L));
        Assert.assertNull(accountService.getUser(INVALID_ID));
    }

    @Test
    public void testAddedAndGettedUserMatches() {
        final String login = "test123";
        final String email = "test123@mail.ru";
        final Long id = addUser(login, email, PASSWORD);
        final User user = accountService.getUser(id);
        Assert.assertEquals(login, user.getLogin());
        Assert.assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetNonExistingUsername() {
        Assert.assertNull(accountService.getUserID("not_exist"));
    }

    @Test
    public void testGetUserByUsername() {
        Assert.assertNotNull(accountService.getUserID(getDefaultUserRequest().getLogin()));
        Assert.assertNotNull(accountService.getUserID(getDefaultUserRequest().getEmail()));
    }

    @Test
    public void testCheckUserAccount() {
        final String login = "ulogin";
        final String email = "uemail@mail.ru";
        final String rawPassword = "rawPassword";
        Long id = addUser(login, email, rawPassword);
        final User user = accountService.getUser(id);
        Assert.assertFalse(accountService.checkUserAccount(id, user.getPassword()));
        Assert.assertTrue(accountService.checkUserAccount(id, rawPassword));

        Assert.assertFalse(accountService.checkUserAccount(INVALID_ID, user.getPassword()));
    }

    @Test
    public void testChangePassword() {
        final String login = "ulogin";
        final String email = "uemail@mail.ru";
        final String rawPassword = "rawPassword";
        final String newPassword = "newPassword";
        Long id = addUser(login, email, rawPassword);
        final User user = accountService.getUser(id);

        Assert.assertTrue(accountService.changePassword(user, rawPassword, newPassword));

        final User userWithAnotherPassword = accountService.getUser(id);

        Assert.assertTrue(accountService.checkUserAccount(id, newPassword));
    }

    private void addNUsers(int count) {
        for (int i = 0; i < count; i++) {
            addUser("login" + i, "email" + i + "@mail.ru", PASSWORD);
        }
    }

    @Test
    public void testGetUsers() {
        final List<FullUserResponse> users = accountService.getUsers();
        final int count = 5;
        addNUsers(count);
        final List<FullUserResponse> usersWithNew = accountService.getUsers();
        Assert.assertEquals(usersWithNew.size() - users.size(), count);
    }

    @Test
    public void testGetUsersByPage() {
        final int count = 10;
        addNUsers(count);
        Assert.assertTrue(accountService.getUsers(0, -1).isEmpty());
        Assert.assertTrue(accountService.getUsers(INVALID_PAGE, 1).isEmpty());

        Assert.assertEquals(accountService.getUsers().size(), accountService.getUsers(0, Integer.MAX_VALUE).size());
        Assert.assertEquals(accountService.getUsers(0, count).size(), count);
    }

}
