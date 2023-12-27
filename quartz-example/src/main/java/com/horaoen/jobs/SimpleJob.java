package com.horaoen.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

@Slf4j
public class SimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        log.info(context.getJobDetail().getKey() + ": Hello World! - " + LocalDateTime.now());
    }
}
