/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.restfull;

import br.com.aeroceti.obreiros.componentes.StringTools;
import br.com.aeroceti.obreiros.controladores.exceptions.EntidadeExistenteOuAusenteException;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.colaboradores.Endereco;
import br.com.aeroceti.obreiros.modelos.dto.BodyRequest;
import br.com.aeroceti.obreiros.modelos.dto.ColaboradorDTO;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import br.com.aeroceti.obreiros.servicos.ColaboradorService;
import br.com.aeroceti.obreiros.servicos.InstituicaoService;
import br.com.aeroceti.obreiros.servicos.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe REST Controller para o objeto COLABORADOR (Usuarios).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/colaborador/")
@Tag(name = "Colaboradores", description = "Entry Point para manipular os dados dos Colaboradores (usuários) cadastrados.")
public class ColaboradorRestController {

    private final ModelMapper mapeador;
    private final PasswordService passwordService;
    private final ColaboradorService colaboradorService;
    private final InstituicaoService instituicaoService;

    private final Logger logger = LoggerFactory.getLogger(ColaboradorRestController.class);

    public ColaboradorRestController(ColaboradorService userServices, ModelMapper map, PasswordService pws, InstituicaoService casaServices) {
        this.colaboradorService = userServices;
        this.instituicaoService = casaServices;
        this.mapeador           = map;
        this.passwordService    = pws;
    }

    /**
     * Listagem de TODOS os Colaboradores cadastrados no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * @return ArrayList em JSON com os objetos cadastrados
     */
    @GetMapping("/listar/{ordenar}")
    @Operation(summary = "Listar todos os Colaboradores", description = "Este Entry Point obtem uma listagem (ordenada ou não) de todos os colaboradores espíritas cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ResponseEntity com uma Lista dos Colaboradores", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class))))
    })
    public ResponseEntity<?> listagem( @PathVariable boolean ordenar ) {
        logger.info("Recebida requisicao na API para listar todas as instituicoes ...");
        return  new ResponseEntity<>( colaboradorService.listar(ordenar), HttpStatus.OK);
    }

    /**
     * Cria um Colaborador na base de dados.
     *
     * Esta funcao cria um Colaborador na base de dados.
     *
     * @param colaborador - Objeto a ser persistido ou atualizado no banco
     * @return Objeto (persistido) ou Mensagem com o erro ocorrido
     */
    @PutMapping("/cadastrar")
    @Operation(summary = "Grava um Colaborador no Banco de Dados", description = "Este Entry Point valida os dados recebidos de um Colaborador e (se corretos) grava no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "ResponseEntity com os dados do Colaborador salvo", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com uma mensagem de erro do processamento", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ColaboradorDTO colaborador) {
        logger.info("Requisicao na API para cadastrar um Colaborador ...");
     
        if ( colaboradorService.verificar( StringTools.setCnpjLimpo( colaborador.cpf() ) )) {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: Colaborador já Existe!", "COLABORADOR JÁ CADASTRADO" );
        }
        // Salva no Banco de dados atraves da Classe de Servico
        Colaborador participante = (Colaborador) mapeador.map(colaborador, Colaborador.class);
        participante.setColaboradorID(null);
        return colaboradorService.gravar(participante);
    }

    /**
     * Atualiza um Colaborador na base de dados.
     *
     * @param colaborador - Objeto a ser persistido ou atualizado no banco
     * @return Objeto (persistido) ou MensagemDTO (com o erro ocorrido)
     */
    @PutMapping("/atualizar")
    @Operation(summary = "Atualiza um Colaborador do Banco de Dados", description = "Este Entry Point valida os dados recebidos de um Colaborador e (se corretos) atualiza os dados no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador atualizados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> atualizar(@RequestBody @Valid ColaboradorDTO colaborador) {
        Colaborador participante;
        logger.info("Recebida requisicao na API para atualizacao de dados ...");
        Optional<Colaborador> colaboradorSolicitado = colaboradorService.buscar(colaborador.colaboradorID());
        if( colaboradorSolicitado.isPresent() ) {
            participante = (Colaborador) mapeador.map(colaborador, Colaborador.class);
        } else {
            throw new EntidadeExistenteOuAusenteException( "Dados nao cadastrados: Colaborador com o ID informado não Existe!", "COLABORADOR NÃO CADASTRADO" );
        }
        return colaboradorService.atualizar(participante);
    }

    /**
     * DELETA um Colaborador da base de dados pelo ID informado.
     *
     * @param id - ID do colaborador que se procura
     * @return String contendo uma mensagem de sucesso ou erro
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "DELETA um Colaborador do Banco de Dados", description = "Este Entry Point valida os dados recebidos de um Colaborador e (se corretos) EXCLUI a colaborador informada no Banco de Dados.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "MessageRequest com uma mensagem de SUCESSO do processamento", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageRequest.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Colaborador colaboradorInformado;
        logger.info("Recebida requisicao na API para excluir dados ...");
        Optional<Colaborador> colaboradorSolicitado = colaboradorService.buscar(id);
        if( colaboradorSolicitado.isPresent() ) {
            colaboradorInformado = colaboradorSolicitado.get();
            colaboradorInformado.getInstituicao().removeColaborador(colaboradorInformado);
            
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao foi deletada: Colaborador com o ID informado não Existe!", "COLABORADOR NÃO CADASTRADO" );
        }
        return colaboradorService.deletar(colaboradorInformado);
    }
    
    /**
     * Obtem um Colaborador da base de dados pelo ID informado.
     *
     * @param id - do objeto que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/{id}")
    @Operation(summary = "APRESENTA um Colaborador do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de um Colaborador conforme o ID informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterColaborador(@PathVariable Long id) {
        Colaborador colaboradorInformado;
        logger.info("Obtendo uma colaborador com o ID = " + id);
        Optional<Colaborador> colaboradorSolicitado = colaboradorService.buscar(id);
        if( colaboradorSolicitado.isPresent() ) {
            colaboradorInformado = colaboradorSolicitado.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Colaborador com o ID informado não Existe!", "COLABORADOR NÃO CADASTRADO" );
        }
        return new ResponseEntity<>(colaboradorInformado, HttpStatus.OK);
    }

    /**
     * Obtem um Colaborador da base de dados pelo CPF informado.
     *
     * @param cpf - CPF da colaborador que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "APRESENTA um Colaborador do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de um Colaborador conforme o CPF informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterColaborador(@PathVariable String cpf) {
        Colaborador colaboradorInformada;
        logger.info("Obtendo uma colaborador com o nome de " + cpf);
        Optional<Colaborador> colaboradorSolicitado = colaboradorService.buscarCPF(cpf);
        if( colaboradorSolicitado.isPresent() ) {
            colaboradorInformada = colaboradorSolicitado.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Colaborador com o CPF informado não Existe!", "COLABORADOR NÃO CADASTRADO" );
        }
        return new ResponseEntity<>(colaboradorInformada, HttpStatus.OK);
    }
        
    /**
     * Obtem um Colaborador da base de dados pelo NOME informado.
     *
     * @param nome - nome da colaborador que se procura
     * @return Objeto (persistido) ou Mensagem (com o erro ocorrido)
     */
    @GetMapping("/nome/{nome}")
    @Operation(summary = "APRESENTA um Colaborador do Banco de Dados", description = "Este Entry Point busca as informações no banco de dados de um Colaborador conforme o NOME informado na requisição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> obterColaboradorPorNome(@PathVariable String nome) {
        Colaborador colaboradorInformada;
        logger.info("Obtendo uma colaborador com o nome de " + nome);
        Optional<Colaborador> colaboradorSolicitado = colaboradorService.buscar(nome);
        if( colaboradorSolicitado.isPresent() ) {
            colaboradorInformada = colaboradorSolicitado.get();
        } else {
            throw new EntidadeExistenteOuAusenteException( "Entidade nao encontrada: Colaborador com o NOME informado não Existe!", "COLABORADOR NÃO CADASTRADO" );
        }
        return new ResponseEntity<>(colaboradorInformada, HttpStatus.OK);
    }

    /**
     * Para validar uma senha do Colaborador.
     * 
     * A senha deve respeitar os seguintes requisitos:
     *  - Verificar se a senha possui pelo menos 08 caracteres.
     *  - Verificar se a senha contém pelo menos uma letra maiúscula.
     *  - Verificar se a senha contém pelo menos uma letra minúscula.
     *  - Verificar se a senha contém pelo menos um dígito numérico.
     *  - Verificar se a senha contém pelo menos um caractere especial (e.g, !@#$%).
     * 
     * @param request - senha a ser validada
     * @return LIST com os requisitos nao atendidos.
     */
    @PostMapping("/validar-senha")
    @Operation(summary = "VALIDA uma senha que sera usada por um Colaborador", description = "Este Entry Point valida uma senha para um Colaborador conforme 5 requisitos basicos.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "ResponseEntity NoContent indicando senha valida",   content = @Content(mediaType = "application/json", examples = { @ExampleObject(name = "400", description = "{ ... }")} )),
        @ApiResponse(responseCode = "400", description = "LISTA com os requisitos nao cumpridos pela senha",  content = @Content(mediaType = "application/json", examples = { @ExampleObject(name = "400", description = "{ ... }") }))
    })
    public ResponseEntity<List<String>> isSafe(@RequestBody BodyRequest request){
        var failures = passwordService.validate(request.password());

        if (failures.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body(failures);
    }    
   
    /**
     * Grava a INSTITUICAO que um Colaborador frequenta.
     * 
     * @param usuarioID - ID do participante
     * @param instituicaoID - ID da Instituicao
     * 
     * @return String contendo uma mensagem de sucesso ou erro
     */
    @GetMapping("/atribuir/{usuarioID}/{instituicaoID}")
    @Operation(summary = "ASSOCIA um Colaborador à uma Instituição", description = "Este Entry Point valida o ID recebido de um Colaborador e o ID de uma Instituição e cadastra o usuario como Paricipante da Instituição.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador atualizados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> associarParticipante( @PathVariable("usuarioID") Long usuarioID, @PathVariable("instituicaoID") Long instituicaoID ) {
        logger.info("Recebida requisicao para gravar a instituicao em que um usuario participa ...");
        Optional participanteSolicitado = colaboradorService.buscar(usuarioID);
        Optional instituicaoSolicitada  = instituicaoService.buscar(instituicaoID);
        
        if(   ( !participanteSolicitado.isPresent() || participanteSolicitado.isEmpty() )
           && ( !instituicaoSolicitada.isPresent() || instituicaoSolicitada.isEmpty()   ) ) {
                throw new EntidadeExistenteOuAusenteException( "Associação não realizada: Instituicao ou Participante não Existe!", "ATRIBUIÇÃO NÃO REALIZADA" );
        }
        
        Colaborador colaborador = (Colaborador) participanteSolicitado.get();
        Instituicao instituicao = (Instituicao) instituicaoSolicitada.get();
        colaborador.setInstituicao(instituicao);
                
        return colaboradorService.atualizar(colaborador);
    }
    
    @GetMapping("/endereco/{usuarioID}")
    @Operation(summary = "Cadastra um ENDERECO de um Colaborador", description = "Este Entry Point valida o ID recebido de um Colaborador e cadastra o endereço recebido no corpo da requisição para o Usuario informado.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ResponseEntity com os dados da Colaborador atualizados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Colaborador.class)))),
        @ApiResponse(responseCode = "400", description = "ProblemDetail com a mensagem de erro do processamento",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProblemDetail.class))))
    })
    public ResponseEntity<?> atribuirEndereco( @PathVariable Long usuarioID, @RequestBody Endereco enderecoUsuario) {
        logger.info("Recebida requisicao para gravar o endereço de um usuario ...");
        Optional participanteSolicitado = colaboradorService.buscar(usuarioID);
        
        if( participanteSolicitado.isPresent() ) {


        System.out.println("Usuario: " + ((Colaborador) participanteSolicitado.get()).getNomeCompleto()  );
            System.out.println("Endereco: " + enderecoUsuario.getCep() + enderecoUsuario.getLogradouro());
            
        } else { 
            throw new EntidadeExistenteOuAusenteException( "Cadastro de Endereço não realizado: Participante não Existe!", "CADASTRO DE ENDEREÇO NÃO REALIZADO" );
        }
        
        
        return null;
    }

}
/*                    End of Class                                            */