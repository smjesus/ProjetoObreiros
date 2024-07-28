/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.servicos;

import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import br.com.aeroceti.obreiros.repositorios.InstituicaoRepository;

/**
 * Classe de SERVICOS para o objeto Instituicao (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;
    private final Logger logger = LoggerFactory.getLogger(InstituicaoService.class);

    /**
     * Listagem de TODAS as Instituicoes cadastradas no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList com os objetos cadastrados
     */
    public List<Instituicao> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todas as Instituicoes...");
        if (ordenarByNome) {
            logger.info("Executado Servico de listagem ordenada das instituicoes ...");
            return instituicaoRepository.findByOrderByNomeAsc();
        }
        logger.info("Executado Servico de listagem sem ordenacao das instituicoes ...");
        return instituicaoRepository.findAll();
    }

    /**
     * Verifica se um NOME de INSTITUICAO ja esta cadastrado no Banco de dados.
     *
     * @param  CNPJ - CNPJ para pesquisar no banco
     * @return Boolean - TRUE se o nome informado existe no banco; e FALSE caso contrario
     */
    public boolean verificar(String CNPJ) {
        CNPJ = CNPJ.replaceAll("\\D", "").trim();
        return ( instituicaoRepository.countByCnpj(CNPJ) > 0 );
    }

    /**
     * Busca uma INSTITUICAO pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Instituicao> buscar(Long identidade) {
        return instituicaoRepository.findByInstituicaoID(identidade);
    }
    
    /**
     * Busca uma INSTITUICAO pelo NOME cadastrado no Banco de dados.
     *
     * @param  nome - Nome para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Instituicao> buscar(String nome) {
        return instituicaoRepository.findByNome(nome);
    }
    
    /**
     * Busca uma INSTITUICAO pelo CNPJ cadastrado no Banco de dados.
     *
     * @param  CNPJ - CNPJ para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Instituicao encontrada (se houver)
     */
    public Optional<Instituicao> buscarCNPJ(String CNPJ) {
        CNPJ = CNPJ.replaceAll("\\D", "").trim();
        return instituicaoRepository.findByCnpj(CNPJ);
    }
    
    /**
     * Busca uma INSTITUICAO pela sua SIGLA cadastrada no Banco de dados.
     *
     * @param  siglaDaInstituicao - SIGLA para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Instituicao encontrada (se houver)
     */
    public Optional<Instituicao> buscarSIGLA(String siglaDaInstituicao) {
        return instituicaoRepository.findBySigla(siglaDaInstituicao);
    }
    
    /**
     * Metodo para gravar uma Instituicao na base de dados.
     *
     * @param instituicao - Objeto com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
     */
    public ResponseEntity<?> gravar(Instituicao instituicao) {
        logger.info("Persistindo Instituicao no banco de dados...");
        instituicaoRepository.save(instituicao);
        logger.info("Instituicao {} salva no banco de dados!", instituicao.getNome());
        return new ResponseEntity<>( instituicao, HttpStatus.CREATED);
    }
     
    /**
     * Metodo para atualizar uma Instituicao na base de dados.
     *
     * @param instituicao - Objeto com os dados a serem atualizados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
     */
    public ResponseEntity<?> atualizar(Instituicao instituicao) {
        // ATUALIZA o objeto do banco de dados
        logger.info("Instituicao {} atualizada no banco de dados!", instituicao.getNome());
        return new ResponseEntity<>(instituicaoRepository.save(instituicao), HttpStatus.OK);
    }

    /**
     * DELETA uma Instituicao do banco de dados.
     *
     * @param instituicao - Objeto a ser deletado
     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
     */
    public ResponseEntity<?> deletar(Instituicao instituicao) {
        logger.info("Excluindo Instituicao do banco ...");
        instituicaoRepository.delete(instituicao);
        MessageRequest mensagem = new MessageRequest("Instituicao DELETADA no Sistema!");
        logger.info("Requisicao executada: {}", mensagem.mensagem());
        return new ResponseEntity<>( mensagem, HttpStatus.OK);
    }
       
}
/*                    End of Class                                            */