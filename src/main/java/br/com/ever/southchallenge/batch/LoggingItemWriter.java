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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

//@PropertySource("classpath:south-config.properties")
public class LoggingItemWriter implements ItemWriter<Object> {

    //@Value("${south.config.input.folder.name}")
    private String inputFolderName;

    //@Value("${south.config.output.folder.name}")
    private String outputFolderName;

    @Autowired
    private SouthProperties properties;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingItemWriter.class);
    private List<Salesman> salesmanList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<Sales> salesList = new ArrayList<>();


    @Override//8
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

        StringBuilder reportContent = new StringBuilder();
        reportContent.append(this.numberOfCustomers());
        reportContent.append(";");
        reportContent.append(this.numberOfSalesman());
        reportContent.append(";");
        reportContent.append(this.getMostExpansiveSales());
        reportContent.append(";");
        reportContent.append(this.getWorstSalesman());
        this.createFile(reportContent.toString());

        System.out.println(properties.getInputFolderName());
        System.out.println(properties.getOutputFolderName());
        System.out.println(properties.getHomePath());
        String home = System.getProperty("user.home");
        System.out.println("home = " + home);

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
            salesmanSaleitemsSum.put(sales.getName(), sales.getSalesItems().stream().mapToDouble(saleItem -> saleItem.getPrice()).sum());
        }

        Map.Entry<String, Double> salesmanSaleitemsMinimum = Collections.min(salesmanSaleitemsSum.entrySet(), Map.Entry.comparingByValue());
        return salesmanSaleitemsMinimum.getKey();
    }


    private void createFile(String reportContent) {
        try {
            Path path = Path.of(properties.getHomePath());
            Path newDirectory = Files.createDirectories(path.resolve(properties.getOutputFolderName()));
            File file = new File(newDirectory.toAbsolutePath().toString());
            System.out.println("input == "+path.resolve(properties.getInputFolderName()).toAbsolutePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            Files.deleteIfExists(newDirectory.resolve(properties.getOutputFileName()));
            Path file2 = Files.createFile(newDirectory.resolve(properties.getOutputFileName()));

            System.out.println(">> Output Path1: " + file2.toAbsolutePath());
            System.out.println(">> Output Path2: " + file2);

            try (FileOutputStream outputStream = new FileOutputStream(file2.toString())) {
                outputStream.write(reportContent.getBytes());
                LOGGER.info(" File successfully created  at: " + file2.toAbsolutePath());
            } catch (IOException ex) {
                throw ex;
            }
        } catch (IOException e) {
            LOGGER.error("*************  File creation failed ************* ", e);
        }
    }
}

