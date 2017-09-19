package org.superbiz.struts;

import com.opensymphony.module.sitemesh.filter.PageFilter;
import org.apache.struts2.dispatcher.ActionContextCleanUp;
import org.apache.struts2.dispatcher.FilterDispatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(App.class, args);
    }

    @Bean
    @Order(1)
    public FilterRegistrationBean strutsCleanup() {
        return createFilter(
                "struts-cleanup",
                "/*",
                new ActionContextCleanUp());
    }

    @Bean
    @Order(2)
    public FilterRegistrationBean sitemesh() {
        return createFilter(
                "sitemesh",
                "/*",
                new PageFilter());
    }

    @Bean
    @Order(3)
    public FilterRegistrationBean struts() {
        FilterRegistrationBean bean = createFilter(
                "struts2",
                "/*",
                new FilterDispatcher());
        bean.addInitParameter("actionPackages", "com.lq");
        return bean;
    }

    private FilterRegistrationBean createFilter(String name, String urlPattern, Filter filter) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(filter);
        bean.setName(name);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(urlPattern);
        bean.setUrlPatterns(urlPatterns);
        return bean;
    }
}
