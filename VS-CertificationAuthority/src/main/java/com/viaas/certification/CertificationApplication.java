package com.viaas.certification;

import com.viaas.certification.api.filter.FormAuthenticationFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@SpringBootApplication
@MapperScan(basePackages = "com.viaas.certification.mapper")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FormAuthenticationFilter.class))
public class CertificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CertificationApplication.class, args);
    }
}