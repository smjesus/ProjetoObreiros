/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.repositorios;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;

/**
 * Interface para o Repositorio de Usuarios do Sistema (COLABORADOR).
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    // obtem um usuario atraves do cpf
    Optional findByCpf(String chavePesquisa);

    // obtem um usuario atraves do cpf
    Optional findByNome(String chavePesquisa);

    // obtem um usuario atraves do email
    Optional findByEmail(String chavePesquisa);

    // obtem um usuario atraves do ID
    Optional findByColaboradorID(Long chavePesquisa);
   
    // obtem um usuario atraves do ID
    Optional findByVerificationCode(String chavePesquisa);

    // obtem uma lista de usuarios ORDENADA por nome
    List<Colaborador> findByOrderByNomeAsc();

    // obtem uma lista de usuarios ORDENADA por email
    List<Colaborador> findByOrderByEmailAsc();

    // obtem uma lista de usuarios ORDENADA por email
    List<Colaborador> findByInstituicao(Instituicao instituicao);

    // obtem o numero de usuarios por EMAIL
    int countByEmail(String email);

    // obtem o numero de usuarios NOME
    int countByCpf(String numeroCPF);

}
/*                    End of Class                                            */