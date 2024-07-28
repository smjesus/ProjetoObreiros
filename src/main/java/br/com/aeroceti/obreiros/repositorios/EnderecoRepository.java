/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.repositorios;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.aeroceti.obreiros.modelos.colaboradores.Endereco;
import java.util.Optional;

/**
 * Interface para o Repositorio de Endereco dos Colaboradores.
 *
 * Esta classe abstrai diversos metodos de persistencia do JPA.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {   

    public Optional<Endereco> findByEnderecoID(Long identidade);

}
/*                    End of Class                                            */