package org.sguesdon.jsbpdfsplitter.partitionner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sguesdon.jsbpdfsplitter.config.Config;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class PdfPartitionner implements Partitioner {

    private final Config config;

    @Override
    @SneakyThrows
    public Map<String, ExecutionContext> partition(int num) {

        final File pdfDirectory = new File(this.config.getInput());

        if(!pdfDirectory.exists()) {
            throw new FileNotFoundException(String.format("missing directory %s", pdfDirectory.getAbsolutePath()));
        }

        Map<String, ExecutionContext> files = new HashMap<>();

        for(File pdf : Objects.requireNonNull(pdfDirectory.listFiles())) {
            ExecutionContext context = new ExecutionContext();
            context.putString("filename", pdf.getName());
            context.putString("absolutePath", pdf.getAbsolutePath());
            files.put("partition_" + pdf.getName(), context);
            if(files.size() >= num) {
                break;
            }
        }

        return files;
    }
}
