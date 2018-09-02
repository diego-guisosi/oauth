package br.com.dixy.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	public ApprovalStore approvalStore() {
		TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
		tokenApprovalStore.setTokenStore(tokenStore());
		return tokenApprovalStore;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("my-trusted-client")
				.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
				.scopes("read", "write", "trust")
				.resourceIds("oauth2-resource")
				.accessTokenValiditySeconds(180)
				.redirectUris("/oauth/accept_code")
				.and()
				.withClient("client-credential")
				.authorizedGrantTypes("client_credentials")
				.authorities("ROLE_CLIENT")
				.scopes("read")
				.resourceIds("oauth2-resource")
				.accessTokenValiditySeconds(180) // it should be set too a much longer expiration time. Default is 44000
				.secret("client-password");

	}

}
