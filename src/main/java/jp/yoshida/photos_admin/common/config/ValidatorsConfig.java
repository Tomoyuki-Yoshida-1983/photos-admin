package jp.yoshida.photos_admin.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * バリデーターのコンフィグ
 */
@Configuration
@RequiredArgsConstructor
public class ValidatorsConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;

    @Bean
    public LocalValidatorFactoryBean validator() {

        // バリデーターのメッセージの参照先をデフォルトのValidationMessages.propertiesからmessages.propertiesに変更する。
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Override
    public Validator getValidator() {

        return validator();
    }
}
