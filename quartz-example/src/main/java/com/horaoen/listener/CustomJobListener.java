package com.horaoen.listener;

import cn.hutool.core.text.StrFormatter;
import com.horaoen.jobs.SimpleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
public class CustomJobListener implements JobListener {
    @Override
    public String getName() {
        return "customJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info(StrFormatter.format("---{} to be executed.", context.getJobDetail().getKey()));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info(StrFormatter.format("---{} execution was vetoed.", context.getJobDetail().getKey()));
    }

    @Override
    @SneakyThrows
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info(StrFormatter.format("---{} was executed.", context.getJobDetail().getKey()));

        JobDetail subJob = newJob(SimpleJob.class).withIdentity("job2").build();
        Trigger trigger = newTrigger().withIdentity("trigger2").startNow().build();
        context.getScheduler().scheduleJob(subJob, trigger);
    }
}
