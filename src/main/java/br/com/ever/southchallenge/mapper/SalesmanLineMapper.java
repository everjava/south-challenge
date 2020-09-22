package br.com.ever.southchallenge.mapper;

import br.com.ever.southchallenge.model.Salesman;
import org.springframework.batch.item.file.LineMapper;

public class SalesmanLineMapper implements LineMapper<Salesman> {

//7
    public Salesman mapLine(String line, int lineNumber) throws Exception {
        System.out.println(line);
        String[] lineSplit = line.split("ç");
        Salesman salesman = new Salesman();
        salesman.setCode(lineSplit[0]);
        salesman.setName(lineSplit[1]);
        salesman.setCpf(lineSplit[2]);
        salesman.setSalary(lineSplit[3]);


        return salesman;
    }


}

