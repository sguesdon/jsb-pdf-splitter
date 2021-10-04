package org.sguesdon.jsbpdfsplitter.chunk;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.sguesdon.jsbpdfsplitter.config.Config;
import org.sguesdon.jsbpdfsplitter.domain.entity.Page;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Iterator;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class PdfReader implements ItemStreamReader<Page> {

    private final Config config;

    private String name;
    private int nbPage = 0;
    private String extension;
    private PDDocument document;
    private Iterator<PDPage> iterator;

    @Override
    public Page read() {
        if(this.iterator.hasNext()) {
            this.nbPage++;
            return Page.builder()
                .destination(String.format(this.config.getPattern(), this.name, this.nbPage, this.extension))
                .page(this.iterator.next())
                .build();
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        final String filename = executionContext.getString("filename");
        this.name = FilenameUtils.getBaseName(filename);
        this.extension = FilenameUtils.getExtension(filename);
        this.document = Loader.loadPDF(new File(executionContext.getString("absolutePath")));
        this.iterator = this.document.getPages().iterator();
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {}

    @SneakyThrows
    @Override
    public void close() throws ItemStreamException {
        this.document.close();
    }
}
