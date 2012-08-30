package com.wixpress.streaming.spring;

import com.wixpress.streaming.chat.ChatCoordinator;
import com.wixpress.streaming.opentok.OpenTokFacade;
import com.wixpress.streaming.paypal.PayPalFacade;
import com.wixpress.streaming.wix.AppInstanceDao;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Configuration
@Import({EmbeddedAppVelocityBeansConfig.class})
@EnableWebMvc
public class EmbeddedAppConfig
{
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AppInstanceDao instanceDao() {
        return new AppInstanceDao();
    }

    @Bean
    public ChatCoordinator chatCoordinator() {
        return new ChatCoordinator(openTokFacade());
    }

    @Bean
    public OpenTokFacade openTokFacade() {
        return new OpenTokFacade();
    }

    @Bean
    public PayPalFacade payPalFacade() {
        return new PayPalFacade();
    }
}
