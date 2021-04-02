package com.oathc.authclient.conf.oauth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.Arrays;


@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {

    private static final String REDIRECT_URI = "/thirdLogin";

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext context,
                                                 OAuth2ProtectedResourceDetails details) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(details, context);
        AuthorizationCodeAccessTokenProvider authCodeProvider = new AuthorizationCodeAccessTokenProvider();
        // 否则提示 Possible CSRF detected - state parameter was required but no state could be found
        authCodeProvider.setStateMandatory(false);
        AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(authCodeProvider));
        template.setAccessTokenProvider(provider);
        return template;
    }

    /**
     * 注册处理redirect uri的filter
     * 拦截redirectUri,根据authentication code获取token，依赖前面两个对象
     * @param oauth2RestTemplate
     * @return
     */
    @Bean
    public OAuth2ClientAuthenticationProcessingFilter oauth2ClientAuthenticationProcessingFilter(
            OAuth2RestTemplate oauth2RestTemplate, RemoteTokenServices tokenServices) {
        // 只拦截 redirectUri
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(REDIRECT_URI);
        filter.setRestTemplate(oauth2RestTemplate);
        tokenServices.setRestTemplate(oauth2RestTemplate);
        filter.setTokenServices(tokenServices);
        // 这里可以设置回调成功的后续处理，继承SavedRequestAwareAuthenticationSuccessHandler 使用自定义的处理器
        // 比如存储用户的信息啊，access_token啊，绑定用户登录什么的，这里无账号体系，就不配了
        // filter.setAuthenticationSuccessHandler(new Oauth2AuthenticationSuccessHandler());
        return filter;
    }

    /**
     * RemoteTokenServices是用于向远程认证服务器验证token，同时获取token对应的用户的信息。
     * @param details
     * @return
     */
    @Bean
    public RemoteTokenServices tokenServices(OAuth2ProtectedResourceDetails details, ResourceServerProperties properties) {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(details.getClientId());
        tokenServices.setClientSecret(details.getClientSecret());
        tokenServices.setCheckTokenEndpointUrl(properties.getTokenInfoUri());
        return tokenServices;
    }

}
