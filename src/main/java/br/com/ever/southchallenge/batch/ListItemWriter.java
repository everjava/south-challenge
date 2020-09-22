package br.com.ever.southchallenge.batch;

import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class ListItemWriter<T> implements ItemWriter<T> {

    private List<T> writtenItems = new ArrayList<T>();

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            writtenItems.add(item);
        }
    }

    public List<? extends T> getWrittenItems() {
        return this.writtenItems;
    }
}