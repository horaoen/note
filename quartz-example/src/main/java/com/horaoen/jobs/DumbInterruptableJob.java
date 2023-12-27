package com.horaoen.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;
import java.util.Map;

@Slf4j
public class DumbInterruptableJob implements InterruptableJob {
    // has the job been interrupted?
    private boolean interrupted = false;

    public static final String INTERRUPTED_COUNT = "interruptedCount";
    public static final String EXECUTED_COUNT = "executedCount";
    public static final Map<String, Integer> jobCounter = new java.util.concurrent.ConcurrentHashMap<>();

    // job name 
    private JobKey jobKey = null;

    @Override
    public void execute(JobExecutionContext context) {

        jobKey = context.getJobDetail().getKey();
        log.info("---- " + jobKey + " executing at " + new java.util.Date());

        if (jobCounter.containsKey(jobKey + EXECUTED_COUNT)) {
            jobCounter.put(jobKey + EXECUTED_COUNT, jobCounter.get(jobKey + EXECUTED_COUNT) + 1);
        } else jobCounter.put(jobKey + EXECUTED_COUNT, 1);

        try {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }

                // periodically check if we've been interrupted...
                if (interrupted) {
                    if (jobCounter.containsKey(jobKey + INTERRUPTED_COUNT)) {
                        jobCounter.put(jobKey + INTERRUPTED_COUNT, jobCounter.get(jobKey + INTERRUPTED_COUNT) + 1);
                    } else jobCounter.put(jobKey + INTERRUPTED_COUNT, 1);

                    log.info("--- " + jobKey + "  -- Interrupted... bailing out!");
                    return; // could also choose to throw a JobExecutionException 
                    // if that made for sense based on the particular  
                    // job's responsibilities/behaviors
                }
            }

        } finally {
            log.info("---- " + jobKey + " completed at " + new Date());
        }
    }

    public void interrupt() {
        log.info("---" + jobKey + "  -- INTERRUPTING --");
        interrupted = true;
    }
}
