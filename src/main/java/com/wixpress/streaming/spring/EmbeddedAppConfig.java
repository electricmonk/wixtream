package com.wixpress.streaming.spring;

import com.wixpress.streaming.domain.InstanceDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Configuration
@Import({EmbeddedAppVelocityBeansConfig.class})
public class EmbeddedAppConfig
{
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public InstanceDao instanceDao() {
        return new InstanceDao();
    }
}
