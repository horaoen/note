package com.horaoen;

import com.horaoen.jobs.SimpleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@SuppressWarnings("all")
class Example8Test {
    @Test
    @SneakyThrows
    void run() {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        AnnualCalendar holidays = new AnnualCalendar();

        // fourth of July (July 4)
        Calendar fourthOfJuly = new GregorianCalendar(2005, Calendar.JULY, 4);
        holidays.setDayExcluded(fourthOfJuly, true);
        // halloween (Oct 31)
        Calendar halloween = new GregorianCalendar(2005, Calendar.OCTOBER, 31);
        holidays.setDayExcluded(halloween, true);
        // christmas (Dec 25)
        Calendar christmas = new GregorianCalendar(2005, Calendar.DECEMBER, 25);
        holidays.setDayExcluded(christmas, true);

        // tell the schedule about our holiday calendar
        scheduler.addCalendar("holidays", holidays, false, false);

        // schedule a job to run hourly, starting on halloween
        // at 10 am
        Date runDate = dateOf(0, 0, 10, 31, 10);

        JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();

        SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1")
                .startAt(runDate)
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever())
                .modifiedByCalendar("holidays")
                .build();

        // schedule the job and print the first run date
        Date firstRunTime = scheduler.scheduleJob(job, trigger);

        // print out the first execution date.
        // Note: Since Halloween (Oct 31) is a holiday, then
        // we will not run until the next day! (Nov 1)
        log.info(job.getKey() + " will run at: " + firstRunTime + " and repeat: " + trigger.getRepeatCount()
                + " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

        scheduler.start();
        try {
            // wait 30 seconds to show jobs
            Thread.sleep(30L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }

        scheduler.shutdown(true);

        SchedulerMetaData metaData = scheduler.getMetaData();
        log.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
    }
}
