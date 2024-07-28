/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;
import br.com.aeroceti.obreiros.servicos.PermissoesService;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.servicos.ColaboradorService;

/**
 * Classe Controller MVC para o objeto Permissao.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/permissao/")
public class PermissaoController {

    private final PermissoesService permissaoService;
    private final ColaboradorService colaboradorService;
    private final Logger logger = LoggerFactory.getLogger(PermissaoController.class);

    public PermissaoController(PermissoesService ps, ColaboradorService cs) {
        this.permissaoService = ps;
        this.colaboradorService = cs;
    }

    /**
     * Listagem de TODAS as Permissoes cadastradas no Banco de dados.
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
        logger.info("Recebida requisicao para listar todas as permissoes...");
        modelo.addAttribute("permissoes",permissaoService.listar(ordenar));
        return"permissao/listagem";
    }
    
    /**
     * Formulario de cadastro de Permissao na base de dados.
     * Esta funcao abre a Pagina de Cadastro (Formulario) de um permissao.
     *
     * @param modelo - Objeto Model para injetar dados na View
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrar(Model modelo) {
        modelo.addAttribute("rules", new Rules());
        logger.info("Requisicao no PermissaoController para cadastro:  encaminhando ao formulario ...");
        return "permissao/cadastrar";
    }

    /**
     * Metodo para gravar uma Permissao na base de dados.
     * Esta funcao envia os dados do Cadastro (Formulario) de uma permissao para o banco de dados.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param permissao - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/cadastrar")
    public String salvar(Model modelo, @Valid Rules permissao, BindingResult result, RedirectAttributes atributes) {
        logger.info("Recebida requisicao para gravar um cadastro...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            return "permissao/cadastrar" ;
        }
        if ( permissaoService.verificar(permissao.getRuleName()) ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Permissao já Existe! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Permissao já Existe!");
            return "permissao/cadastrar" ;
        }
        // Se nao houver erros, salva os dados:
        permissao.setRulesID(null);

        if ( permissaoService.gravar(permissao).getStatusCode().equals(HttpStatus.CREATED) ) {
            atributes.addFlashAttribute("mensagem", "Permissão salva com sucesso!");
            return "redirect:/permissao/listar/0";
        }
        atributes.addFlashAttribute("mensagem", "Permissão NAO cadastrada!");
        return "permissao/cadastrar" ;

    }
    
    /**
     * Apresenta o formulario para atualizar uma Permissao na base de dados.
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
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(id);
        if( permissaoSolicitada.isPresent() ) {
            Rules permissao = permissaoSolicitada.get();
            logger.info("APRESENTANDO os dados para alteracao: {}", permissao.getRuleName());
            modelo.addAttribute("rules", permissao);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/permissao/listar/0";
        }
        return "permissao/editar";
    }

    /**
     * Atualiza uma Permissao da base de dados
     *
     * @param permissao - objeto a ser persistido
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto de manipulacao da view pelo Spring
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar/")
    public String atualizar(@Valid Rules permissao, BindingResult result, RedirectAttributes atributes) {
        if (result.hasErrors()) {
            logger.info("ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "permissao/editar";
        }
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(permissao.getRulesID());
        if( permissaoSolicitada.isPresent() ) {
            Rules permissaoAntiga = permissaoSolicitada.get();
            logger.info("ALTERANDO  os dados do {}", permissaoAntiga.getRuleName());
            permissaoAntiga.setRuleName(permissao.getRuleName());
            permissaoService.atualizar(permissaoAntiga);
            atributes.addFlashAttribute("mensagem", "Alteração realizada com sucesso!");
        } else {
            logger.info("ALTERÇÃO NÃO REALIZADA - Recebeu uma Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "ALTERÇÃO NÃO REALIZADA - Referencia Invalida!");
        }
        logger.info("Solicitando a Listagem GERAL. ");
        return "redirect:/permissao/listar/0";
    }

    /**
     * DELETA uma Permissao da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param atributes - objeto de manipulacao da view pelo Spring
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletar( @PathVariable("id") long id, RedirectAttributes atributes ) {
        logger.info("Buscando PERMISSAO para DELETAR... " );
        Optional<Rules> permissaoSolicitada = permissaoService.buscar(id);
        if( permissaoSolicitada.isPresent() ) {
            Rules permissao = permissaoSolicitada.get();
            logger.info("DELETANDO permissao: {}", permissao.getRuleName());
            // remove a permissao dos usuarios:
            for ( Colaborador book : permissao.getColaboradores() ) {
                permissao.removeColaborador(book);
            }
            permissaoService.deletar(permissao);
            atributes.addFlashAttribute("mensagem", "Permissão EXCLUíDA do sistema!");
        } else {
            atributes.addFlashAttribute("mensagem", "Erro na Exclusão: Referencia Inválida!");
        }
        return "redirect:/permissao/listar/0";
    }

    /**
     * Apresenta o formulario de atribuicao de Permissao de um usuario no sistema.
     *
     * @param id - ID do usuario que se deseja atribuir uma permissao
     * @param modelo - Objeto Model para injetar dados na View
     * @param atributes - objeto contento atributos para a visualizacao HTML
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/atribuir/{id}")
    public String atribuir( @PathVariable("id") long id,Model modelo, RedirectAttributes atributes ) {
        logger.info("Recebida requisicao para atribuir permissoes ...");
        Optional user = colaboradorService.buscar(id);
        if( user.isPresent() ) {
            Colaborador colaborador    =  (Colaborador) user.get();
            modelo.addAttribute("permissoes",permissaoService.listar(true));
            modelo.addAttribute("colaborador", colaborador);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/colaborador/listar/0"; 
        }
        logger.info("Apresentando o formulario para ediçao ...");        
        return "colaborador/permissoes";
    }    
 
    /**
     * Atualiza uma Permissao da base de dados
     *
     * @param colaborador - objeto a ser atualizado
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto de manipulacao da view pelo Spring
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atribuir")
    public String atribuir( Colaborador colaborador, BindingResult result, RedirectAttributes atributes) {
        logger.info("Recebida requisicao para atualizar as permissoes de um usuario ...");
        Optional user = colaboradorService.buscar(colaborador.getColaboradorID());
        if( user.isPresent() ) {
            Colaborador usuario    =  (Colaborador) user.get();
            usuario.setVersao(colaborador.getVersao());
            usuario.setAtivo(colaborador.isAtivo());
            usuario.getPermissoes().clear();
            colaborador.getPermissoes().forEach( rule -> {
                Optional regra = permissaoService.buscar(rule.getRulesID());
                if( regra.isPresent() ) {
                    Rules permissao = (Rules) regra.get();
                    logger.info("Adicionando a permissao de {} ao usuario {}", permissao.getRuleName(), usuario.getNome() );
                    usuario.getPermissoes().add(permissao);
                } else {
                    logger.info("Uma permissao nao foi adicionada por nao ter sido encontrada no banco!" );
                }
            });
            colaboradorService.atualizar(usuario);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
        }
        atributes.addFlashAttribute("mensagem", "Situação do usuário atualizada no Banco de Dados!");
        return "redirect:/colaborador/listar/0"; 
    }
    
}
/*                    End of Class                                            */