package com.horaoen;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * test cron trigger
 * see {@link org.quartz.CronExpression}
 */
@Slf4j
@SuppressWarnings("all")
class Example3Test {
    @Test
    @SneakyThrows
    void run() {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();

        JobDetail job = newJob().withIdentity("job1", "example3")
                .build();
        // job1 will run every 20 seconds j
        Trigger trigger = newTrigger().withIdentity("trigger1", "example3")
                .withSchedule(cronSchedule("0/20 * * * * ?"))
                .build();

        // job2 will run every other minute(at 15 secondes past the minute)
        trigger = newTrigger().withIdentity("trigger2", "example3")
                .withSchedule(cronSchedule("15 0/2 * * * * ?"))
                .build();

        // job3 will run every other minute but only betwwen 8:00 and 17:00
        trigger = newTrigger().withIdentity("trigger3", "example3")
                .withSchedule(cronSchedule("0 0/2 8-17 * * ?"))
                .build();

        // job4 will run every three minute but only betwwen 17:00 and 23:00
        trigger = newTrigger().withIdentity("trigger4", "example3")
                .withSchedule(cronSchedule("0 0/3 17-23 * * ?"))
                .build();

        // cron: "0 0 10am 1,15 * ?" job will run on the 1st and 15th of every month at 10:00 am
        // cron: "0,30 * * ? * MON-FRI" job will run every 30 second but only on Weekdays
        // cron: "0,30 * * ? * SAT,SUN" job will run every 30 seconds but only on Weekends (Saturday and Sunday)
        scheduler.start();
        Thread.sleep(120L * 1000L);
        scheduler.shutdown();
    }
}
