package com.horaoen;

import com.horaoen.jobs.DumbInterruptableJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example7Test {

    @Test
    @SneakyThrows
    void run() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        Date startTime = nextGivenSecondDate(null, 3);

        JobDetail job = newJob(DumbInterruptableJob.class).withIdentity("interruptableJob1", "group1").build();

        SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .build();
        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
                + trigger.getRepeatInterval() / 1000 + " seconds");

        // start up the scheduler (jobs do not start to fire until
        // the scheduler has been started)
        sched.start();
        for (int i = 0; i < 6; i++) {
            try {
                Thread.sleep(3000L);
                // tell the scheduler to interrupt our job
                sched.interrupt(job.getKey());
            } catch (Exception e) {
                //
            }
        }

        sched.shutdown(true);
        SchedulerMetaData metaData = sched.getMetaData();
        assertEquals(DumbInterruptableJob.jobCounter.get(job.getKey() + DumbInterruptableJob.EXECUTED_COUNT), metaData.getNumberOfJobsExecuted());
        log.info("--- interruptedCount: " + DumbInterruptableJob.jobCounter.get(job.getKey() + DumbInterruptableJob.INTERRUPTED_COUNT));
        log.info("--- executedCount: " + DumbInterruptableJob.jobCounter.get(job.getKey() + DumbInterruptableJob.EXECUTED_COUNT));
    }
}
