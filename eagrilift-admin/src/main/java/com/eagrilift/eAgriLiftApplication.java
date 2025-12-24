package com.eagrilift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author eagrilift
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class eAgriLiftApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(eAgriLiftApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  e起富农管理系统启动成功   ლ(´ڡ`ლ)ﾞ " +
                ".__                                 .__\n" +
                "|  |__  __ _______      ____ _____  |__|\n" +
                "|  |  \\|  |  \\__  \\   _/ ___\\\\__  \\ |  |\n" +
                "|   Y  \\  |  // __ \\_ \\  \\___ / __ \\|  |\n" +
                "|___|  /____/(____  /  \\___  >____  /__|\n" +
                "     \\/           \\/       \\/     \\/"
        );
    }
}
