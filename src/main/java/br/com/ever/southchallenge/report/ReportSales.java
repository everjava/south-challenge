package br.com.ever.southchallenge.report;

import br.com.ever.southchallenge.model.Customer;
import br.com.ever.southchallenge.model.Sales;
import br.com.ever.southchallenge.model.SalesItem;
import br.com.ever.southchallenge.model.Salesman;

import java.util.*;

public class ReportSales {

    public static String createFileContent(
            List<Salesman> salesmanList, List<Customer> customerList, List<Sales> salesList) {

        StringBuilder reportContent = new StringBuilder();
        reportContent.append(numberOfCustomers(customerList));
        reportContent.append(";");
        reportContent.append(numberOfSalesman(salesmanList));
        reportContent.append(";");
        reportContent.append(getMostExpansiveSales(salesList));
        reportContent.append(";");
        reportContent.append(getWorstSalesman(salesList));
        return reportContent.toString();
    }

    private static int numberOfSalesman(List<Salesman> salesmanList) {
        return salesmanList.size();
    }

    private static int numberOfCustomers(List<Customer> customerList) {
        return customerList.size();
    }

    private static String getMostExpansiveSales(List<Sales> salesList) {
        Comparator<SalesItem> salesitemPriceComparator = Comparator.comparingDouble(SalesItem::getPrice);
        SalesItem salesItem = salesList
                .stream()
                .map(sales -> sales.getSalesItems().stream().max(salesitemPriceComparator).get())
                .max(salesitemPriceComparator)
                .get();

        return salesItem.getId();
    }

    private static String getWorstSalesman(List<Sales> salesList) {
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

}
