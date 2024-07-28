/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * Classe de Manipulacao de Exceptions do sistema.
 *
 * Esta classe obedece ao padrao especificado na RFC7807
 * https://datatracker.ietf.org/doc/html/rfc7807
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public class ObreirosException extends RuntimeException {
    
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("Obreiros Internal Server Error!");
        return pb;
    }
}
/*                    End of Class                                            */