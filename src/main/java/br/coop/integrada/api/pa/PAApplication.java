package br.coop.integrada.api.pa;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
@SecurityScheme(name = "javainuseapi", scheme = "Bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class PAApplication {	
		
	@Bean
	public TaskScheduler task() {
		return new ThreadPoolTaskScheduler();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(PAApplication.class, args);
	}	
	
	@PostConstruct
	public void init(){
	   TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}
}
