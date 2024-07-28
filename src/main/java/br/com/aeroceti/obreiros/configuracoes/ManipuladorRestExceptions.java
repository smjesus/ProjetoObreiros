/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.configuracoes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.aeroceti.obreiros.controladores.exceptions.ObreirosException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Classe Controller Advice para manipular as excessões no sistema.
 *
 * Esta classe obedece ao padrao especificado na RFC7807
 * https://datatracker.ietf.org/doc/html/rfc7807
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@RestControllerAdvice
public class ManipuladorRestExceptions {
    
    private final Logger logger = LoggerFactory.getLogger(ManipuladorRestExceptions.class);
    
    @ExceptionHandler(ObreirosException.class)
    public ProblemDetail handleObreirosExceptions(ObreirosException ex) {
        return ex.toProblemDetail();
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handleNullPointerException(NullPointerException npe) {
        logger.info("Exception Gerada: {} - Retornando BAD Request!", npe.getMessage());
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Objeto NULO recebido na requisição");
        pd.setDetail(npe.getLocalizedMessage());
        
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        logger.info("Exception Gerada: {} - Retornando BAD Request!", ex.getMessage());
        var fieldErrors = ex.getFieldErrors()
                .stream()
                .map(field -> new InvalidParam(field.getField(), field.getDefaultMessage()))
                .toList();
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Houve erro na validacao dos parametros enviados!");
        pd.setProperty("invalid-params", fieldErrors);
        
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationExceptions(ConstraintViolationException ex) {
        logger.info("Exception Gerada na PERSISTENCIA do objeto no Banco de Dados - Retornando BAD Request!");
        var fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(erro -> new InvalidParam(erro.getPropertyPath().toString(), erro.getMessageTemplate()))
                .toList();
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Erro na Validacao das propriedades do Objeto a ser persistico no banco.");
        pd.setProperty("invalid-params", fieldErrors);
        
        return pd;
    }
        
    private record InvalidParam(String fieldName, String reasonError){
    }
        
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {
        logger.info("Exception Gerada: {} - Retornando BAD Request!", e.getMessage());
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Estado Inconsistente da Entidade ");
        pd.setDetail(e.getLocalizedMessage());
        
        return pd;
    }
        
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.info("Exception Gerada: {} - Retornando BAD Request!", e.getMessage());
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Estado Inconsistente da Entidade ");
        pd.setDetail(e.getLocalizedMessage());
        
        return pd;
    }
    
}
/*                    End of Class                                            */