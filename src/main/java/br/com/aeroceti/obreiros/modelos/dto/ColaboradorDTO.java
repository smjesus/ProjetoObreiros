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
 *  RECORD para tratar objetos de Usuarios transitorios.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public record ColaboradorDTO  (
        @Schema(example = "0", description = "ID na Base de dados do objeto")
        Long colaboradorID, 
        
        @NotBlank
        @Schema(example = "111.222.333-44", description = "CPF Válido para cadastrar o úsuario")
        String cpf,
        
        @NotBlank
        @Schema(example = "Joao", description = "Nome do úsuario")
        String nome,
        
        @NotBlank
        @Schema(example = "da Silva", description = "Sobrenome do úsuario")
        String sobrenome,
        
        @NotBlank
        @Schema(example = "joao@gmail.com", description = "E-Mail válido do úsuario")
        String email,
        
        @NotBlank
        @Schema(example = "11/22/9999", description = "Data de Nascimento do úsuario no formato dd/mm/aaaa")
        String dataNascimento,
        
        @Schema(example = "M", description = "Sexo do úsuario sendo [M]=Masculino, [F]=Feminino e [N] - Nao Informar")
        String sexo,
        
        @Schema(example = "99877776666", description = "Telefone com Whatsapp do úsuario no formato (99) 9.8888-7777")
        String whatsapp,
        
        @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
        Long versao
        
        ) {
}
/*                    End of Record                                            */