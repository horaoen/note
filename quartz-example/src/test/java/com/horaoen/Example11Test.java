package com.horaoen;

import com.horaoen.jobs.DelayJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example11Test {
    public static final long JOB_NUMBER = 50;

    @Test
    @SneakyThrows
    void run() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        for (int count = 0; count < JOB_NUMBER; count++) {
            JobDetail job = newJob(DelayJob.class).withIdentity("job" + count)
                    // 数据持久化才能看出差别
                    .requestRecovery()
                    .build();
            // tell the job to delay some small amount... to simulate work...
            long timeDelay = (long) (java.lang.Math.random() * 2000L);

            job.getJobDataMap().put(DelayJob.DELAY_TIME, timeDelay);

            Trigger trigger = newTrigger().withIdentity("trigger_" + count)
                    // 预算启动时间
                    .startAt(futureDate((1000 + (count * 100)), DateBuilder.IntervalUnit.MILLISECOND)) // space fire times a small bit
                    .build();
            scheduler.scheduleJob(job, trigger);
        }
        scheduler.start();
        Thread.sleep(10L * 1000L);
        scheduler.shutdown(true);
        scheduler.shutdown();
        scheduler.pauseAll();
        int numberOfJobsExecuted = scheduler.getMetaData().getNumberOfJobsExecuted();
        log.info("executed " + DelayJob.COUNT.get() + " jobs");
    }
}
