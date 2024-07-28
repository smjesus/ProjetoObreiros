/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.repositorios;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;

/**
 * Interface para o Repositorio de Rules (Permissoes).
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface PermissoesRepository extends JpaRepository<Rules, Long> {

    // obtem uma lista de Permissoes ORDENADA por nome
    List<Rules> findByOrderByRuleNameAsc();

    // obtem uma Permissao atraves do nome
    Optional<Rules> findByRuleName(String chavePesquisa);

    // obtem uma Permissao atraves do ID
    Optional<Rules> findByRulesID(Long chavePesquisa);

    // obtem o numero de Permissoes por ID
    int countByRulesID(Long rulesID);
    
    // obtem o numero de Permissoes por nome
    int countByRuleName(String ruleName);    

}
/*                    End of Class                                            */