/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.configuracoes;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  CONFIGURACAO: Cria um ModelMapper para uso no sistema.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Configuration
public class ModelMapperConfiguration {
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper().registerModule(new RecordModule() );
    }
}
/*                    End of Class                                            */