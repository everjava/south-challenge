package br.com.ever.southchallenge.mapper;

import br.com.ever.southchallenge.model.Customer;
import org.springframework.batch.item.file.LineMapper;

public class CustomerLineMapper implements LineMapper<Customer> {

    public Customer mapLine(String line, int lineNumber) throws Exception {
        System.out.println(line);
        String[] lineSplit = line.split("ç");
        Customer customer = new Customer();
        customer.setCode(lineSplit[0]);
        customer.setCnpj(lineSplit[1]);
        customer.setName(lineSplit[2]);
        customer.setBusinessArea(lineSplit[3]);

        return customer; // 002ç2345675434544345çJose da SilvaçRural
    }
}