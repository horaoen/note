package com.horaoen.jobs;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.sql.Date;
import java.time.Instant;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@NoArgsConstructor
public class ColorJob implements Job {

    // parameter names specific to this job
    public static final String FAVORITE_COLOR = "favorite color";
    public static final String EXECUTION_COUNT = "count";

    // Since Quartz will re-instantiate a class every time it
    // gets executed, members non-static member variables can
    // not be used to maintain state!
    private int _counter = 1;

    @SneakyThrows
    public void execute(JobExecutionContext context) {
        // This job simply prints out its job name and the
        // date and time that it is running
        JobKey jobKey = context.getJobDetail().getKey();

        // Grab and print passed parameters
        JobDataMap data = context.getJobDetail().getJobDataMap();
        log.info("jobDetail: " +  context.getJobDetail());
        String favoriteColor = data.getString(FAVORITE_COLOR);
        int count = data.getInt(EXECUTION_COUNT);
        log.info("ColorJob: " + jobKey + " executing at " + Date.from(Instant.now()) + "\n" +
                "  favorite color is " + favoriteColor + "\n" +
                "  execution count (from job map) is " + count + "\n" +
                "  execution count (from job member variable) is " + _counter);

        // increment the count and store it back into the 
        // job map so that job state can be properly maintained
        count++;
        data.put(EXECUTION_COUNT, count);

        // Increment the local member variable 
        // This serves no real purpose since job state can not 
        // be maintained via member variables!
        _counter++;
    }
}
