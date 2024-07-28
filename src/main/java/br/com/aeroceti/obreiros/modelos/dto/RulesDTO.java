/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *  RECORD para tratar objetos de Permissoes transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record RulesDTO  (
        @Schema(example = "0", description = "ID na Base de dados do objeto")
        Long rulesID, 
        
        @NotBlank
        @Size(min=5, max=50, message = "Nome precisa ter no mínimo 5 caracteres")
        @Schema(example = "Colaborador", description = "Nome da Permissão/Nivel para os úsuarios")
        String ruleName ) {
}
/*                    End of Record                                            */