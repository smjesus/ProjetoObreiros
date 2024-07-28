/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.configuracoes;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  CONFIGURACAO para o Spring DOC - Documentacao da API.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */

@Configuration
public class SpringDocConfiguration {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Obreiros do Cristo API")
                        .version("v1 - 0.1")
                        .description("REST API Obreiros do Cristo - Sistema de Cadastro para trabalhadores espíritas.")
                        .license(new License()
                                .name("Apache Licence 2.0")
                                .url("https://opensource.org/license/apache-2-0/")
                        ));
    }
    
}
/*                    End of Class                                            */