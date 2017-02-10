package sample;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Solovyev on 09/02/2017.
 */
@RestController
public class UserController {

    @RequestMapping(path = "/api/user/{userId}", method = RequestMethod.GET)
    public String getMsg(@PathVariable(name = "userId") int userId) {
        return "{\"userId\":" + userId + '}';
    }
}
