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
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import br.com.aeroceti.obreiros.repositorios.ColaboradorRepository;

/**
 * Classe de SERVICOS para o objeto Colaborador (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class ColaboradorService {
    
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    private final Logger logger = LoggerFactory.getLogger(ColaboradorService.class);

    /**
     * Listagem de TODOS os Colaboradores cadastrados no Banco de dados.
     *
     * @param ordenarByNome - Boolean para indicar se ordena por nome o resultado da pesquisa
     * @return ArrayList com os objetos cadastrados
     */
    public List<Colaborador> listar(boolean ordenarByNome) {
        logger.info("Obtendo uma listagem de todas os Colaboradores...");        
        if (ordenarByNome) {
            logger.info("Executado Servico de listagem ordenada dos colaboradores ...");
            return colaboradorRepository.findByOrderByNomeAsc();
        }
        logger.info("Executado Servico de listagem sem ordenacao dos colaboradores ...");
        return colaboradorRepository.findAll();
    }

    /**
     * Verifica se um CPF de USUARIO ja esta cadastrado no Banco de dados.
     *
     * @param  cpf - Numero do CPF para pesquisar no banco
     * @return Boolean - TRUE se o nome informado existe no banco; e FALSE caso contrario
     */
    public boolean verificar(String cpf) {
        cpf = cpf.replaceAll("\\D", "").trim();
        return ( colaboradorRepository.countByCpf(cpf) > 0 );
    }

    /**
     * Busca um USUARIO pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Colaborador> buscar(Long identidade) {
        return colaboradorRepository.findByColaboradorID(identidade);
    }

    /**
     * Busca uma USUARIO pelo NOME cadastrado no Banco de dados.
     *
     * @param  nome - Nome do usuario para pesquisar no banco
     * @return OPTIONAL - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Colaborador> buscar(String nome) {
        return colaboradorRepository.findByNome(nome);
    }
    
    /**
     * Busca uma USUARIO pelo CPF cadastrado no Banco de dados.
     *
     * @param  cpf - Numero do CPF para pesquisar no banco
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Colaborador> buscarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "").trim();
        return colaboradorRepository.findByCpf(cpf);
    }
    
    /**
     * Metodo para gravar um Colaborador na base de dados.
     *
     * @param colaborador - Objeto com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
     */
    public ResponseEntity<?> gravar(Colaborador colaborador) {
        logger.info("Persistindo Colaborador no banco de dados...");
        colaboradorRepository.save(colaborador);
        logger.info("Colaborador {} enviado para o banco de dados!", colaborador.getNome());
        return new ResponseEntity<>(colaborador, HttpStatus.CREATED);
    }
     
    /**
     * Metodo para atualizar um Colaborador na base de dados.
     *
     * @param colaborador - Objeto com os dados a serem atualizados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto cadastrado
     */
    public ResponseEntity<?> atualizar(Colaborador colaborador) {
        // ATUALIZA o objeto do banco de dados
        logger.info("Colaborador {} atualizado no banco de dados!", colaborador.getNome());
        return new ResponseEntity<>(colaboradorRepository.save(colaborador), HttpStatus.OK);
    }

    /**
     * LISTAGEM dos Colaboradores que participam de uma Instituicao
     *
     * @param casaEspirita - Instituicao que sera pesquisada
     * @return Listagem de colaboradoes que participam da Instituicao
     */
    public List<Colaborador> participantes(Instituicao casaEspirita) {
        logger.info("Retornando uma listagem com todos os Participantes ...");        
        return colaboradorRepository.findByInstituicao(casaEspirita);
    }
    
    /**
     * DELETA um Colaborador do banco de dados.
     *
     * @param colaborador - Objeto a ser deletado
     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
     */
    public ResponseEntity<?> deletar(Colaborador colaborador) {
        logger.info("Excluindo Colaborador do banco ...");
        colaboradorRepository.delete(colaborador);
        MessageRequest mensagem = new MessageRequest("Colaborador DELETADO do Sistema!");
        logger.info("Requisicao executada: {}", mensagem.mensagem());
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

}
/*                    End of Class                                            */