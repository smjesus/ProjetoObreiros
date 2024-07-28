/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.v1.web;

import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
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
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import br.com.aeroceti.obreiros.servicos.ColaboradorService;
import br.com.aeroceti.obreiros.servicos.InstituicaoService;

/**
 * Classe Controller MVC para o objeto INSTITUICAO (Casas Espiritas).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/instituicao/")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;
    private final ColaboradorService colaboradorService;
    private final Logger logger = LoggerFactory.getLogger(InstituicaoController.class);

    public InstituicaoController(InstituicaoService casaServices, ColaboradorService cs) {
        this.instituicaoService = casaServices;
        this.colaboradorService = cs;
    }

    /**
     * Listagem de TODAS as Instituicoes cadastradas no Banco de dados.
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
        logger.info("Recebida requisicao para listar todas as instituicoes ...");
        modelo.addAttribute("instituicoes",instituicaoService.listar(ordenar));
        return "instituicao/listagem";
    }
    
    /**
     * Formulario de cadastro de Instituicao na base de dados.
     * Esta funcao abre a Pagina de Cadastro (Formulario) de uma casa espirita.
     *
     * @param modelo - Objeto Model para injetar dados na View
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/cadastrar")
    public String cadastrar(Model modelo) {
        modelo.addAttribute("instituicao", new Instituicao());
        logger.info("Recebida requisicao para cadastro:  encaminhando ao formulario ...");
        return "instituicao/cadastrar";
    }

    /**
     * Metodo para gravar uma Instituicao na base de dados.
     * Esta funcao envia os dados do Cadastro (Formulario) de uma casaEspirita para o banco de dados.
     *
     * @param modelo - Objeto Model para injetar dados na View
     * @param instituicao - Objeto a ser persistido no banco
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto contento atributos para a visualizacao HTML
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/cadastrar")
    public String salvar(Model modelo, @Valid Instituicao instituicao, BindingResult result, RedirectAttributes atributes) {
        logger.info("Recebida requisicao para gravar um cadastro...");
        // Se houver erros retorna ao formulario:
        if( result.hasErrors() ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Erro na Validacao! ");
            return "instituicao/cadastrar" ;
        }
        if ( !instituicao.getCnpj().isBlank() && instituicaoService.verificar(instituicao.getCnpj().trim()) ) {
            logger.info("GRAVAÇAO NÃO REALIZADA - Instituicao já Existe! ");
            modelo.addAttribute("mensagem", "GRAVAÇAO NÃO REALIZADA - Instituicao já Existe!");
            return "instituicao/cadastrar" ;
        }
        // Se nao houver erros, salva os dados:
        instituicao.setInstituicaoID(null);
        if ( instituicaoService.gravar(instituicao).getStatusCode().equals(HttpStatus.CREATED) ) {
            atributes.addFlashAttribute("mensagem", "Instituicao salva com sucesso!");
            return "redirect:/instituicao/listar/0";
        }
        atributes.addFlashAttribute("mensagem", "Instituicao NAO cadastrada!");
        return "instituicao/cadastrar" ;
    }
    
    /**
     * Apresenta o formulario para atualizar uma Instituicao na base de dados.
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
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(id);
        if( instituicaoSolicitada.isPresent() ) {
            Instituicao casaEspirita = instituicaoSolicitada.get();
            logger.info("APRESENTANDO os dados para alteracao: " + casaEspirita.getNome());
            modelo.addAttribute("instituicao", casaEspirita);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/instituicao/listar/0";
        }
        return "instituicao/editar";
    }

    /**
     * Atualiza uma Instituicao da base de dados.
     * 
     * @param instituicao - objeto a ser persistido
     * @param result  - objeto do contexto HTTP
     * @param atributes - objeto de manipulacao da view pelo Spring
     *
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atualizar")
    public String atualizar(@Valid Instituicao instituicao, BindingResult result, RedirectAttributes atributes ) {
        if (result.hasErrors()) {
            logger.info("ALTERÇÃO NÃO REALIZADA - Erro na validação! ");
            return "instituicao/editar";
        }
        instituicaoService.atualizar(instituicao);
        logger.info("Solicitando a Listagem GERAL. ");
        atributes.addFlashAttribute("mensagem", "ATUALIZAÇÃO realizada com sucesso!");
        return "redirect:/instituicao/listar/0";
    }

    /**
     * DELETA uma Instituicao da base de dados.
     * 
     * @param id - ID do objeto a ser REMOVIDO DO BANCO
     * @param atributes - objeto de manipulacao da view pelo Spring
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/remover/{id}")
    public String deletar( @PathVariable("id") long id, RedirectAttributes atributes) {
        logger.info("Buscando INSTITUICAO para DELETAR... " );
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(id);
        if( instituicaoSolicitada.isPresent() ) {
            Instituicao casaEspirita = instituicaoSolicitada.get();
            logger.info("DELETANDO Instituicao: {}", casaEspirita.getNome());
            // remove a casaEspirita dos usuarios:
            for ( Colaborador book : casaEspirita.getColaboradores() ) {
                casaEspirita.removeColaborador(book);
            }
            instituicaoService.deletar(casaEspirita);
            atributes.addFlashAttribute("mensagem", "Instituição EXCLUíDA do sistema!");
        } else {
            atributes.addFlashAttribute("mensagem", "Erro na Exclusão: Referencia Inválida!");
        }
        return "redirect:/instituicao/listar/0";
    }
        
    /**
     * Apresenta o formulario de atribuicao da Instituicao que um usuario frequenta.
     *
     * @param id - ID do usuario que se deseja atribuir a instituicao
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
            modelo.addAttribute("instituicoes", instituicaoService.listar(true));
            modelo.addAttribute("colaborador" , colaborador);
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida!");
            return "redirect:/colaborador/listar/0"; 
        }
        logger.info("Apresentando o formulario para ediçao ...");        
        return "colaborador/instituicao";
    }    

    /**
     * Grava a INSTITUICAO que um Colaborador frequenta.
     * 
     * @param usuario - objeto a ser atualizado na base de dados
     * @param atributes - objeto de manipulacao da view pelo Spring
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @PostMapping("/atribuir")
    public String atribuir( Colaborador usuario, RedirectAttributes atributes  ) {
        logger.info("Recebida requisicao para gravar a instituicao em que um usuario participa ...");
        Optional user = colaboradorService.buscar(usuario.getColaboradorID());
        if( user.isPresent() ) {
            Colaborador colaborador    =  (Colaborador) user.get();
            Instituicao casaEspiritaInformada = usuario.getInstituicao();
            Optional centroEspirita = instituicaoService.buscar(casaEspiritaInformada.getInstituicaoID());
            if( centroEspirita.isPresent() ) {
                Instituicao instituicao = (Instituicao) centroEspirita.get();
                colaborador.setInstituicao(instituicao);
                colaboradorService.atualizar(colaborador);
            } else {
                logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
                atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida a uma Instituicao!");
                return "redirect:/colaborador/listar/0"; 
            }
        } else {
            logger.info("SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida! ");
            atributes.addFlashAttribute("mensagem", "SOLICITAÇÃO NÃO REALIZADA - Referencia Invalida a um Colaborador!");
            return "redirect:/colaborador/listar/0"; 
        }
        logger.info("Dados do usuário atualizado com sucesso!");        
        return "redirect:/colaborador/listar/0";
    }

    /**
     * Listagem dos Colaboradores que frequentam uma INSTITUICAO
     * 
     * @param id - ID da Instituicao
     * @param modelo - Objeto Model para injetar dados na View
     * @param atributes - objeto contento atributos para a visualizacao HTML
     * 
     * @return String Padrao Spring para redirecionar a uma pagina
     */
    @GetMapping("/participantes/{id}")
    public String participantes( @PathVariable("id") long id, Model modelo, RedirectAttributes atributes) {
        logger.info("Recebida requisicao para listar os participantes de uma instituicao ...");
        Optional<Instituicao> instituicaoSolicitada = instituicaoService.buscar(id);
        if( instituicaoSolicitada.isPresent() ) {
            Instituicao casaEspirita = instituicaoSolicitada.get();
            logger.info("Obtendo os Participantes da Instituicao: {}", casaEspirita.getSigla());
            modelo.addAttribute("siglaDaInstituicao",casaEspirita.getSigla());
            modelo.addAttribute("usuarios",colaboradorService.participantes(casaEspirita));

        } else {
            atributes.addFlashAttribute("mensagem", "Erro na busca da Instituicao: Referencia Inválida!");
        }
        return "instituicao/participantes";
    }


}
/*                    End of Class                                            */