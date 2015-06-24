package kr.co.kware.batch.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by chlee on 15. 4. 15..
 */
public class TestSlaveTasklet implements Tasklet {
    private static final Logger LOG = LoggerFactory.getLogger(TestSlaveTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOG.info("step execution context : " + chunkContext.getStepContext().getStepExecutionContext());

        return RepeatStatus.FINISHED;
    }
}
