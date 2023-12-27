package com.horaoen;

import com.horaoen.jobs.SimpleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Instant;
import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example1Test {
    @Test
    @SneakyThrows
    void run() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Date runTime = evenMinuteDate(Date.from(Instant.now()));
        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job1", "example1")
                .build();
        Trigger trigger = newTrigger().withIdentity("trigger1", "example1")
                .startAt(runTime)
                .build();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.scheduleJob(job, trigger);

        log.info(job.getKey() + " has been scheduled to run at: " + runTime);

        scheduler.start();
        Thread.sleep(65L * 1000L);
        scheduler.shutdown();
        log.info("Shutting down example 1");
    }
}
