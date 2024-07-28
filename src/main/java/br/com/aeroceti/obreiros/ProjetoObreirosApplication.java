/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros;

import org.springframework.boot.SpringApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Classe principal da Aplicacao usando o Spring Boot.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@SpringBootApplication
@OpenAPIDefinition
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ProjetoObreirosApplication  extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoObreirosApplication.class, args);
	}

}
/*                    End of Class                                            */