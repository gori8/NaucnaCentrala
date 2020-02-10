package rs.ac.uns.naucnacentrala.config;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.filter.CharacterEncodingFilter;
import rs.ac.uns.naucnacentrala.camunda.types.JsonFormType;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionFormType;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionStringFormType;


import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;

@Configuration
@Import( SpringProcessEngineServicesConfiguration.class )
public class CamundaConfig {


    @Autowired
    private DataSource dataSource;


    @Value("${camunda.bpm.history-level:full}")
    private String historyLevel;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate("true");

        config.setTransactionManager(transactionManager());

        config.setHistory(historyLevel);
        config.setJobExecutorActivate(true);
        if(config.getCustomFormTypes()==null){
            config.setCustomFormTypes(new ArrayList<AbstractFormFieldType>());
        }
        config.getProcessEnginePlugins().add(new SpinProcessEnginePlugin());
        config.setDefaultSerializationFormat("application/json");
        config.getCustomFormTypes().add(new MultiSelectionFormType("first-multi-select"));
        config.getCustomFormTypes().add(new MultiSelectionFormType("second-multi-select"));
        config.getCustomFormTypes().add(new MultiSelectionFormType("recezenti-multi-select"));
        config.getCustomFormTypes().add(new MultiSelectionFormType("urednici-multi-select"));
        config.getCustomFormTypes().add(new MultiSelectionStringFormType("sel-recezenti-multi-select"));
        config.getCustomFormTypes().add(new JsonFormType("add-children"));




        config.setDeploymentResources(resourceLoader.getResources("classpath:/processes/*.bpmn"));

        return config;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() throws IOException {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}
