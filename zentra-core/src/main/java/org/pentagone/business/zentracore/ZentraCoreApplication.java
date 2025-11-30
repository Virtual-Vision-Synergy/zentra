package org.pentagone.business.zentracore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "org.pentagone.business.zentracore",
    "org.pentagone.business.zentracore.hr.ai"
})
public class ZentraCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZentraCoreApplication.class, args);
    }

}
