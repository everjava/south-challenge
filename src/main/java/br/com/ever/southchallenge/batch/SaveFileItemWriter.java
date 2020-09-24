package br.com.ever.southchallenge.batch;

import br.com.ever.southchallenge.model.Customer;
import br.com.ever.southchallenge.model.Sales;
import br.com.ever.southchallenge.model.Salesman;
import br.com.ever.southchallenge.report.FileReport;
import br.com.ever.southchallenge.report.ReportSales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class SaveFileItemWriter implements ItemWriter<Object> {

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
        FileReport.createFile(ReportSales.createFileContent(salesmanList, customerList, salesList));
    }

}

