/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.servicos;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import br.com.aeroceti.obreiros.repositorios.PermissoesRepository;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;

/**
 * Classe de SERVICOS para o objeto Rules (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class PermissoesService {

    @Autowired
    private PermissoesRepository rulesRepository;
    private final Logger logger = LoggerFactory.getLogger(PermissoesService.class);

    /**
     * Listagem de TODOS as Permissoes cadastradas no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList em JSON com varios objetos USUARIO
     */
    public List<Rules> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todas as permissoes...");
        if (ordenarByNome) {
            logger.info("Executado Servico de listagem ordenada das permissoes ...");
            return rulesRepository.findByOrderByRuleNameAsc();
        }
        logger.info("Executado Servico de listagem sem ordenacao das permissoes ...");
        return this.rulesRepository.findAll();
    }

    /**
     * Verifica se um NOME de PERMISSAO ja esta cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return Boolean - TRUE se o nome informado existe no banco; e FALSE caso contrario
     */
    public boolean verificar(String nome) {
        return ( rulesRepository.countByRuleName(nome) > 0 );
    }

    /**
     * Busca uma PERMISSAO pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Rules> buscar(Long identidade) {
        return rulesRepository.findByRulesID(identidade);
    }
    
    /**
     * Busca uma PERMISSAO pelo NOME cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Rules> buscar(String nome) {
        return rulesRepository.findByRuleName(nome);
    }

    /**
     * Metodo para salvar uma Permissao na base de dados.
     *
     * @param permissao - Objeto Usuario com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> gravar(Rules permissao) {
        logger.info("Persistindo permissao no banco de dados ...");
        permissao.setRulesID(null);
        rulesRepository.save(permissao);
        logger.info("Permissao {} salva no banco de dados!", permissao.getRuleName());
        return new ResponseEntity<>( permissao, HttpStatus.CREATED);
    }

    /**
     * Metodo para atualizar uma Permissao na base de dados.
     *
     * @param permissao - Objeto Usuario com os dados a serem atualizados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> atualizar(Rules permissao) {
        // ATUALIZA o objeto do banco de dados
        logger.info("Permissao {} atualizada no banco de dados!", permissao.getRuleName());
        return new ResponseEntity<>(rulesRepository.save(permissao), HttpStatus.OK);
    }
    
    /**
     * DELETA uma permissao do banco de dados.
     *
     * @param permissao - Permissao a ser deletada
     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
     */
    public ResponseEntity<?> deletar(Rules permissao) {
        logger.info("Excluindo permissao do banco ...");
        rulesRepository.delete(permissao);
        MessageRequest mensagem = new MessageRequest("Permissao DELETADA no Sistema!");
        logger.info("Requisicao executada: {}", mensagem.mensagem());
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }
    
}
/*                    End of Class                                            */