package com.antake;


import com.antake.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EquityApplicationTests {
    @Test
    void test(){
        System.out.println(PasswordUtils.encode("antake666666", "7fcec916-e04c-4917-9877-9cf98f2e140c"));
    }
}
