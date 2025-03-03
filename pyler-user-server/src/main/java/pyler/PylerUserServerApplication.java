package pyler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"pyler"})
@EntityScan(basePackages = {"pyler.domain.entity"})
@EnableJpaRepositories(basePackages = {"pyler.repository"})
public class PylerUserServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PylerUserServerApplication.class, args);
    }
}