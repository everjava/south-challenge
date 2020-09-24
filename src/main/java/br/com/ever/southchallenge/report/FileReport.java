package br.com.ever.southchallenge.report;

import br.com.ever.southchallenge.properties.SouthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileReport {

    private static SouthProperties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileReport.class);

    @Autowired
    public FileReport(SouthProperties properties) {
        FileReport.properties = properties;
    }

    public static void createFile(String reportContent) {
        try {
            Path reportFile = setUpFileCreation();
            try (FileOutputStream outputStream = new FileOutputStream(reportFile.toString())) {
                outputStream.write(reportContent.getBytes());
                LOGGER.info(" File successfully created  at: " + reportFile.toAbsolutePath());
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            LOGGER.error("File creation failed", e);
        }
    }

    private static Path setUpFileCreation() throws IOException {
        Path homePath = Path.of(properties.getHomePath());
        Path outputFolderPath = Files.createDirectories(homePath.resolve(properties.getOutputFolderName()));
        File outputFileFullPath = new File(outputFolderPath.toAbsolutePath().toString());
        if (!outputFileFullPath.exists()) {
            outputFileFullPath.mkdirs();
        }
        Files.deleteIfExists(outputFolderPath.resolve(properties.getOutputFileName()));
        Path reportFile = Files.createFile(outputFolderPath.resolve(properties.getOutputFileName()));
        return reportFile;
    }

}
