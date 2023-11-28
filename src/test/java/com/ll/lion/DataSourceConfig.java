package com.ll.lion;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "database_aws_RDS")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:mysql://database-1.cgzxv8yi0pzb.ap-northeast-2.rds.amazonaws.com:3306/shoppingmall")
                .username("admin")
                .password("tkwk1234")
                .type(HikariDataSource.class)
                .build();
    }
}
