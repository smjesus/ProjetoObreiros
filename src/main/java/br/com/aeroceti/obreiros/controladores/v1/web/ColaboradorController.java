/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.web;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.colaboradores.Endereco;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;
import br.com.aeroceti.obreiros.servicos.ColaboradorService;
import br.com.aeroceti.obreiros.servicos.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe Controller MVC para o objeto COLABORADOR (Usuarios).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/colaborador/")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final EnderecoService enderecoService;
    private final Logger logger = LoggerFactory.getLogger(ColaboradorController.class);

    public ColaboradorController(ColaboradorService casaServices, EnderecoService addSvc) {
        this.colaboradorService = casaServices;
        this.enderecoService    = addSvc;
    }

    /**
     * Listagem de TODOS os Colaboradores cadastrados no Banco de dados.
     * 
     * Caso desejar ordenar por nome em ordem alfabetica, 
     * passar o valor TRUE senao FALSE
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param ordenar - Verdadeiro se desejar ordenar os nomes em ordem alfabetica
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @RequestMapping("/listar/{ordenar}")
    public String listagem(Model modelo, @PathVariable boolean ordenar) {
        logger.info("Recebida requisicao para listar todos os colaboradores ...");
        modelo.addAttribute("usuarios",colaboradorService.listar(ordenar));
        return "colaborador/listagem";
    }
    
    /**
     * Formulario de cadastro de Colaborador na base de dados.
     *
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um colaborador.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrar(Model modelo) {
        modelo.addAttribute("colaborador", new Colaborador());
        logger.info("Recebida requisicao para cadastro:  encaminhando ao formulario ...");
        return "colaborador/cadastro";
    }

    /**
     * Metodo para gravar uma Colaborador na base de dados.
     * 
     * Esta funcao envia os dados do Cadastro (Formulario) de um Colaborador para o banco de dados.
     *
     * @param usuario - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * @param modelo - Objeto Model para injetar dados na View
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/cadastrar")
    public String salvar(@Valid Colaborador usuario, BindingResult result, RedirectAttributes atributes, Model modelo) {
        logger.info("Recebida requisicao para gravar um cadastro...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Erro na Validacao!");
            return "colaborador/cadastro" ;
        }
        if ( colaboradorService.verificar(usuario.getCleanCPF()) ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Colaborador já Existe! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Colaborador já Existe!");
            return "colaborador/cadastro" ;
        }
        // Se nao houver erros, salva os dados:
        usuario.setColaboradorID(null);
        if ( colaboradorService.gravar(usuario).getStatusCode().equals(HttpStatus.CREATED) ) {
            atributes.addFlashAttribute("mensagem", "Colaborador salvo com sucesso!");
            return "redirect:/colaborador/listar/0";
        }
        atributes.addFlashAttribute("mensagem", "Instituicao NAO cadastrada!");
        return "colaborador/cadastro" ;
    }
    
    /**
     * Apresenta o formulario para atualizar uma Colaborador na base de dados.
     * 
     * @param id - ID do objeto a ser persistido
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atualizar/{id}")
    public String atualizar( @PathVariable("id") long id, Model modelo, RedirectAttributes atributes ) {
        logger.info("Recebida requisicao para editar um cadastro...");
        Optional<Colaborador> usuarioSolicitado = colaboradorService.buscar(id);
        if( usuarioSolicitado.isPresent() ) {
            Colaborador usuario = usuarioSolicitado.get();
            logger.info("APRESENTANDO os dados para alteracao: " + usuario.getNome());
            modelo.addAttribute("colaborador", usuario);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/colaborador/listar/0";
        }
        return "colaborador/editar";
    }

    /**
     * Atualiza uma Colaborador (com ID valido) que esteja na base de dados.
     * 
     * @param usuario - objeto a ser persistido 
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto de manipulacao da view pelo Spring
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar")
    public String atualizar(@Valid Colaborador usuario, BindingResult result, RedirectAttributes atributes) {
        if (result.hasErrors()) {
            logger.info("ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "colaborador/editar";
        }
        colaboradorService.gravar(usuario);
        logger.info("Solicitando a Listagem GERAL. ");
        atributes.addFlashAttribute("mensagem", "ATUALIZAÇÃO realizada com sucesso!");
        return "redirect:/colaborador/listar/0";
    }
    
    /**
     * DELETA uma Colaborador da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param atributes - objeto de manipulacao da view pelo Spring
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletar( @PathVariable("id") long id, RedirectAttributes atributes) {
        logger.info("Buscando COLABORADOR para DELETAR... " );
        Optional<Colaborador> usuarioSolicitado = colaboradorService.buscar(id);
        if( usuarioSolicitado.isPresent() ) {
            Colaborador usuario = usuarioSolicitado.get();
            logger.info("DELETANDO Colaborador: " + usuario.getNome());
            // remove usuario das PERMISSOES:
            for ( Rules book : usuario.getPermissoes() ) {
                book.removeColaborador(usuario);
            }
            // remove usuario da INSTITUICAO:
            usuario.setInstituicao(null);
            colaboradorService.deletar(usuario);
            atributes.addFlashAttribute("mensagem", "Colaborador EXCLUíDO do sistema!");
        } else {
            atributes.addFlashAttribute("mensagem", "Erro na Exclusão: Referencia Inválida!");
        }
        return "redirect:/colaborador/listar/0";
    }
        
    /**
     * Apresenta o formulario para atualizar um ENDERECO de um Colaborador na base de dados.
     * 
     * @param id - ID do usuario que se deseja cadastrar um endereco
     * @param modelo - objeto de manipulacao da view pelo Spring
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/endereco/{id}")
    public String endereco( @PathVariable("id") long id, Model modelo, RedirectAttributes atributes  ) {
        logger.info("Recebida requisicao para atribuir endereco ...");
        Optional user = colaboradorService.buscar(id);
        if( user.isPresent() ) {
            Colaborador colaborador    =  (Colaborador) user.get();
            modelo.addAttribute("colaborador", colaborador);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/colaborador/listar/0"; 
        }
        logger.info("Apresentando o formulario para cadastrar endereço ...");        
        return "colaborador/endereco";
    }

    /**
     * Grava o ENDERECO de um Colaborador na base de dados.
     * 
     * @param usuario - objeto a ser adicionado ou editado o endereco 
     * @param atributes - objeto de manipulacao da view pelo Spring
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/endereco")
    @Transactional
    public String endereco( Colaborador usuario, RedirectAttributes atributes  ) {
        logger.info("Recebida requisicao para gravar o endereco ...");
        Optional user = colaboradorService.buscar(usuario.getColaboradorID());
        if( user.isPresent() ) {
            Colaborador colaborador    =  (Colaborador) user.get();
            Endereco endereco = usuario.getEndereco();
            if( endereco.getEnderecoID() == null || endereco.getEnderecoID() == 0L) {
                logger.info("Endereço NOVO será gravado no banco... ");
                endereco.setEnderecoID(null);
                enderecoService.gravar(endereco);
            }
            enderecoService.atualizar(endereco);
            logger.info("Endereço [{}] gravado no banco... ", endereco.getEnderecoID());
            colaborador.setEndereco(endereco);
            colaboradorService.atualizar(colaborador);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/colaborador/listar/0"; 
        }
        logger.info("Dados do usuário atualizado com sucesso!");        
        return "redirect:/colaborador/listar/0";
    }

}
/*                    End of Class                                            */