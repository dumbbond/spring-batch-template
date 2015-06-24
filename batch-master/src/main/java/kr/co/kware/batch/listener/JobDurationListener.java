package kr.co.kware.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by chlee on 15. 4. 15..
 */
public class JobDurationListener implements JobExecutionListener {
    private static final Logger LOG = LoggerFactory.getLogger(JobDurationListener.class);


    private StopWatch stopWatch;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        stopWatch = new StopWatch();
        stopWatch.start("Start stopwatch.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        stopWatch.stop();

        long duration = stopWatch.getLastTaskTimeMillis();

        LOG.info("Job took: {} minutes, {} seconds.",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
