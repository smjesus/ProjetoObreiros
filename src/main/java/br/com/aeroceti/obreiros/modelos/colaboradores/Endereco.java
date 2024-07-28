/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.colaboradores;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;

/**
 *  Objeto base Endereco.
 *
 * Esta classe representa um endereco para um colaborador ou Instituicao no movimento espirita.
 * 
 * Copyright (c) 2016 AeroCETI   -  Todos os direitos reservados.
 *                http://www.aeroceti.com
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Endereco")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 4L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enderecoID", nullable = false)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long enderecoID;
    
    @Column(name = "CEP",        length = 45)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private String cep        = "";
    @Column(name = "logradouro", length = 45)
    @Schema(example = "Rua A", description = "Nome da Rua, Estrada ou Avenida da Residência do Usuário")
    private String logradouro = "";
    @Column(name = "numero",     length = 45)
    @Schema(example = "10", description = "Número da Residência do Usuário")
    private String numero     = "";
    @Column(name = "complemento",length = 45)
    @Schema(example = "Conjunto Tiradentes II", description = "Complemento da Residência do Usuário")
    private String complemento     = "";
    @Column(name = "bairro",     length = 45)
    @Schema(example = "Bairro de Flores", description = "Bairro da Residência do Usuário")
    private String bairro     = "";
    @Column(name = "localidade", length = 45)
    @Schema(example = "Manaus", description = "Municipio onde se localiza a Residência do Usuário")
    private String localidade = "";
    @Column(name = "estado",     length = 45)
    @Schema(example = "AM", description = "Unidade da Federação que se encontra a Residência do Usuário")
    private String estado     = "";
    @Column(name = "ibge",       length = 45)
    @Schema(example = "0350", description = "Código do IBGE para a localização da Residência do Usuário")
    private String ibge       = "";

    @Version
    @Column(name = "versao")
    @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
    private Long versao;    

    public Endereco() {
    }

    public Endereco(Long enderecoID) {
        this.enderecoID = enderecoID;
    }

    public Long getEnderecoID() {
        return enderecoID;
    }

    public void setEnderecoID(Long enderecoID) {
        this.enderecoID = enderecoID;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enderecoID != null ? enderecoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Endereco)) {
            return false;
        }
        Endereco other = (Endereco) object;
        return !((this.enderecoID == null && other.enderecoID != null) || (this.enderecoID != null && !this.enderecoID.equals(other.enderecoID)));
    }

    @Override
    public String toString() {
        return "Endereco[ enderecoID=" + enderecoID + " ]";
    }
    
}
/*                    End of Class                                            */