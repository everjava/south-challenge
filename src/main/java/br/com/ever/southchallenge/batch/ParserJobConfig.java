package br.com.ever.southchallenge.batch;

import br.com.ever.southchallenge.mapper.CustomerLineMapper;
import br.com.ever.southchallenge.mapper.SalesLineMapper;
import br.com.ever.southchallenge.mapper.SalesmanLineMapper;
import br.com.ever.southchallenge.model.Customer;
import br.com.ever.southchallenge.model.Salesman;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ParserJobConfig {

    //private ClassifierCompositeLineMapper compositeLineMapper;
    private  static List<Salesman> salesmanList = new ArrayList<>();

    @Bean
    public ItemReader<Object> itemReader() {//1

        //FlatFileItemReader<Salesman> fileItemReader = new FlatFileItemReader<Salesman>();
        FlatFileItemReader<Object> fileItemReader = new FlatFileItemReader<Object>();
        fileItemReader.setResource(new ClassPathResource("data/arquivo.csv"));
        //LineMapper<Salesman> salesmanLineMapper = createSalesmanLineMapper();
        LineMapper<Object> salesmanLineMapper = setUp();

        fileItemReader.setLineMapper(salesmanLineMapper);

        return fileItemReader;
    }

    @Bean
    public ItemWriter<Object> itemWriter() {//3
        return new LoggingItemWriter();
      //  return new ListItemWriter<>();
    }

    public ClassifierCompositeLineMapper setUp() {//2
        Classifier<String, LineMapper<?>> classifier = new Classifier<String, LineMapper<? extends Object>>() {
            public LineMapper<?> classify(String classifiable) {
                if (classifiable.contains("001")) {
                    return new SalesmanLineMapper();
                }
                if (classifiable.contains("002")) {
                    return new CustomerLineMapper();
                }
                if (classifiable.contains("003")) {
                    return new SalesLineMapper();
                }
                return new PassThroughLineMapper(); // or any other default line mapper
            }
        };
        return new ClassifierCompositeLineMapper(classifier);
        //return compositeLineMapper;
    }


    @Bean//4
    public Step exampleJobStep(ItemReader<Object> reader,
                               ItemWriter<Object> writer,
                               StepBuilderFactory stepBuilderFactory) {
        System.out.println("exampleJobStep");
        return stepBuilderFactory.get("exampleJobStep")
                .<Object, Object>chunk(1000)
                .reader(reader)
                .writer(writer)
                .build();
    }


    @Bean//5
    public Job exampleJob(Step exampleJobStep,
                          JobBuilderFactory jobBuilderFactory) {

        System.out.println("exampleJobStep");
        return jobBuilderFactory.get("exampleJob")
                .incrementer(new RunIdIncrementer())
                .flow(exampleJobStep)
                .end()
                .build();
    }
}
