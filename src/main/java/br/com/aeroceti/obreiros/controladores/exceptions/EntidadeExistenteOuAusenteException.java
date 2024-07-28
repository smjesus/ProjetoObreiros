/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.controladores.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * Classe de Manipulacao de Exceptions para Erro de Objeto já cadastrado no Banco de Dados.
 *
 * Esta classe obedece ao padrao especificado na RFC7807
 * https://datatracker.ietf.org/doc/html/rfc7807
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public class EntidadeExistenteOuAusenteException extends ObreirosException {

    private final String detalhes;
    private final String titulo;
    private final Logger logger = LoggerFactory.getLogger(EntidadeExistenteOuAusenteException.class);

    public EntidadeExistenteOuAusenteException(String detalhe, String Titulo) {
        this.detalhes = detalhe;
        this.titulo   = Titulo;
    }
    
    @Override
    public ProblemDetail toProblemDetail() {
        logger.info("Exception Gerada! Retornando BAD Request: {}", this.detalhes);
        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle(titulo);
        pb.setDetail(detalhes);
        return pb;
    }

}
/*                    End of Class                                            */