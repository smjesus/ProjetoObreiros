/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  RECORD para tratar uma string como senha no Body da requisicao.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record BodyRequest(
        @Schema(example = "1234Abc%", description = "Senha para validacao.")
        String password) {

}
/*                    End of Record                                            */