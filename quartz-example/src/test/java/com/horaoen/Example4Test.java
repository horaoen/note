package com.horaoen;

import com.horaoen.jobs.ColorJob;
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
class Example4Test {
    @Test
    @SneakyThrows
    void run() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        // get a "nice round" time a few seconds in the future....
        Date startTime = nextGivenSecondDate(null, 10);

        // job1 will only run 5 times (at start time, plus 4 repeats), every 10 seconds
        JobDetail job1 = newJob(ColorJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(4))
                .build();

        // pass initialization parameters into the job
        job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Green");
        job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

        // schedule the job to run
        Date scheduleTime1 = sched.scheduleJob(job1, trigger1);
        log.info(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount()
                + " times, every " + trigger1.getRepeatInterval() / 1000 + " seconds");

        log.info("jobDetail: " + job1);
        // job2 will also run 5 times, every 10 seconds
        JobDetail job2 = newJob(ColorJob.class).withIdentity("job2", "group1").build();

        SimpleTrigger trigger2 = newTrigger().withIdentity("trigger2", "group1")
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(4))
                .build();

        // pass initialization parameters into the job
        // this job has a different favorite color!
        job2.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Red");
        job2.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

        // schedule the job to run
        Date scheduleTime2 = sched.scheduleJob(job2, trigger2);
        log.info(job2.getKey().toString() + " will run at: " + scheduleTime2 + " and repeat: " + trigger2.getRepeatCount()
                + " times, every " + trigger2.getRepeatInterval() / 1000 + " seconds");


        // All the jobs have been added to the scheduler, but none of the jobs
        // will run until the scheduler has been started
        sched.start();
        Thread.sleep(60L * 1000L);
        sched.shutdown(true);
        SchedulerMetaData metaData = sched.getMetaData();
        log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
}
