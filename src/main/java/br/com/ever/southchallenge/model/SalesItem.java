package br.com.ever.southchallenge.model;

import lombok.Data;

@Data
public class SalesItem {
    private String id;
    private Integer quantity;
    private Double price;
}
