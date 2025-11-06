package com.url.shortener.confgurations;


import com.url.shortener.helper.Snowflake;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProjectConfiguration {

    @Value("${snowflake.config.node-id}")
    private String nodeId;

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncode() {
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public Snowflake getSnowflakeBean() {
        return new Snowflake(Long.parseLong(nodeId));
    }
}
