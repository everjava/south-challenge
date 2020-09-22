package br.com.ever.southchallenge.model;

import lombok.Data;

@Data
public class Customer {
    private String code;
    private String cnpj;
    private String name;
    private String businessArea;
}
