/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.repositorios;

import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface para o Banco de Dados de INSTITUICAO.
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    // obtem uma lista de Instituicoes ORDENADA por nome
    List<Instituicao> findByOrderByNomeAsc();

    // obtem uma Instituicao atraves do nome
    Optional<Instituicao> findByNome(String chavePesquisa);

    // obtem uma Instituicao atraves do ID
    Optional<Instituicao> findByInstituicaoID(Long chavePesquisa);

    // obtem uma Instituicao pelo CNPJ
    public Optional<Instituicao> findByCnpj(String CNPJ);

    // obtem uma Instituicao pela sua SIGLA
    public Optional<Instituicao> findBySigla(String siglaDaInstituicao);

    // obtem o numero de Permissoes por ID
    int countByInstituicaoID(Long rulesID);
    
    // obtem o numero de Permissoes por NOME
    int countByNome(String nome);

    // obtem o numero de Permissoes por CNPJ
    int countByCnpj(String cnpj);

}
/*                    End of Class                                            */