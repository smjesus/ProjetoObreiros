/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.instituicoes;

import br.com.aeroceti.obreiros.componentes.StringTools;
import br.com.aeroceti.obreiros.modelos.colaboradores.Colaborador;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *  Objeto base Instituicao.
 * Esta classe representa uma Instituicao Espirita ou Grupo de Estudos.
 *
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Instituicao")
public class Instituicao implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @Basic(optional = false)
    @Column(name = "instituicaoID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long instituicaoID;
    
    @NotNull
    @NotBlank
    @Size(min=5, max=50, message = "Nome precisa ter no mínimo 5 caracteres")
    @Schema(example = "Centro Espírita Casa do Caminho", description = "Nome da Instituição ou do Grupo")
    @Column(name = "nome",             length = 50)
    private String nome                = "";
    
    @Column(name = "CNPJ",             length = 20)
    @Schema(example = "111.222.333/0001-99", description = "CNPJ Válido da Instituição ou Grupo (Esta propriedade armazena o CNPJ limpo, sem formatação)")
    private String cnpj                = "";
    
    @Column(name = "sigla",            length = 20)
    @Schema(example = "CECC", description = "Sigla da Instituição como é conhecida")
    private String sigla               = "";    
    
    @Column(name = "telefone",         length = 20)
    @Schema(example = "99 9.9999-8888", description = "Telefone de Contato da Instituição (Esta propriedade armazena o TELEFONE limpo, sem formatação)")
    private String telefone            = "";
    
    @Column(name = "enderecoEletronico", length = 120)
    @Schema(example = "caminho@gmail.com", description = "Endereço Eletronico (e-mail) da Instituição")
    private String enderecoEletronico  = "";
    
    @Column(name = "googleMaps",       length = 200)
    @Schema(example = "https://maps.app.goo.gl/hyLVnBmo9QYuA9uw7", description = "URL do Google Maps para a Localização da Instituição")
    private String googleMaps          = "";
    
    @Version
    @Column(name = "versao")
    @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
    private Long versao;    
    
    //  Uma Instituicao possue varios Colaboradores
    @Schema(hidden = true)
    @JsonBackReference("instituicao")
    @OneToMany(mappedBy = "instituicao", fetch = FetchType.LAZY)
    private Collection<Colaborador> colaboradores;

    public Instituicao() {
    }

    public Instituicao(Long instituicaoID) {
        this.instituicaoID = instituicaoID;
    }

    public Long getInstituicaoID() {
        return instituicaoID;
    }

    public void setInstituicaoID(Long instituicaoID) {
        this.instituicaoID = instituicaoID;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public void setSigla(String sigla) {
        try {
            this.sigla = sigla.trim().toUpperCase();
        } catch (Exception e) {
            this.sigla = "";
        }   
    }

    public String getSigla() {
        return sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = StringTools.formatName(nome);
    }
    
    public String getCnpj() {
        return cnpj;
    }

    @Schema(hidden = true)
    public String getCnpjFormatado() {
        return StringTools.getCnpjFormatado(cnpj);
    }

    public void setCnpj(String cnpj) {
        this.cnpj = StringTools.setCnpjLimpo(cnpj);
    }

    public void setEnderecoEletronico(String enderecoEletronico) {
        this.enderecoEletronico = enderecoEletronico.trim().toLowerCase();
    }

    public String getEnderecoEletronico() {
        return enderecoEletronico;
    }

    public void setGoogleMaps(String googleMaps) {
        this.googleMaps = googleMaps;
    }

    public String getGoogleMaps() {
        return googleMaps;
    }

    public String getTelefone() {
        return StringTools.getTelefoneFormatado(this.telefone) ;
    }

    public void setTelefone(String telefone) {
        this.telefone = StringTools.getTelefoneLimpo(telefone);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Collection<Colaborador> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(Collection<Colaborador> colaboradorCollection) {
        this.colaboradores = colaboradorCollection;
    }

    public void addColaborador(Colaborador user) {
        this.colaboradores.add(user);
        user.setInstituicao(this);
    }
    
    public void removeColaborador(Colaborador user) {
        this.colaboradores.remove(user);
        user.setInstituicao(null) ;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (instituicaoID != null ? instituicaoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Instituicao)) {
            return false;
        }
        Instituicao other = (Instituicao) object;
        return !((this.instituicaoID == null && other.instituicaoID != null) || (this.instituicaoID != null && !this.instituicaoID.equals(other.instituicaoID)));
    }

    @Override
    public String toString() {
        return "Instituicao[instituicaoID=" + instituicaoID + "]: " + this.sigla;
    }

}
/*                    End of Class                                            */