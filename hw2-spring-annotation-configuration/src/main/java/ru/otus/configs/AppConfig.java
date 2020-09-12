package ru.otus.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("ru.otus")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
@Configuration
public class AppConfig {

}
