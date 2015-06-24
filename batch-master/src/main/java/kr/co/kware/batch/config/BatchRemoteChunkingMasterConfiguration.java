package kr.co.kware.batch.config;

import kr.co.kware.batch.chunk.Payment;
import kr.co.kware.batch.listener.JobDurationListener;
import kr.co.kware.batch.partition.TestPartitioner;
import kr.co.kware.batch.step.TestMasterTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.integration.chunk.RemoteChunkHandlerFactoryBean;
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@AutoConfigureAfter(value = {BatchAutoConfiguration.class, IntegrationAutoConfiguration.class})
@Profile(value = { "remoteChunking" })
public class BatchRemoteChunkingMasterConfiguration {

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
    public Job addTestChunkJob() {
        return jobBuilderFactory.get("testChunkJob")
                .listener(new JobDurationListener())
                .start(addTestChunkStep())
                .build();

    }

    @Bean
    public Step addTestChunkStep() {
//        return stepBuilderFactory.get("testChunkJob.master")
//                .partitioner(addTestStep())
//                .partitioner("testChunkJob", new TestPartitioner(TestPartitioner.DEFAULT_TEST_VALUE_SIZE))
//                .partitionHandler(remotePartitionHandler())
//                .build();
        return null;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Payment> reader(@Value("#{stepExecutionContext[fileName]}") String fileName){
        FlatFileItemReader<Payment> reader = new FlatFileItemReader<Payment>();

        reader.setResource(new ClassPathResource("data/payment.input"));
        reader.setStrict(true);
        reader.setLineMapper(new DefaultLineMapper<Payment>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] {"firstName", "lastName"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Payment>() {{
                setTargetType(Payment.class);
            }});
        }});
        System.out.println(">>> reader" );
        return reader;
    }

    @Bean
    public Step addTestStep() {
        return stepBuilderFactory.get("testChunkJob")
                .tasklet(new TestMasterTasklet())
                .build();
    }

    @Bean
    public ChunkHandler remoteChunkHandler() throws Exception {
        RemoteChunkHandlerFactoryBean remoteChunkHandlerFactory = new RemoteChunkHandlerFactoryBean<Payment>();
        remoteChunkHandlerFactory.setChunkWriter(addChunkWriter());
        remoteChunkHandlerFactory.setStep(addLoadPayments());

        return remoteChunkHandlerFactory.getObject();
    }

    private TaskletStep addLoadPayments() {
        return null;
    }

    private ItemWriter addChunkWriter() {

        return null;
    }
}
