/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.servicos;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de SERVICOS para Validar uma senha dos participantes (Logica do negocio).
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Service
public class PasswordService {
    
    private final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    /**
     * Metodo principal para validar uma senha.
     * 
     * @param   pass - senha recebida para validacao
     * @return  LIST - lista com as mensagens de erro
     */
    public List<String> validate(String pass) {
        List<String> failures = new ArrayList<>();
        logger.info("Executado Servico de validacao de senha ...");
        validateLength(pass, failures);
        validateUppercase(pass, failures);
        validateLowercase(pass, failures);
        validateNumber(pass, failures);
        validateSpecialChars(pass, failures);
        logger.info("Senha {} verificada em 5 parametros; enviando resposta ...", pass);
        return failures;
    }

    private static void validateLength(String pass, List<String> failures) {
        if (StringUtils.isBlank(pass) || pass.length() < 8) {
            failures.add("A senha deve possuir pelo menos 08 caracteres.");
        }
    }

    private static void validateUppercase(String pass, List<String> failures) {
        if (!Pattern.matches(".*[A-Z].*", pass)) {
            failures.add("A senha deve possuir pelo menos uma letra maiúscula.");
        }
    }

    private static void validateLowercase(String pass, List<String> failures) {
        if (!Pattern.matches(".*[a-z].*", pass)) {
            failures.add("A senha deve possuir pelo menos uma letra minúscula.");
        }
    }

    private static void validateNumber(String pass, List<String> failures) {
        if (!Pattern.matches(".*[0-9].*", pass)) {
            failures.add("A senha deve possuir pelo menos um numero.");
        }
    }

    private static void validateSpecialChars(String pass, List<String> failures) {
        if (!Pattern.matches(".*[\\W].*", pass)) {
            failures.add("A senha deve possuir pelo menos um caractere especial.");
        }
    }
    
}
/*                    End of Class                                            */