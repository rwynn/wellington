package org.github.rwynn.wellington.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.github.rwynn.wellington.properties.ApplicationProperties;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ApplicationProperties appProps;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
        argumentResolvers.add(new FilterHandlerArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      if (appProps.isDevelopment()) {
        Properties devProps = new Properties();
        try {
          InputStream is = this.getClass()
            .getClassLoader()
            .getResourceAsStream("dev.properties");
          devProps.load(is);
          String location = devProps.getProperty("resources.root");
          registry.addResourceHandler("/**")
            .addResourceLocations(location)
            .setCachePeriod(0);
          is.close();
        } catch (IOException ex) {
          logger.error(ex.getMessage());
          throw new RuntimeException(ex.getMessage(), ex);
        }
      }
    }
}
