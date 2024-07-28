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
import br.com.aeroceti.obreiros.modelos.colaboradores.Endereco;
import br.com.aeroceti.obreiros.modelos.dto.MessageRequest;
import br.com.aeroceti.obreiros.repositorios.EnderecoRepository;

/**
 * Classe de SERVICOS para o objeto Endereco (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;
    private final Logger logger = LoggerFactory.getLogger(EnderecoService.class);

    /**
     * Listagem de TODOS as Permissoes cadastradas no Banco de dados.
     *
     * @return ArrayList em JSON com varios objetos USUARIO
     */
    public List<Endereco> listar() {
        logger.info("Obtendo uma listagem de todos os enderecos ...");
        logger.info("Executado Servico de listagem dos enderecos ...");
        return this.enderecoRepository.findAll();
    }

    /**
     * Busca um ENDERECO pelo ID cadastrado no Banco de dados.
     *
     * @param  identidade - ID do objeto desejado do banco de dados
     * @return OPTIONAL   - Objeto Optional contendo a Permissao encontrada (se houver)
     */
    public Optional<Endereco> buscar(Long identidade) {
        return enderecoRepository.findByEnderecoID(identidade);
    }
    
    /**
     * Metodo para salvar um Endereco na base de dados.
     *
     * @param endereco - Objeto com os dados a serem gravados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Endereco cadastrado
     */
    public ResponseEntity<?> gravar(Endereco endereco) {
        logger.info("Persistindo permissao no banco de dados ...");
        endereco.setEnderecoID(null);
        enderecoRepository.save(endereco);
        return new ResponseEntity<>( endereco, HttpStatus.CREATED);
    }

    /**
     * Metodo para atualizar um Endereco na base de dados.
     *
     * @param endereco - Objeto com os dados a serem atualizados
     * @return ResponseEntity contendo uma mensagem de erro OU um objeto Usuario cadastrado
     */
    public ResponseEntity<?> atualizar(Endereco endereco) {
        // ATUALIZA o objeto do banco de dados
        logger.info("Endereco atualizado no banco de dados!");
        return new ResponseEntity<>(enderecoRepository.save(endereco), HttpStatus.OK);
    }
    
    /**
     * DELETA uma permissao do banco de dados.
     *
     * @param endereco - Objeto a ser deletado
     * @return ResponseEntity - Mensagem de Erro ou Sucesso na operacao
     */
    public ResponseEntity<?> deletar(Endereco endereco) {
        logger.info("Excluindo endereco do banco ...");
        enderecoRepository.delete(endereco);
        MessageRequest mensagem = new MessageRequest("Endereço DELETADO no Sistema!");
        logger.info("Requisicao executada: {}", mensagem.mensagem());
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }
    
}
/*                    End of Class                                            */