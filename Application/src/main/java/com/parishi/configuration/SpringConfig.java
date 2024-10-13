package com.parishi.configuration;


import com.parishi.entity.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.parishi")
@PropertySource("classpath:application.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.parishi.dao")
public class SpringConfig implements WebMvcConfigurer {

    private final Environment env;

    @Autowired
    public SpringConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/views/", ".jsp");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("dashboard");
//        registry.addViewController("/").setViewName("trainer-dashboard");

    }
    @Bean
    public MessageSource messageSource()
    {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
    @Bean
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.parishi.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        sessionFactory.setAnnotatedClasses(AssignmentPlan.class);
        sessionFactory.setAnnotatedClasses(Attendance.class);
        sessionFactory.setAnnotatedClasses(Batch.class);
        sessionFactory.setAnnotatedClasses(CoursePlan.class);
        sessionFactory.setAnnotatedClasses(SubTopic.class);
        sessionFactory.setAnnotatedClasses(Topic.class);
        sessionFactory.setAnnotatedClasses(Trainee.class);
        sessionFactory.setAnnotatedClasses(Trainer.class);
        sessionFactory.setAnnotatedClasses(DayWisePlan.class);
        sessionFactory.setAnnotatedClasses(MentoringSession.class);
        sessionFactory.setAnnotatedClasses(Holiday.class);
        sessionFactory.setAnnotatedClasses(Assessment.class);
        sessionFactory.setAnnotatedClasses(User.class);

        return sessionFactory;
    }
    //    @Bean
//    public HibernateTransactionManager transactionManager()
//    {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory().getObject());
//        return transactionManager;
//    }
    private Properties hibernateProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.setProperty("hibernate.format_sql", env.getProperty("spring.jpa.format-sql"));
        properties.setProperty("hibernate.hbm2ddl.auto",env.getProperty("spring.jpa.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(5242880); // 5MB
        return resolver;
    }

    @Bean
    public static Logger getLogger(){
        return LogManager.getLogger("LoggerFactory");
    }

    //    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource());
//        emf.setPackagesToScan("com.parishi");
//        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return emf;
//    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.parishi.entity");
        em.setJpaProperties(jpaProperties());
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return em;
    }
    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getRequiredProperty("spring.jpa.show-sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("spring.jpa.hbm2ddl.auto"));
        return properties;
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}