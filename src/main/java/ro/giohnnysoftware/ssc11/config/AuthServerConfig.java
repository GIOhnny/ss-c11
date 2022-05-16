package ro.giohnnysoftware.ssc11.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

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
                .authorizedGrantTypes("password")
        .and()
                .withClient("client2")
                .secret("secret2")
                .scopes("read")
                .authorizedGrantTypes("authorization_code")
                .redirectUris("http://localhost:9090");
        /* in browser http://localhost:8080/oauth/authorize?response_type=code&client_id=client2&scope=read
           in postman http://localhost:8080/oauth/token?grant_type=authorization_code&scope=read&code=CNYHvN
           cu codul de la primul apel si BasicAuth client2/secret2
        */
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }


}
