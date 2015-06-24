package kr.co.kware.batch.config;

import kr.co.kware.batch.listener.JobDurationListener;
import kr.co.kware.batch.partition.TestPartitioner;
import kr.co.kware.batch.step.TestMasterTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@AutoConfigureAfter(value = { BatchAutoConfiguration.class, IntegrationAutoConfiguration.class })
@EnableBatchProcessing
@Profile(value = { "remotePartitioning" })
public class BatchRemotePartitionMasterConfiguration {

    @Value("${spring.batch.grid.size}")
    private int gridSize;

    /**
     * JobBuilderFactory 는 Spring Batch에서 생성해주는 객체임.
     * spring bean name이 jobBuilderFactory임.
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * StepBuilderFactory 는 Spring Batch에서 생성해주는 객체임.
     * spring bean name이 stepBuilderFactory임.
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    /**
     * integration-master.xml 에 정의되어 있음.
     */
    @Autowired
    private PollableChannel aggregatedReplyChannel;

    /**
     * integration-master.xml 에 정의되어 있음.
     */
    @Autowired
    private MessageChannel requestsChannel;

    @Bean
    public Job addTestJob() {
        return jobBuilderFactory.get("testJob")
                .listener(new JobDurationListener())
                .start(addTestPartitionStep())
                .build();

    }

    @Bean
    public Step addTestPartitionStep() {
        return stepBuilderFactory.get("testStep.master")
                .partitioner(addTestStep())
                .partitioner("testStep", new TestPartitioner(TestPartitioner.DEFAULT_TEST_VALUE_SIZE))
                .partitionHandler(remotePartitionHandler())
                .build();
    }

    @Bean
    public Step addTestStep() {
        return stepBuilderFactory.get("testStep")
                .tasklet(new TestMasterTasklet())
                .build();
    }

    @Bean
    public PartitionHandler remotePartitionHandler() {
        MessageChannelPartitionHandler partitionHandler = new MessageChannelPartitionHandler();
        partitionHandler.setStepName("testStep");
        partitionHandler.setGridSize(this.gridSize);
        partitionHandler.setReplyChannel(this.aggregatedReplyChannel);

        MessagingTemplate messagingTemplate = new MessagingTemplate(this.requestsChannel);

        partitionHandler.setMessagingOperations(messagingTemplate);

        return partitionHandler;
    }
}
