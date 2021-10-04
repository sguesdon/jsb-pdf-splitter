package org.sguesdon.jsbpdfsplitter.decider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sguesdon.jsbpdfsplitter.config.Config;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class PdfLoopDecider implements JobExecutionDecider {

    private final Config config;

    public static final String COMPLETED = "COMPLETED";
    public static final String CONTINUE = "CONTINUE";

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if(Objects.requireNonNull(new File(this.config.getInput()).listFiles()).length > 0) {
            return new FlowExecutionStatus(CONTINUE);
        }
        return new FlowExecutionStatus(COMPLETED);
    }
}
