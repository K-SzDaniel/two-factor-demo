package milthdev.twofactordemo.configurations;

import org.apache.commons.codec.binary.Base32;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class BeanConfig {

    @Bean
    public SecureRandom getRandom() {
        return new SecureRandom();
    }

    @Bean
    public Base32 getBase32() {
        return new Base32();
    }

}
