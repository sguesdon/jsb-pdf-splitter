package org.sguesdon.jsbpdfsplitter.chunk;

import lombok.extern.slf4j.Slf4j;
import org.sguesdon.jsbpdfsplitter.domain.entity.Page;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class PdfWriter implements ItemStreamWriter<Page> {

    private String absolutePath;

    @Override
    public void write(List<? extends Page> list) {
        list.forEach((i) -> log.info("{}, page {}", i.getDestination()));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.absolutePath = executionContext.getString("absolutePath");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {}

    @Override
    public void close() throws ItemStreamException {
        (new File(this.absolutePath)).delete();
    }
}
