/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author Ngn
 */
@Configuration
public class AdvantageDataSourceConfig {

    @Bean(name = "advantageDataSource")
    public DataSource advantageDataSource(
            @Value("${advantage.datasource.url}") String url,
            @Value("${advantage.datasource.username}") String user,
            @Value("${advantage.datasource.password}") String pass) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.extendedsystems.jdbc.advantage.ADSDriver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        return ds;
    }

    @Bean
    public JdbcTemplate advantageJdbcTemplate(
            @Qualifier("advantageDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
