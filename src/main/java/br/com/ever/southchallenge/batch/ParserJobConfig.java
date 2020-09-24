package br.com.ever.southchallenge.batch;

import br.com.ever.southchallenge.mapper.ClassifierCompositeLineMapper;
import br.com.ever.southchallenge.mapper.CustomerLineMapper;
import br.com.ever.southchallenge.mapper.SalesLineMapper;
import br.com.ever.southchallenge.mapper.SalesmanLineMapper;
import br.com.ever.southchallenge.properties.SouthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Path;

@Configuration
public class ParserJobConfig {

    @Autowired
    private SouthProperties properties;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserJobConfig.class);

    @Bean
    public MultiResourceItemReader<Object> multiResourceItemReader() {
        MultiResourceItemReader<Object> resourceItemReader = new MultiResourceItemReader<Object>();
        try {
            String path = Path.of(
                    properties.getInputFolderName()).toUri().toString() +
                    properties.getInputFolderMultipleFiles();
            PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(
                    this.getClass().getClassLoader());
            resourceItemReader.setResources(resourceResolver.getResources(path));
            resourceItemReader.setDelegate(itemReader());
        } catch (IOException e) {
            LOGGER.error("Error on classpath input files", e);
        }

        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<Object> itemReader() {
        FlatFileItemReader<Object> fileItemReader = new FlatFileItemReader<Object>();
        LineMapper<Object> salesmanLineMapper = classifierCompositeLineMapper();
        fileItemReader.setLineMapper(salesmanLineMapper);
        return fileItemReader;
    }


    @Bean
    public ItemWriter<Object> itemWriter() {
        return new SaveFileItemWriter();
    }


    public ClassifierCompositeLineMapper classifierCompositeLineMapper() {
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
                return new PassThroughLineMapper(); //default line mapper
            }
        };
        return new ClassifierCompositeLineMapper(classifier);
    }

    @Bean//4
    public Step processFileJobStep() {
        return stepBuilderFactory.get("processFileJobStep")
                .<Object, Object>chunk(1000)
                .reader(multiResourceItemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean//5
    public Job processFileJob(Step processFileJobStep,
                              JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("processFileJob")
                .incrementer(new RunIdIncrementer())
                .flow(processFileJobStep)
                .end()
                .build();
    }

}
