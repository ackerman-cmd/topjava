package ru.javawebinar.topjava.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.active", "datajpa");
        super.onStartup(servletContext);
    }
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationConfig.class}; // Корневая конфигурация app
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class}; // Подключаем конфигурацию Spring MVC
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"}; // Все запросы отправляются в DispatcherServlet
    }
}
