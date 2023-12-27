package com.horaoen;

import cn.hutool.json.JSONUtil;
import com.horaoen.jobs.SimpleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example2Test {
    @Test
    @SneakyThrows
    void run() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "example2")
                .build();
        Date date = DateBuilder.nextGivenSecondDate(null, 10);
        SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "example2")
                .startAt(date)
                .build();

        log.info("simpleTrigger: " + JSONUtil.parse(trigger).toString());
        log.info(job.getKey() + " has been scheduled to run at: " + date);
        // job1
        scheduler.scheduleJob(job, trigger);

        job = newJob(SimpleJob.class).withIdentity("job2", "example2").build();
        trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2", "example2")
                .startAt(date)
                .build();

        // job2
        scheduler.scheduleJob(job, trigger);

        job = newJob(SimpleJob.class).withIdentity("job3", "example2").build();
        trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger3", "example2")
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(4))
                .build();

        // job3
        scheduler.scheduleJob(job, trigger);

        job = newJob(SimpleJob.class).withIdentity("job4", "example2").build();
        trigger = (SimpleTrigger) newTrigger().withIdentity("trigger4", "example2")
                .startAt(futureDate(10, DateBuilder.IntervalUnit.SECOND))
                .build();

        scheduler.scheduleJob(job, trigger);

        scheduler.start();
        Thread.sleep(60 * 1000);
        scheduler.shutdown();
        log.info("Scheduler shut down");
    }
}
