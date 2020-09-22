package br.com.ever.southchallenge.batch;

import br.com.ever.southchallenge.mapper.ClassifierCompositeLineMapper;
import br.com.ever.southchallenge.mapper.CustomerLineMapper;
import br.com.ever.southchallenge.mapper.SalesLineMapper;
import br.com.ever.southchallenge.mapper.SalesmanLineMapper;
import br.com.ever.southchallenge.properties.SouthProperties;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import java.io.File;
import java.nio.file.Path;

@Configuration
public class ParserJobConfig {

    @Autowired
    private SouthProperties properties;

    @Bean
    public ItemReader<Object> itemReader() {
        FlatFileItemReader<Object> fileItemReader = new FlatFileItemReader<Object>();
        fileItemReader.setResource(new PathResource(this.getInputFileFullPath()));
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
    public Step exampleJobStep(ItemReader<Object> reader,
                               ItemWriter<Object> writer,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("exampleJobStep")
                .<Object, Object>chunk(1000)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean//5
    public Job exampleJob(Step exampleJobStep,
                          JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("exampleJob")
                .incrementer(new RunIdIncrementer())
                .flow(exampleJobStep)
                .end()
                .build();
    }

    private String getInputFileFullPath() {
        Path homePath = Path.of(properties.getHomePath());
        Path inputFolderPath = homePath.resolve(properties.getInputFolderName()).toAbsolutePath();
        String inputFileFullPath = inputFolderPath.toString() + File.separator + properties.getInputFileName();
        return inputFileFullPath;
    }
}
