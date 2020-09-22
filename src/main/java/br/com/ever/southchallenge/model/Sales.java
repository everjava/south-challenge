package br.com.ever.southchallenge.model;

import lombok.Data;
import java.util.List;

@Data
public class Sales {
    private String code;
    private String saleId;
    private List<SalesItem> salesItems;
    private String name;
}
