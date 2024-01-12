package com.ll.lion;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
// 아마존 DB로 datasorce를 설정하기 위한 configuration
//    @Bean(name = "database_aws_RDS")
//    @ConfigurationProperties(prefix="spring.datasource")
//    public DataSource dataSource(){
//        return DataSourceBuilder.create()
//                .url("jdbc:mysql://database-1.cgzxv8yi0pzb.ap-northeast-2.rds.amazonaws.com:3306/shoppingmall")
//                .username("admin")
//                .password("tkwk1234")
//                .type(HikariDataSource.class)
//                .build();
//    }
}
