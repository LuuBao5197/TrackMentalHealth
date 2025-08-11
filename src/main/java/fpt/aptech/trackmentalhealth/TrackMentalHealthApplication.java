package fpt.aptech.trackmentalhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//bat scheduling de theo doi ngay
@EnableScheduling
@SpringBootApplication
public class TrackMentalHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackMentalHealthApplication.class, args);
    }
}
