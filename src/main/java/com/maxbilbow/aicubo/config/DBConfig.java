package com.maxbilbow.aicubo.config;

import com.maxbilbow.aicubo.exception.AiCuboException;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Max on 18/04/2016.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.maxbilbow.aicubo.dao")
@EntityScan("com.maxbilbow.aicubo.model")
public class DBConfig
        //implements TransactionManagementConfigurer
{
  @Resource
  Environment mEnvironment;

  @Bean
  public DataSource dataSource()
  {
    try
    {
      HikariDataSource ds = new HikariDataSource();
      ds.setDriverClassName(mEnvironment.getRequiredProperty("db.driver"));
      ds.setUsername(mEnvironment.getRequiredProperty("db.username"));
      ds.setPassword(mEnvironment.getRequiredProperty("db.password"));
      ds.setJdbcUrl(mEnvironment.getRequiredProperty("db.url"));
//      ds.setMinimumIdle(10);
//      ds.setMaximumPoolSize(15);
      ds.setDataSourceProperties(hibernateProperties());
      return ds;
    }
    catch(Exception aException)
    {
      throw new AiCuboException(aException);
    }

  }

  //Hibernate Config
//  @Bean
  private Properties hibernateProperties()
  {
    Properties hibernateProperties = new Properties();

    hibernateProperties.put("hibernate.dialect", mEnvironment.getRequiredProperty("hibernate.dialect"));
    hibernateProperties.put("hibernate.show_sql", mEnvironment.getRequiredProperty("hibernate.show_sql"));

    hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
//    hibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop"); //update
//    hibernateProperties.put("hibernate.order_updates", "true");
//    hibernateProperties.put("hibernate.order_inserts", "true");
//    hibernateProperties.put("hibernate.jdbc.batch_size", "50");
//    hibernateProperties.put("hibernate.max_fetch_depth", "3");

//    hibernateProperties.put("hibernate.cache.use_second_level_cache", "false");

//    hibernateProperties.put("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.DefaultAuditStrategy");
//    hibernateProperties.put("org.hibernate.envers.do_not_audit_optimistic_locking_field", "true");
//    hibernateProperties.put("org.hibernate.envers.revision_field_name","REV");
//    hibernateProperties.put("org.hibernate.envers.revision_type_field_name","REVTYPE");
//
//    hibernateProperties.put("hibernate.globally_quoted_identifiers", "true");
    hibernateProperties.put("autoReconnect", "true");
    hibernateProperties.put("createDatabaseIfNotExist", "true");

    return hibernateProperties;
  }

//  @Bean
  public SessionFactory sessionFactory()
  {
    try
    {
      LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

      sessionFactoryBean.setDataSource(dataSource());

//      sessionFactoryBean.setPackagesToScan("com.maxbilbow.aicubo.model");

      sessionFactoryBean.setHibernateProperties(hibernateProperties());
      sessionFactoryBean.afterPropertiesSet();

      return sessionFactoryBean.getObject();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

//  @Bean
//  public HibernateTransactionManager transactionManager()
//  {
//    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//    transactionManager.setSessionFactory(sessionFactory());
//    return transactionManager;
//  }

//  @Override
//  public PlatformTransactionManager annotationDrivenTransactionManager()
//  {
//    return transactionManager();
//  }

//  @Bean
//  public TransactionTemplate transactionTemplate()
//  {
//    TransactionTemplate transactionTemplate = new TransactionTemplate();
//    transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//    transactionTemplate.setTransactionManager(transactionManager());
//    return transactionTemplate;
//  }
}