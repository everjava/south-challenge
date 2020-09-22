package br.com.ever.southchallenge.model;

import lombok.Data;

@Data
public class Customer {

    //002,CNPJ,Name,Business Area
    private String code;
    private String cnpj;
    private String name;
    private String businessArea;
}
