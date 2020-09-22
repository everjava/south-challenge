package br.com.ever.southchallenge;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableBatchProcessing
//@EnableScheduling
public class SouthChallengeApplication {
/*
    public static void main2(String[] args) {
        SpringApplication.run(SouthChallengeApplication.class, args);
    }


    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    protected Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution,
                                        ChunkContext context) {
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Job job() throws Exception {
        return this.jobs.get("job").start(step1()).build();
    }

    @Bean
    protected Step step1() throws Exception {
        return this.steps.get("step1").tasklet(tasklet()).build();
    }

    public static void main3(String[] args) throws Exception {
        // System.exit is common for Batch applications since the exit code can be used to
        // drive a workflow
        System.exit(SpringApplication.exit(SpringApplication.run(
                SouthChallengeApplication.class, args)));
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(SouthChallengeApplication.class, args);
    }
}
