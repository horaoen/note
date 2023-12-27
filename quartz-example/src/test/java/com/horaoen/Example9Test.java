package com.horaoen;

import com.horaoen.jobs.SimpleJob;
import com.horaoen.listener.CustomJobListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
class Example9Test {
    @Test
    @SneakyThrows
    void run() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        // schedule a job to run immediately

        JobDetail job = newJob(SimpleJob.class).withIdentity("job1").build();

        Trigger trigger = newTrigger().withIdentity("trigger1").startNow().build();

        // Set up the listener
        JobListener listener = new CustomJobListener();
        Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getKey());
        scheduler.getListenerManager().addJobListener(listener, matcher);

        // schedule the job to run
        scheduler.scheduleJob(job, trigger);

        scheduler.start();

        try {
            Thread.sleep(30L * 1000L);
        } catch (Exception e) {
            //
        }
        scheduler.shutdown(true);
        SchedulerMetaData metaData = scheduler.getMetaData();
        log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
}
