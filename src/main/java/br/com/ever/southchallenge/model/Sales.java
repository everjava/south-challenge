package br.com.ever.southchallenge.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class Sales {

    //003,Sale ID,[Item ID-Item Quantity-Item Price],Salesman

    private String code;
    private String saleId;
    private List<SalesItem> salesItems;
    private String name;
}
