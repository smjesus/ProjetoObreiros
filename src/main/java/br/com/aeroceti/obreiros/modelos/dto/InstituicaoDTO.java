/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 *  RECORD para tratar objetos de Instituicoes transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record InstituicaoDTO(
        @Schema(example = "0", description = "ID na Base de dados do objeto")
        Long instituicaoID,

        @NotBlank
        @Schema(example = "Grupo de Estudos de Manaus", description = "Nome da Instituicao ou do Grupo")
        String nome,

        @Schema(example = "111.222.333/0001-44", description = "CNPJ Válido para cadastrar a instituicao (formatado ou não)")
        String cnpj,

        @NotBlank
        @Schema(example = "EOS_Manaus", description = "Sigla da Instituição (ou grupo) como é conhecida")
        String sigla,

        @Schema(example = "99877776666", description = "Telefone de Contato da Instituição (formatado ou não)")
        String telefone,

        @Schema(example = "caminho@gmail.com", description = "Endereço Eletronico (e-mail) da Instituição")
        String enderecoEletronico,

        @Schema(example = "https://maps.app.goo.gl/hyLVnBmo9QYuA9uw7", description = "URL do Google Maps para a Localização da Instituição")
        String googleMaps,

        @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
        Long versao

) {
}
/*                    End of Record                                            */