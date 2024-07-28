/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.componentes;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Esta classe fornece funcoes para tratamento de Strings.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
public class StringTools {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /*
     * Esta funcao gera uma String Aleatoria para servir de Codigo de Verificacao
     */
    public static String generateRandomString(int length){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++){
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    
    /*
     * Esta funcao formata um nome com as iniciais maiusculas e demais minusculas
     */
    public static String formatName(String nome) {
        try {
            String[] partes = nome.split(" ");
            StringBuilder sb = new StringBuilder();
            String [] codes = {"da", "de", "do", "di", "dos", "das", "e", "d'"};
            for (int i = 0; i < partes.length; i++) {
                String parte = partes[i];
                if( !parte.trim().equals("") ) {
                    if( i == 0 ) {
                        parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                    } else  {
                        if(Arrays.asList(codes).contains( parte.toLowerCase().trim()) ) {
                            parte = parte.toLowerCase();
                        }  else  {
                            parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                        }
                    }
                    sb.append(" ").append(parte.trim());
                }
            }
            nome = sb.toString().trim();
        } catch(Exception nameError) {
            nome = "" ;
        }
        return nome;
    }

    /*
     * Esta funcao formata um CNPJ para aparesentar ao usuario
     */
    public static String getCnpjFormatado(String cnpj_clean) {
        String valorFormatado;
        try {
            valorFormatado =  cnpj_clean.replaceAll( "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})", "$1.$2.$3/$4-$5" ).trim();
        } catch (Exception e) {
            valorFormatado = "";
        }
        return valorFormatado;
    }

    /*
     * Esta funcao retira a formatacao de um CNPJ para inserir no banco de dados
     */
    public static String setCnpjLimpo(String cnpj) {
        if( cnpj.isBlank() || cnpj.isEmpty() || cnpj.equalsIgnoreCase("000000000000000") ) {
            cnpj = "";
        } else {
            try {
                String complemento = "";
                int diferenca = 15 - cnpj.trim().length();
                for (int i=0; i<diferenca;i++) {
                    complemento = complemento + "0";
                }
                complemento = complemento + cnpj ;
                cnpj = complemento.replaceAll("\\D", "").trim();
            } catch (Exception e) {
                cnpj = "";
            }
        }
        return cnpj;
    }

    /*
     * Esta funcao retira a formatacao de um TELEFONE para inserir no banco de dados
     */
    public static String getTelefoneLimpo(String fone) {
        String novoValor;
        try {
            novoValor = fone.trim();
            //  Limpa o telefone das formatacoes:
            novoValor = novoValor.replaceAll("\\D", "");
            // corrige o problema da formatacao do Bootstrap:
            if( novoValor.trim().length() == 1 ) {
                novoValor = "";
            }
        } catch(NullPointerException nulo) {
            novoValor = "";
        }
        return novoValor;
    }

    /*
     * Esta funcao apresenta um TELEFONE formatado para visualizacao
     */
    public static String getTelefoneFormatado(String fone) {
        if ( fone.trim().length() == 10 ) {
            // Formata no padrao (99)4444-2222 (fixo)
            return fone.trim().replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1)$2-$3");
        } else {
            // Formata no padrao (99)9.8888-7777  (Celular)
            return fone.trim().replaceFirst("(\\d{2})(\\d{1})(\\d{4})(\\d+)", "($1)$2.$3-$4");
        }
    }

}
/*                    End of Class                                            */