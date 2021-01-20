package jp.yoshida.photos_admin.common.config;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * アプリケーション起動時のDBスキーマセットアップのコンフィグ
 */
@Configuration
public class DataSourceInitializersConfig {

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        ResourceDatabasePopulator resourceDatabasePopulator
                = new ResourceDatabasePopulator(new ClassPathResource(KeyWordsConstants.CONFIG_SCHEMA_SQL_PATH));
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
