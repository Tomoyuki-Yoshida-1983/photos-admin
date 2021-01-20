package jp.yoshida.photos_admin.common.config;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * データソースのコンフィグ
 */
@Configuration
public class DataSourcesConfig {

    @Bean
    public DataSource dataSource() {

        @SuppressWarnings("rawtypes")
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(KeyWordsConstants.CONFIG_DB_DRIVER_CLASS_NAME);
        dataSourceBuilder.url(KeyWordsConstants.CONFIG_DB_URL);
        dataSourceBuilder.username(KeyWordsConstants.CONFIG_DB_USERNAME);
        return dataSourceBuilder.build();
    }
}
