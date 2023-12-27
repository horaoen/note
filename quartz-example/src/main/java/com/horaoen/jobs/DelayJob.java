package com.horaoen.jobs;

import cn.hutool.core.text.StrFormatter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DelayJob implements Job {
    public static final String DELAY_TIME = "delayTime";
    public static final AtomicInteger COUNT = new AtomicInteger(0);

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        long delayTime = jobDetail.getJobDataMap().getLong(DELAY_TIME);
        log.info(StrFormatter.format("---{} executing. delay time: {}", jobDetail.getKey(), delayTime));
        log.info("{} starts", jobDetail.getKey());
        Thread.sleep(delayTime);
        log.info("{} ends", jobDetail.getKey());
        COUNT.incrementAndGet();
    }
}
