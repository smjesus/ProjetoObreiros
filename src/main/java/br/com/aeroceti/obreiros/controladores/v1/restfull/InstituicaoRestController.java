/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.restfull;

import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.aeroceti.obreiros.modelos.dto.InstituicaoDTO;
import br.com.aeroceti.obreiros.servicos.InstituicaoService;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.componentes.StringTools;
import br.com.aeroceti.obreiros.controladores.exceptions.EntidadeExistenteOuAusenteException;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;
import br.com.aeroceti.obreiros.servicos.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe REST Controller para o objeto INSTITUICAO (Casas Espiritas).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/instituicao/")
@Tag(name = "Instituições", description = "Entry Point para manipular os dados das Instituições cadastradas.")
public class InstituicaoRestController {

    private final ModelMapper mapeador;
    private final InstituicaoService instituicaoService;
    private final ColaboradorService colaboradorService;
    private final Logger logger = LoggerFactory.getLogger(InstituicaoRestController.class);

    public InstituicaoRestController(ModelMapper map, InstituicaoService casaServices, ColaboradorService cs) {
        this.instituicaoService = casaServices;
        this.colaboradorService = cs;
        this.mapeador           = map;
    }

    /**
     * Listagem de TODAS as Instituicoes cadastradas no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ArrayList em JSON com os objetos cadastrados
     */
    @GetMapping("/listar/{ordenar}")
    @Operation(summary = "Listar todas as Instituições", description = "Este Entry Point obtem uma listagem (ordenada ou não) de todos as instituições espíritas cadastradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista das Instituições", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class))))
    })
    public ResponseEntity<?> listagem( @PathVariable boolean ordenar ) {
        logger.info("Recebida requisicao na API para listar todas as instituicoes ...");
        return  new ResponseEntity<>( instituicaoService.listar(ordenar), HttpStatus.OK);
    }

    /**
     * Cria uma Instituicao na base de dados.
     *
     * Esta funcao cria uma Instituicao na base de dados.
     *
     * @param instituicao - Objeto a ser persistido ou atualizado no banco
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @PutMapping("/cadastrar")
    @Operation(summary = "Grava uma Instituicao no Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Instituicao e (se corretos) grava no Banco de Dados.<BR>"
                        + "Os seguintes campos <b>podem ser omitidos</b>: instituicaoID, googleMaps e versao.  Os demais são OBRIGATÓRIOS para a validação do objeto.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "ResponseEntity com os dados da Instituicao salva", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid InstituicaoDTO instituicao) {
        logger.info("Requisicao na API para cadastrar uma Instituicao ...");
        if (instituicao.nome().equals("")) {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: NOME da Instituicao precisa ser preenchido!", "NOME da Instituicao AUSENTE ou INVÁLIDO" );
        }
        if ( !instituicao.cnpj().isBlank() && instituicaoService.verificar(StringTools.setCnpjLimpo( instituicao.cnpj().trim() ) ) ) {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: Instituicao já Existe!", "INSTITUIÇÃO JÁ CADASTRADA" );
        }
        // Salva no Banco de dados atraves da Classe de Servico
        Instituicao casaEspirita = (Instituicao) mapeador.map(instituicao,Instituicao.class);
        casaEspirita.setInstituicaoID(null);
        return instituicaoService.gravar(casaEspirita);
    }

    /**
     * Atualiza uma Instituicao na base de dados.
     *
     * @param instituicao - Objeto a ser persistido ou atualizado no banco
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
    @Operation(summary = "Atualiza uma Instituicao do Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Instituicao e (se corretos) atualiza os dados no Banco de Dados.<BR>"
                       + "<b>Somente o campor google pode ser omitido</> desde que o mesmo não contenha dados válidos.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Instituicao atualizada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> atualizar(@RequestBody @Valid InstituicaoDTO instituicao) {
        Instituicao casaEspirita;
        logger.info("Recebida requisicao na API para atualizacao de dados ...");
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(instituicao.instituicaoID());
        if( instituicaoSolicitada.isPresent() ) {
            casaEspirita = (Instituicao) mapeador.map(instituicao,Instituicao.class);
            logger.info("ALTERANDO  os dados do " + casaEspirita.getSigla());
        } else {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: Instituicao com o ID informado não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return instituicaoService.atualizar(casaEspirita);
    }

    /**
     * DELETA uma Instituicao da base de dados pelo ID informado.
     *
     * @param id - rulesID da instituicao que se procura
     * @return String contendo uma mensagem de sucesso ou erro
     */
    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "DELETA uma Instituicao do Banco de Dados", description = "Este Entry Point valida os dados recebidos de uma Instituicao e (se corretos) EXCLUI a instituicao informada no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "MessageRequest com uma mensagem de SUCESSO do processamento", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageRequest.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Instituicao instituicaoInformada;
        logger.info("Recebida requisicao na API para excluir dados ...");
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(id);
        if( instituicaoSolicitada.isPresent() ) {
            instituicaoInformada = instituicaoSolicitada.get();
            // remove a instituicao dos usuarios:
            if( !instituicaoInformada.getColaboradores().isEmpty() ) {
                for ( Colaborador book : instituicaoInformada.getColaboradores() ) {
                    instituicaoInformada.removeColaborador(book);
                }
            }
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao foi deletada: Instituicao com o ID informado não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return instituicaoService.deletar(instituicaoInformada);
    }
    
    /**
     * Obtem uma Instituicao da base de dados pelo NOME informado.
     *
     * @param nome - nome da instituicao que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/nome/{nome}")
    @Operation(summary = "APRESENTA uma Instituicao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Instituicao conforme o NOME informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Instituicao encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterInstituicao(@PathVariable String nome) {
        Instituicao instituicaoInformada;
        logger.info("Obtendo uma instituicao com o nome de " + nome);
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(nome);
        if( instituicaoSolicitada.isPresent() ) {
            instituicaoInformada = instituicaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Instituicao com o NOME informado não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(instituicaoInformada, HttpStatus.OK);
    }
    
    /**
     * Obtem uma Instituicao da base de dados pelo ID informado.
     *
     * @param id - do objeto que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/{id}")
    @Operation(summary = "APRESENTA uma Instituicao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Instituicao conforme o ID informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Instituicao encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterInstituicao(@PathVariable Long id) {
        Instituicao instituicaoInformada;
        logger.info("Obtendo uma instituicao com o ID = " + id);
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(id);
        if( instituicaoSolicitada.isPresent() ) {
            instituicaoInformada = instituicaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Instituicao com o ID informado não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(instituicaoInformada, HttpStatus.OK);
    }

    /**
     * Obtem uma Instituicao da base de dados pelo ID informado.
     *
     * @param cnpj - do objeto que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/cnpj/{cnpj}")
    @Operation(summary = "APRESENTA uma Instituicao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Instituicao conforme o CNPJ informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Instituicao encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> buscarPorCNPJ(@PathVariable String cnpj) {
        Instituicao instituicaoInformada;
        logger.info("Obtendo uma instituicao com o CNPJ " + cnpj);
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscarCNPJ(cnpj);
        if( instituicaoSolicitada.isPresent() ) {
            instituicaoInformada = instituicaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Instituicao com o CNPJ informado não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(instituicaoInformada, HttpStatus.OK);
    }    

    /**
     * Obtem uma Instituicao da base de dados pelo ID informado.
     *
     * @param sigla - do objeto que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/sigla/{sigla}")
    @Operation(summary = "APRESENTA uma Instituicao do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de uma Instituicao conforme a SIGLA informada na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Instituicao encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Instituicao.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> buscarPelaSIGLA(@PathVariable String sigla) {
        Instituicao instituicaoInformada;
        logger.info("Obtendo uma instituicao com a SIGLA " + sigla);
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscarSIGLA(sigla);
        if( instituicaoSolicitada.isPresent() ) {
            instituicaoInformada = instituicaoSolicitada.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Instituicao com a SIGLA informada não Existe!", "INSTITUIÇÃO NÃO CADASTRADA" );
        }
        return new ResponseEntity<>(instituicaoInformada, HttpStatus.OK);
    }  
    
    /**
     * Listagem dos Colaboradores que frequentam uma INSTITUICAO
     * 
     * @param instituicaoID - ID da Instituicao
     * 
     * @return ArrayList em JSON com os objetos cadastrados
     */
    @GetMapping("/participantes")
    @Operation(summary = "Listar Participantes de uma Instituição", description = "Este Entry Point obtem uma listagem ordenada de todos os Participantes de uma Instituição.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista dos Colaboradores", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> listarParticipantes( Long instituicaoID ) {
        logger.info("Recebida requisicao para listar os participantes de uma instituicao ...");
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(instituicaoID);
        if( !instituicaoSolicitada.isPresent() || instituicaoSolicitada.isEmpty() ) {
            throw new EntidadeExistenteOuAusenteException( "Busca não realizada: Instituicao com o ID informado não Existe!", "INSTITUICAO NÃO CADASTRADA" );
        }
        logger.info("Obtendo os Participantes da Instituicao: {}", instituicaoSolicitada.get().getSigla());            
        return  new ResponseEntity<>( colaboradorService.participantes(instituicaoSolicitada.get()), HttpStatus.OK);
    }

}
/*                    End of Class                                            */