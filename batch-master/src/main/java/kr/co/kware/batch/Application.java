package kr.co.kware.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:integration-master.xml")
public class Application {

    //@Value(value = "${foo.var}")
    private String xxx;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("BEAN NAME : " + name);
        }
    }
}
