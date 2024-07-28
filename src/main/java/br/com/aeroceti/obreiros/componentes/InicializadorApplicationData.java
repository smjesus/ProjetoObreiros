/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.componentes;

import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import br.com.aeroceti.obreiros.modelos.colaboradores.Rules;
import br.com.aeroceti.obreiros.repositorios.ColaboradorRepository;
import br.com.aeroceti.obreiros.repositorios.PermissoesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esta classe inicializa um usuario Administrador e a Permissao respectiva.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Component
public class InicializadorApplicationData implements CommandLineRunner {
    
    @Autowired
    private PermissoesRepository   rulesRepository;
    @Autowired
    private ColaboradorRepository  userRepository;
    
    private final Logger logger = LoggerFactory.getLogger(InicializadorApplicationData.class);
    /**
     *  Metodo principal que inicializa um usuario padrao.
     * 
     *  @param args - argumentos passados para a aplicacao
     */
    @Override
    public void run(String... args) throws Exception {
        Colaborador userAdmin = new Colaborador();
        Rules permissao = new Rules(null,"Administrador");
        int valor = rulesRepository.countByRuleName(permissao.getRuleName());
        if( valor == 0 ) {
            rulesRepository.save(permissao);
            logger.info("Sistema FleetCare Inicializando (Criada permissao de Admin) ... ");
        }
        // Verifica se há administrador cadastrado:
        Optional<Rules> permissaoSolicitada = rulesRepository.findByRuleName(permissao.getRuleName());
        if( permissaoSolicitada.isPresent() ) {
            permissao = permissaoSolicitada.get();
            if (permissao.getColaboradores().isEmpty()) {
                // Nao tem administrador, cadastrando um 'default':
                Optional userSolicitado = userRepository.findByCpf("53376207704");
                if (userSolicitado.isEmpty()) {
                    userAdmin.setAtivo(true);
                    userAdmin.setCpf("53376207704");
                    userAdmin.setDataNascimento("01/01/2024");
                    userAdmin.setNome("admin");
                    userAdmin.setSobrenome("Obreiro");
                    userAdmin.setEmail("admin@obreiros.com");
                    userAdmin.setPassword("admin-obreiro");
                    logger.info("Sistema Obreiros Inicializando (Criado Admin default) ... ");
                    userAdmin.addRules(permissao);
                    userRepository.save(userAdmin);
                } else {
                    userAdmin = (Colaborador) userSolicitado.get();
                }
                logger.info("Sistema Obreiros Inicializando (Admin default configurado)!");
            }
        }
        
        logger.info("Sistema Obreiros Inicializado. (Runner concluido)");
    }
    
}
/*                    End of Class                                            */