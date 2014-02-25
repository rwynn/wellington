package org.github.rwynn.wellington.convert;

import org.dozer.DozerBeanMapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class DozerConfig {

    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean() {
        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(new Resource[]{new ClassPathResource("bean-mapping.xml")});
        return dozerBeanMapperFactoryBean;
    }

    @Bean
    public DozerBeanMapper dozerBeanMapper(DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean) throws Exception {
        return (DozerBeanMapper) dozerBeanMapperFactoryBean.getObject();
    }


}
