package top.tony.spring.aop.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring AOP!";
    }

    @GetMapping("/validation/{number}")
    public String validation(
            @PathVariable int number
    ) {
        if (number == 66) {
            throw new IllegalArgumentException("Execute order 66");
        }
        return "Validation AOP";
    }

    @GetMapping("/modification/{number}")
    @ModifyParameter
    public String modification(
            @PathVariable int number
    ) {
        return "Number is always " + number;
    }
}
