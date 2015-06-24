package kr.co.kware.batch.config;


import org.springframework.batch.admin.web.resources.DefaultResourceService;
import org.springframework.batch.admin.web.resources.ResourceService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource({
        "classpath*:/META-INF/spring/batch/bootstrap/integration/*.xml",
        "classpath*:/META-INF/spring/batch/bootstrap/manager/jmx-context.xml",
        "classpath*:/META-INF/spring/batch/bootstrap/manager/execution-context.xml",
        "classpath*:/META-INF/spring/batch/bootstrap/resources/resources-context.xml",
        "classpath*:/META-INF/spring/batch/servlet/**/*.xml"
})
@AutoConfigureAfter(value = {BatchAutoConfiguration.class, IntegrationAutoConfiguration.class})
public class BatchAdminConfiguration {
}
