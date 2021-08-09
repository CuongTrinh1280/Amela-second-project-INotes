package com.assignment.edu.configuration.application;

import com.assignment.edu.fomatter.CategoryFormatter;
import com.assignment.edu.fomatter.LocalDateFormatter;
import com.assignment.edu.service.category.CategoryService;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import org.springframework.format.FormatterRegistry;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import org.thymeleaf.templatemode.TemplateMode;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.assignment.edu.repository")
@PropertySource("classpath:application.properties")
@ComponentScan("com.assignment.edu")
public class AppConfiguration implements ApplicationContextAware, WebMvcConfigurer {

    @Autowired
    Environment env;

    Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

    @Value("${file-upload}")
    private String fileUpload;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        var property = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource("application.properties") };
        property.setLocations(resources);
        property.setIgnoreUnresolvablePlaceholders(true);
        return property;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        var templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("WEB-INF/view");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        var templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        var viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setForceContentType(true);
        viewResolver.setContentType("text/html; charset=UTF-8");
        return viewResolver;
    }

    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.assignment.edu");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/inotes_application?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("cuongthan1280");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var setProperty = new SessionLocaleResolver();
        setProperty.setDefaultLocale(new Locale("en"));
        return setProperty;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver()  {
        var resolver= new CookieLocaleResolver();
        resolver.setCookieDomain("myAppLocaleCookie");
        resolver.setDefaultLocale(Locale.ENGLISH);
        // 60 minutes
        resolver.setCookieMaxAge(60*60);
        return resolver;
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageResource()  {
        var messageResource= new ReloadableResourceBundleMessageSource();

        // Read file i18n/messages_xxx.properties
        // Example: i18n/messages_en.properties
        messageResource.setBasename("classpath:i18n/message");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("language");
        registry.addInterceptor(localeInterceptor).addPathPatterns("/*");
    }

    public Properties additionalProperties() {
        var properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("javax.persistence.validation.mode", "none"); //maybe making error ?
        return properties;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CategoryFormatter(applicationContext.getBean(CategoryService.class)));

        var stringToLocalDateConverter = new
                LocalDateFormatter("MM/dd/yyyy");
        registry.addFormatter(stringToLocalDateConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileUpload = env.getProperty("file.upload.path");

        registry.addResourceHandler("/Users/LENOVO/Pictures/Test_upload_file/**")
                .addResourceLocations("file:" + fileUpload);

        registry.addResourceHandler("/res/**")
                .addResourceLocations("WEB-INF/resources/css")
                .setCachePeriod(31556926);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("WEB-INF/resources/js")
                .setCachePeriod(31556926);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        var resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        resolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(52428800);
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        return messageSource;
    }

    @Bean
    public SpringSecurityDialect securityDialect(){
        return new SpringSecurityDialect();
    }
}
