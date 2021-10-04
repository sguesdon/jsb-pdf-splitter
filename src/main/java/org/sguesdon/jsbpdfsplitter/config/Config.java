package org.sguesdon.jsbpdfsplitter.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class Config {

    private final int chunks;
    private final int gridSize;
    private final String pattern;
    private final String input;
    private final String output;

    Config(
        @Value("${jsbpdfsplitter.chunkSize}") int chunkSize,
        @Value("${jsbpdfsplitter.gridSize}") int gridSize,
        @Value("${jsbpdfsplitter.pattern}") String pattern,
        @Value("${jsbpdfsplitter.input}") String input,
        @Value("${jsbpdfsplitter.output}") String output
    ) {
        this.chunks = chunkSize;
        this.gridSize = gridSize;
        this.pattern = pattern;
        this.input = input;
        this.output = output;

        log.info("job configuration");
        log.info("nb chunks : {}", chunkSize);
        log.info("grid size : {}", gridSize);
        log.info("pattern : {}", pattern);
        log.info("input : {}", input);
        log.info("output : {}", output);
    }
}
