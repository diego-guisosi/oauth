package br.com.dixy.oauth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootApplication
public class AuthResourceServerApplication {

	private @Autowired DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(AuthResourceServerApplication.class, args);
	}

	@Autowired
	public void init(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication().dataSource(dataSource).withUser("dixy").password("secret").roles("USER",
				"ACTUATOR");
	}

}
