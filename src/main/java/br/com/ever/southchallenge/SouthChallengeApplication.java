package br.com.ever.southchallenge;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableBatchProcessing
//@EnableScheduling
public class SouthChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SouthChallengeApplication.class, args);
    }
}
