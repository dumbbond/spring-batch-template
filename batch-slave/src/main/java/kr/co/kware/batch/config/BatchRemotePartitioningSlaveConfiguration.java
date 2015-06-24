package kr.co.kware.batch.config;

import kr.co.kware.batch.step.TestSlaveTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.integration.partition.BeanFactoryStepLocator;
import org.springframework.batch.integration.partition.StepExecutionRequestHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Profile(value = { "remotePartitioning" })
public class BatchRemotePartitioningSlaveConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BeanFactory beanFactory;


    @Bean(name = "testStep")
    public Step testSlaveStep() {
        return stepBuilderFactory.get("testStep")
                .tasklet(new TestSlaveTasklet())
                .build();
    }

    @Bean
    public StepExecutionRequestHandler remoteStepExecutionRequestHandler() {
        StepExecutionRequestHandler stepExecutionRequestHandler = new StepExecutionRequestHandler();

        BeanFactoryStepLocator beanFactoryStepLocator = new BeanFactoryStepLocator();
        beanFactoryStepLocator.setBeanFactory(this.beanFactory);

        stepExecutionRequestHandler.setStepLocator(beanFactoryStepLocator);

        JobExplorer jobExplorer = null;
        try {
            JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
            jobExplorerFactoryBean.setDataSource(this.dataSource);
            jobExplorerFactoryBean.afterPropertiesSet();

            jobExplorer = jobExplorerFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stepExecutionRequestHandler.setJobExplorer(jobExplorer);

        return stepExecutionRequestHandler;
    }
}
