package br.coop.integrada.api.pa.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIDefinitio {
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info(new Info().title("Gênesis 1.0")
				.version("1.0")
				.description("Projeto Gênesis - Desenvolvido pela equipe técnica da Integrada Cooperativa Agroindustrial de Londrina (PR) em parceria com a InSTI Inovação em Soluções de Projetos TI de Paulínia (SP).")				
				.contact(new Contact().name("INTEGRADA AGROINDUSTRIAL")
						.url("https://www.integrada.coop.br/")
						.email("java.terceiro@integrada.coop.br"))
				.license(new License()
						.name("Apache License Version 2.0")
						.url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
				);
	}

}
