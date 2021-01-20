package jp.yoshida.photos_admin.common.config;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * メッセージのコンフィグ
 */
@Configuration
public class MessagesConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {

        ReloadableResourceBundleMessageSource bundleMessageSource = new ReloadableResourceBundleMessageSource();
        bundleMessageSource.setBasename(KeyWordsConstants.CONFIG_MESSAGES_BASENAME);
        bundleMessageSource.setCacheSeconds(KeyWordsConstants.CONFIG_MESSAGES_CACHE_SECONDS);
        bundleMessageSource.setDefaultEncoding(KeyWordsConstants.CONFIG_MESSAGES_DEFAULT_ENCODING);
        return bundleMessageSource;
    }
}
