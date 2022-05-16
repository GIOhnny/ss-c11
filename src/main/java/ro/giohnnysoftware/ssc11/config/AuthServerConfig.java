package ro.giohnnysoftware.ssc11.config;

import org.apache.catalina.webresources.JarWarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    /*
        authorization_code /pkce
        password ----> deprecated
        client_credentials
        refresh_token
        implicit ----> deprecated

     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1")
                .secret("secret1")
                .scopes("read","write")
                .authorizedGrantTypes("password","refresh_token")
        //http://localhost:8080/oauth/token?grant_type=password&username=John&password=12345&scope=read
        //http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=33333
        .and()
                .withClient("client2")
                .secret("secret2")
                .scopes("read")
                .authorizedGrantTypes("authorization_code")
                .redirectUris("http://localhost:9090")
        /* in browser http://localhost:8080/oauth/authorize?response_type=code&client_id=client2&scope=read
           in postman http://localhost:8080/oauth/token?grant_type=authorization_code&scope=read&code=CNYHvN
           cu codul de la primul apel si BasicAuth client2/secret2
        */
                .and()
                .withClient("client3")
                .secret("secret3")
                .scopes("read")
                .authorizedGrantTypes("client_credentials");
        //http://localhost:8080/oauth/token?grant_type=client_credentials&scope=read
    }

    @Bean
    public TokenStore tokenStore() {
        var tokenStore = new JwtTokenStore(converter());
        return tokenStore;
    }

    @Bean
    public JwtAccessTokenConverter converter() {
        return new JwtAccessTokenConverter();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
        .tokenStore(tokenStore())
        .accessTokenConverter(converter());
    }


}
