package com.horaoen.jobs;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.Instant;
import java.util.Date;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@NoArgsConstructor
@Slf4j
@SuppressWarnings("all")
public class StatefulDumbJob implements Job {
    public static final String NUM_EXECUTIONS = "NumExecutions";
    public static final String EXECUTION_DELAY = "ExecutionDelay";

    @SneakyThrows
    public void execute(JobExecutionContext context) {
        log.info("===" + context.getJobDetail().getKey() + " executing.[" + Date.from(Instant.now()) + "]");

        JobDataMap map = context.getJobDetail().getJobDataMap();

        int executeCount = 0;
        if (map.containsKey(NUM_EXECUTIONS)) {
            executeCount = map.getInt(NUM_EXECUTIONS);
        }

        executeCount++;

        map.put(NUM_EXECUTIONS, executeCount);

        long delay = 5000;
        if (map.containsKey(EXECUTION_DELAY)) {
            delay = map.getLong(EXECUTION_DELAY);
        }

        Thread.sleep(delay);
        log.info("===" + context.getJobDetail().getKey() + " complete (" + executeCount + "). executing.[" + Date.from(Instant.now()) + "]");
    }
}
