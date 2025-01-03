package com.viaas.docker.config;

//import com.viaas.docker.plugin.Greetings;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * add plugin
 */
//@Configuration
public class PluginConfiguration {
    @Bean
    public SpringPluginManager pluginManager() {
        return new SpringPluginManager();
    }

//    @Bean
//    @DependsOn("pluginManager")
//    public Greetings greetings() {
//        return new Greetings();
//    }
}
