package org.sguesdon.jsbpdfsplitter.config;

import lombok.RequiredArgsConstructor;
import org.sguesdon.jsbpdfsplitter.chunk.PagePdfProcessor;
import org.sguesdon.jsbpdfsplitter.decider.PdfLoopDecider;
import org.sguesdon.jsbpdfsplitter.listener.JobListener;
import org.sguesdon.jsbpdfsplitter.partitionner.PdfPartitionner;
import org.sguesdon.jsbpdfsplitter.chunk.PdfReader;
import org.sguesdon.jsbpdfsplitter.chunk.PdfWriter;
import org.sguesdon.jsbpdfsplitter.domain.entity.Page;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class PdfSplitterConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final Config config;

    private final PdfReader pdfReader;
    private final PdfWriter pdfWriter;
    private final PagePdfProcessor pdfProcessor;
    private final PdfPartitionner pdfPartitionner;
    private final JobListener jobListener;
    private final PdfLoopDecider loopDecider;

    @Bean
    public Step splitter() {
        return stepBuilderFactory.get("splitter")
            .partitioner(split().getName(), this.pdfPartitionner)
            .step(split())
            .gridSize(this.config.getGridSize())
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    Step split() {
        return this.stepBuilderFactory.get("split")
            .<Page, Page>chunk(this.config.getChunks())
            .reader(pdfReader)
            .processor(pdfProcessor)
            .writer(pdfWriter)
            .build();
    }

    @Bean
    public Flow flowSplitter() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flowSplitter");
        return flowBuilder
                .start(splitter())
                .next(this.loopDecider)
                .on(PdfLoopDecider.CONTINUE)
                .to(splitter())
                .from(this.loopDecider)
                .on(PdfLoopDecider.COMPLETED)
                .end()
                .build();
    }


    @Bean
    public Job job() {
        return jobBuilderFactory.get("splitter")
                .listener(this.jobListener)
                .start(this.flowSplitter())
                .end()
                .build();
    }
}
