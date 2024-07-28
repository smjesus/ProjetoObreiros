/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.restfull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.aeroceti.obreiros.modelos.dto.RulesDTO;
import br.com.aeroceti.obreiros.servicos.PermissoesService;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import org.springframework.transaction.annotation.Transactional;
import br.com.aeroceti.obreiros.controladores.exceptions.EntidadeExistenteOuAusenteException;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;

/**
 * Classe REST Controller para o objeto Rules (Permissoes).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/permissao/")
@Tag(name = "Permissões", description = "Entry Point para manipular o objeto Rules (Permissao)")
@CrossOrigin(origins = {"http://127.0.0.1:5173/", "https://victornunes-me.github.io/" })
public class PermissaoRestController {
    
    private final PermissoesService permissaoService;
    private final Logger logger = LoggerFactory.getLogger(PermissaoRestController.class);

    public PermissaoRestController(PermissoesService rulesServices) {
        this.permissaoService = rulesServices;
    }

    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados.
     * Caso desejar ordenar o nome em ordem alfabetica, passar o valor TRUE
     * senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ArrayList em JSON com os objetos cadastrados
     */
    @GetMapping("/listar/{ordenar}")
    @Operation(summary = "Listar todos as Permissões", description = "Este Entry Point obtem uma listagem (ordenada ou não) de todos as permissões (Rules) cadastradas.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista das Permissões", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rules.class))))
    })
    public ResponseEntity<?> listagem(@PathVariable boolean ordenar) {
        logger.info("Recebida requisicao na API para listar todas as Permissoes ...");
        return new ResponseEntity<>( permissaoService.listar(ordenar), HttpStatus.OK);
    }

    /**
     * Cria uma Permissao na base de dados.
     * Esta funcao cria uma Permissao na base de dados.
     *
     * @param permissao - Objeto Permissao a ser persistido ou atualizado no banco
     * @return Objeto Permissao (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/cadastrar")
    @Operation(summary = "Grava uma Permissao no Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Permissao e (se corretos) grava no Banco de Dados. O ID deve ser 0 (ZERO) para o cadastro de uma nova Permissão.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "ResponseEntity com os dados da Permissão salva", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rules.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> cadastrar(@RequestBody RulesDTO permissao) {
        logger.info("Recebida requisicao na API para cadastrar uma Permissao...");
        if (permissao.ruleName().isEmpty()) {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: NOME da Permissao precisa ser preenchido!", "NOME da Permissao AUSENTE ou INVÁLIDO" );
        }        
        if ( permissaoService.verificar(permissao.ruleName()) ) {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: Permissao já Existe!", "PERMISSÃO JÁ CADASTRADA" );
        }
        // Salva no Banco de dados atraves da Classe de Servico
        Rules novaPermissao = new Rules();
        novaPermissao.setRulesID(null); novaPermissao.setRuleName( permissao.ruleName().trim() );
        return permissaoService.gravar(novaPermissao);
    }

    /**
     * Atualiza uma Permissao na base de dados.
     *
     * @param permissao - Objeto Permissao a ser persistido ou atualizado no banco
     * @return Objeto Permissao (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
    @Operation(summary = "Atualiza uma Permissao do Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Permissao e (se corretos) atualiza os dados no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Permissão atualizada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rules.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> atualizar(@RequestBody RulesDTO permissao) {
        Rules permissaoAntiga;
        logger.info("Recebida requisicao na API para atualizacao de dados ...");
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(permissao.rulesID());
        if( permissaoSolicitada.isPresent() ) {
            permissaoAntiga = permissaoSolicitada.get();
            logger.info("Requisicao no PermissaoController: ALTERANDO  os dados do {}", permissaoAntiga.getRuleName());
            permissaoAntiga.setRuleName(permissao.ruleName());
        } else {
            throw new EntidadeExistenteOuAusenteException( "Dados nao atualizados: Permissao com o ID informado não Existe!", "PERMISSÃO NÃO CADASTRADA" );
        }
        return permissaoService.atualizar(permissaoAntiga);
    }

    /**
     * DELETA uma Permissao da base de dados pelo ID informado.
     *
     * @param id - rulesID da permissao que se procura
     * @return Objeto Permissao (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "DELETA uma Permissao do Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Permissao e (se corretos) EXCLUI a permissao informada no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "MessageRequest com uma mensagem de SUCESSO do processamento", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageRequest.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Rules permissaoInformada;
        logger.info("Recebida requisicao na API para excluir dados ...");
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(id);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
            // remove a permissao dos usuarios:
            if( !permissaoInformada.getColaboradores().isEmpty() ) {
                for ( Colaborador book : permissaoInformada.getColaboradores() ) {
                    permissaoInformada.removeColaborador(book);
                }
            }
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao foi deletada: Permissao com o ID informado não Existe!", "PERMISSÃO NÃO CADASTRADA" );
        }
        return permissaoService.deletar(permissaoInformada);
    }
    
    /**
     * Obtem uma Permissao da base de dados pelo NOME informado.
     *
     * @param nome - nome da permissao que se procura
     * @return Objeto Permissao (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/nome/{nome}")
    @Operation(summary = "APRESENTA uma Permissao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Permissao conforme o NOME informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Permissão encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rules.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterPermissao(@PathVariable String nome) {
        Rules permissaoInformada;
        logger.info("Obtendo uma permissao com o nome de {}", nome);
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(nome);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Permissao com o NOME informado não Existe!", "PERMISSÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(permissaoInformada, HttpStatus.OK);
    }
    
    /**
     * Obtem uma Permissao da base de dados pelo ID informado.
     *
     * @param id - rulesID da permissao que se procura
     * @return Objeto Permissao (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @GetMapping("/{id}")
    @Operation(summary = "APRESENTA uma Permissao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Permissao conforme o ID informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Permissão encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Rules.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterPermissao(@PathVariable Long id) {
        Rules permissaoInformada;
        logger.info("Obtendo uma permissao com o ID = {}", id);
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(id);
        if( permissaoSolicitada.isPresent() ) {
            permissaoInformada = permissaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Permissao com o ID informado não Existe!", "PERMISSÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(permissaoInformada, HttpStatus.OK);
    }

}
/*                    End of Class                                            */