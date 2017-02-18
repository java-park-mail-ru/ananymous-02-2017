package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Solovyev on 09/02/2017.
 */
@RestController
public class UserController {



    /**
     * Данный метод вызывается с помощью reflection'a, поэтому Spring позволяет инжектить в него аргументы.
     * Подробнее можно почитать в сорцах к аннотации {@link RequestMapping}. Там описано как заинжектить различные атрибуты http-запроса.
     * Возвращаемое значение можно так же варьировать. Н.п. Если отдать InputStream, можно стримить музыку или видео
     */
    @RequestMapping(path = "/api/user", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public GetMsgRequest getMsg(@RequestBody GetMsgRequest body, HttpSession httpSession) {
        httpSession.setAttribute("userId", body.getUserId());
        return body;
    }

    /**
     * Конструктор тоже будет вызван с помощью reflection'а. Другими словами, объект создается через ApplicationContext.
     * Поэтому в нем можно использовать DI. Подробнее про это расскажу на лекции.
     */
    public UserController() {
    }

    private static final class GetMsgRequest {
        int userId;

        @JsonCreator
        public GetMsgRequest(@JsonProperty("userId") int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }
    }
}
