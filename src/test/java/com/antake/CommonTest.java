package com.antake;

import com.baomidou.mybatisplus.core.toolkit.AES;
import org.junit.jupiter.api.Test;

public class CommonTest {
    @Test
    public void test(){
        // 随机密钥加密
        String url = AES.encrypt("jdbc:p6spy:mysql://rm-bp1jj55100iqhs8e1to.mysql.rds.aliyuncs.co" +
                "m:3306/sljf_equity?useUnicode=true&characterEncoding=utf-8&useSSL=f" +
                "alse&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&zeroD" +
                "ateTimeBehavior=convertToNull", "4d44801e4df0a5a3");
        String user = AES.encrypt("slb_data_test","4d44801e4df0a5a3");
        String password = AES.encrypt("RAPrhVi6kS9XIKEoZMgjQAxw$nuzZ#4i","4d44801e4df0a5a3");
        String privateKey = AES.encrypt("MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAvhfXtA8NzZcpBwqyU2ou/3F1Lj0hA2ah9v2tyk440N3tybqdhrvfHMrypZ+NyCbV8+bU2NHp1jQh9I59jHAsSwIDAQABAkEArxC+MrF6gNCplDahrHip987BoGfjs4Idv545I4uOf6uM0Fk8oKnoN57oTN1sJ7MfRl19E8LUZX9xzSACxcNTgQIhAOZwyG2+GDKIMK7gD3hPJyGOAq8EASZLT3GG0AyRFiU7AiEA0y1rZ5OtfmR9oF+FoOT9kkGQUjWFp5kg4zScmDd4ZDECIQCIdbcqzYwUuHNjy0k97Fl21Fqge1WW8LftuXCQUIZ1qwIgYqSjuAU2lISyXfhgvjBY9jmkBZK0tP4Fa7xGrZ+zn/ECIAyxdZjbjD7gedkfstF10qqH99AmwWS+RkCMDRT5NEk8","4d44801e4df0a5a3");
        System.out.println("mpw:"+url);
        System.out.println("mpw:"+user);
        System.out.println("mpw:"+password);
        System.out.println("mpw:"+privateKey);
    }
}
