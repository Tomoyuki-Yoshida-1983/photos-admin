package jp.yoshida.photos_admin.common.config;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.common.constant.StandardsConstants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

/**
 * サーブレットのコンフィグ
 */
@Configuration
public class ServletsConfig {

    @Bean
    @SuppressWarnings("rawtypes")
    public ServletRegistrationBean servletRegistration(DispatcherServlet dispatcherServlet) {

        @SuppressWarnings("unchecked")
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet);
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                StandardsConstants.JAVA_IO_TMPDIR,
                KeyWordsConstants.CONFIG_SERVLET_MAX_FILE_SIZE,
                KeyWordsConstants.CONFIG_SERVLET_MAX_REQUEST_SIZE,
                KeyWordsConstants.NUMBER_ZERO);
        servletRegistrationBean.setMultipartConfig(multipartConfig);
        return servletRegistrationBean;
    }
}
