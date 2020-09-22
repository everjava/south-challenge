package br.com.ever.southchallenge.model;

import lombok.Data;

@Data
public class SalesItem {
    // [Item ID  -  Item Quantity  - Item Price]
    //[1-34-10,   2-33-1.50,  3-40-0.10]
    //[1-10-100,  2-30-2.50,  3-40-3.10]
    private String id;
    private Integer quantity;
    private Double price;

}
