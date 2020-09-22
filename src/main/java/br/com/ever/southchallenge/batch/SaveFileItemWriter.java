package br.com.ever.southchallenge.batch;

import br.com.ever.southchallenge.model.Customer;
import br.com.ever.southchallenge.model.Sales;
import br.com.ever.southchallenge.model.SalesItem;
import br.com.ever.southchallenge.model.Salesman;
import br.com.ever.southchallenge.properties.SouthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SaveFileItemWriter implements ItemWriter<Object> {

    @Autowired
    private SouthProperties properties;
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveFileItemWriter.class);
    private List<Salesman> salesmanList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<Sales> salesList = new ArrayList<>();

    @Override
    public void write(List<? extends Object> list) {
        for (Object item : list) {
            if (item instanceof Salesman) {
                salesmanList.add((Salesman) item);
            }
            if (item instanceof Customer) {
                customerList.add((Customer) item);
            }
            if (item instanceof Sales) {
                salesList.add((Sales) item);
            }
        }
        this.createFile(this.createFileContent());
    }

    private String createFileContent() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append(this.numberOfCustomers());
        reportContent.append(";");
        reportContent.append(this.numberOfSalesman());
        reportContent.append(";");
        reportContent.append(this.getMostExpansiveSales());
        reportContent.append(";");
        reportContent.append(this.getWorstSalesman());
        return reportContent.toString();
    }

    private int numberOfSalesman() {
        return salesmanList.size();
    }

    private int numberOfCustomers() {
        return customerList.size();
    }

    private String getMostExpansiveSales() {
        Comparator<SalesItem> salesitemPriceComparator = Comparator.comparingDouble(SalesItem::getPrice);
        SalesItem salesItem = salesList
                .stream()
                .map(sales -> sales.getSalesItems().stream().max(salesitemPriceComparator).get())
                .max(salesitemPriceComparator)
                .get();
        return salesItem.getId();
    }

    private String getWorstSalesman() {
        Map<String, Double> salesmanSaleitemsSum = new HashMap<>();
        for (Sales sales : salesList) {
            salesmanSaleitemsSum.put(
                    sales.getName(),
                    sales.getSalesItems().stream().mapToDouble(saleItem -> saleItem.getPrice()).sum());
        }
        Map.Entry<String, Double> salesmanSaleitemsMinimum = Collections.min(
                salesmanSaleitemsSum.entrySet(),
                Map.Entry.comparingByValue());
        return salesmanSaleitemsMinimum.getKey();
    }

    private void createFile(String reportContent) {
        try {
            Path reportFile = this.setUpFileCreation();
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

    private Path setUpFileCreation() throws IOException {
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

