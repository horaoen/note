package com.horaoen;

import com.horaoen.jobs.BadJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example6Test {
    @Test
    @SneakyThrows
    void run() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        Date startTime = nextGivenSecondDate(null, 15);

        // badJob1 will run every 10 seconds
        // this job will throw an exception and refire
        // immediately
        JobDetail job = newJob(BadJob.class)
                .withIdentity("badJob1", "group1")
                .usingJobData("denominator", "0")
                .build();

        SimpleTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        Date ft = scheduler.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        scheduler.start();
        Thread.sleep(30L * 1000L);
        scheduler.shutdown(false);
        SchedulerMetaData metaData = scheduler.getMetaData();
        log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
}
