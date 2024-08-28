package com.viaas.docker.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 全局配置
 * @author Chajian
 * @date 2020/9/17
 */
@Component
@Configuration
public class AppServiceConfig implements WebMvcConfigurer {

    @Value("${space.root:}")
    private static String rootSpace;

    public static String userAvatarSpaceFormat = "%s"+ File.separator+"%s";

//    public FilterRegistrationBean<Filter> filterFilterRegistrationBean(){
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
//        filterRegistrationBean.setFilter((Filter) shiro);
//        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
//        filterRegistrationBean.setAsyncSupported(true);
//        filterRegistrationBean.setEnabled(true);
//        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
//        return filterRegistrationBean
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:"+getRootSpace()+"/");
    }

    public static String getRootSpace() {
        if(StringUtils.isEmpty(rootSpace)){
            rootSpace = System.getProperty("user.dir");
        }
        return rootSpace;
    }

    public static void setRootSpace(String rootSpace) {
        rootSpace = rootSpace;
    }

    public static String getUserAvatarSpace() {
        return String.format(userAvatarSpaceFormat,rootSpace,"avatar");
    }

}
