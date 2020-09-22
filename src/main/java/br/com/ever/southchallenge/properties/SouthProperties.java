package br.com.ever.southchallenge.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:south-config.properties")
@Data
public class SouthProperties {

    @Value("${south.config.input.folder.name}")
    private String inputFolderName;

    @Value("${south.config.output.folder.name}")
    private String outputFolderName;

    @Value("${south.config.home.path}")
    private String homePath;

    @Value("${south.config.user.home}")
    private String userHome;

    @Value("${south.config.output.file.name}")
    private String outputFileName;

    @Value("${south.config.input.file.name}")
    private String inputFileName;

}
