package ru.javawebinar.topjava.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.LogManager;

@Configuration
@EnableJpaRepositories(basePackages = "ru.javawebinar.topjava.repository.datajpa")
@ComponentScan(basePackages = {"ru.javawebinar.topjava.repository.jpa", "ru.javawebinar.topjava.repository.datajpa"})
@EnableTransactionManagement
@Profile("datajpa")
@PropertySource("classpath:db/postgres.properties")
public class JpaConfig {

    @Value("${database.url}")
    private String dataBaseURL;


    @Value("${database.username}")
    private String dataBaseUsername;

    @Value("${database.password}")
    private String dataBasePassword;

    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${hibernate.use_sql_comments}")
    private String hibernateUseSqlComments;



    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(dataBaseURL);
        config.setUsername(dataBaseUsername);
        config.setPassword(dataBasePassword);
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }

    @PostConstruct
    public void init() {
        System.out.println("JpaConfig загружен!");
    }

    @PostConstruct
    public static void  configureLogging() {
        LogManager.getLogManager().reset();
        System.setProperty("java.util.logging.manager", "org.apache.logging.slf4j.Log4jBridgeHandler");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.javawebinar.topjava.model");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put("hibernate.format_sql", hibernateFormatSql);
        properties.put("hibernate.use_sql_comments", hibernateUseSqlComments);
        properties.put("hibernate.jpa_proxy_compliance", "false");
        properties.put("hibernate.type.descriptor.sql.BasicBinder", "TRACE");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
