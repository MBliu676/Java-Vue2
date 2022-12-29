package com.kim.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author www
 * @title: KaptchaConfig
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/26 0:24
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer(){
        Properties properties = new Properties();
        properties.put("kaptcha.border","no");//是否有边框
        properties.put("kaptcha.textproducer.font.color","black"); // 文本颜色
        properties.put("kaptcha.textproducer.char.space",""); //每个字符之间的空格
        properties.put("kaptcha.image.height","40"); // 验证码高度
        properties.put("kaptcha.image.width","120");// 验证码 宽度
        properties.put("kaptcha.textproducer.font.size","30");// value的文字大小

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
