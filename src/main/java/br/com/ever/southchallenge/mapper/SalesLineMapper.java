package br.com.ever.southchallenge.mapper;

import br.com.ever.southchallenge.model.Sales;
import br.com.ever.southchallenge.model.SalesItem;
import org.springframework.batch.item.file.LineMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalesLineMapper implements LineMapper<Sales> {

    public Sales mapLine(String line, int lineNumber) {
        String[] lineSplit = line.split("ç");
        Sales sales = new Sales();
        sales.setCode(lineSplit[0]);
        sales.setSaleId(lineSplit[1]);
        sales.setSalesItems(getSalesItem(lineSplit[2]));
        sales.setName(lineSplit[3]);
        return sales;
    }

    private List<SalesItem> getSalesItem(String saleItemArray) {
        List<SalesItem> salesItemList = new ArrayList<>();
        saleItemArray = saleItemArray.replaceAll("\\[|\\]", "");

        Arrays.asList(saleItemArray.split(",")).stream().forEach(line -> {
            String[] item = line.split("-");
            SalesItem salesItem = new SalesItem();
            salesItem.setId(item[0]);
            salesItem.setQuantity(Integer.parseInt(item[1]));
            salesItem.setPrice(Double.parseDouble(item[2]));
            salesItemList.add(salesItem);
        });
        return salesItemList;
    }
}
