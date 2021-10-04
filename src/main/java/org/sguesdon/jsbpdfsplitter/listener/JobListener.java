package org.sguesdon.jsbpdfsplitter.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.sguesdon.jsbpdfsplitter.config.Config;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {

    private final Config config;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            Files.createDirectories(Paths.get(this.config.getInput()));
            Files.createDirectories(Paths.get(this.config.getOutput()));
        } catch(IOException e) {
            jobExecution.addFailureException(e);
            jobExecution.setExitStatus(ExitStatus.FAILED);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
