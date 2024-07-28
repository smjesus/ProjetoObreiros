/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.componentes;

/**
 *  ENUM para selecao de Genero do Participante (Sexo)
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public enum Genero {
    
    N("Prefiro não informar"),
    M("Masculino"),
    F("Feminino");
    
    private final String displayValue;

    private Genero(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }    
    
}
/*                    End of Class                                            */