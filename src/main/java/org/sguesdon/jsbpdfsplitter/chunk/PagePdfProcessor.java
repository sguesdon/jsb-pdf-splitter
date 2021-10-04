package org.sguesdon.jsbpdfsplitter.chunk;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.sguesdon.jsbpdfsplitter.config.Config;
import org.sguesdon.jsbpdfsplitter.domain.entity.Page;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class PagePdfProcessor implements ItemProcessor<Page, Page> {

    private final Config config;

    @Override
    public Page process(Page pdf) throws Exception {

        final PDDocument chunk = new PDDocument();
        final File dest = new File(this.config.getOutput(), pdf.getDestination());

        if(dest.exists()) {
            dest.delete();
        }

        chunk.addPage(pdf.getPage());
        chunk.save(dest);

        return pdf;
    }
}
