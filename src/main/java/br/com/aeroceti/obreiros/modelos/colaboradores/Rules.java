/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.colaboradores;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *  Objeto base Rules (Niveis de usuarios).
 * Esta classe representa um nivel de um usuario no sistema.
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Rules")
public class Rules implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "rulesID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long rulesID;
    
    @NotNull
    @NotBlank
    @Size(min=5, max=50, message = "precisa ter no mínimo 5 caracteres")
    @Schema(example = "Administrador", description = "Nome da Permissão/Nivel para os úsuarios")
    @Column(name = "ruleName",        length = 50)
    private String ruleName;
    
    @Version
    @Column(name = "versao")
    @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
    private Long versao;

    @JsonBackReference("permissoes")
    @ManyToMany(mappedBy = "permissoes", fetch = FetchType.EAGER)
    @Schema(hidden = true)
    private Set<Colaborador> colaboradores = new HashSet<>();
    
    public Rules() {
    }

    public Rules(Long rID) {
        this.rulesID = rID;
    }

    public Rules(Long rulesID, String ruleName) {
        this.rulesID = rulesID;
        this.ruleName = ruleName;
    }

    public Rules(Long rolesID, String roleName, Long versao) {
        this.rulesID = rolesID;
        this.ruleName = roleName;
        this.versao = versao;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getRulesID() {
        return rulesID;
    }

    public void setRulesID(Long rolesID) {
        this.rulesID = rolesID;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String roleName) {
        roleName = StringUtils.capitalize(roleName);
        this.ruleName = roleName;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    public Set<Colaborador> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(Set<Colaborador> usuarios) {
        this.colaboradores = usuarios;
    }    

    public void addColaborador(Colaborador user) {
        this.colaboradores.add(user);
        user.getPermissoes().add(this);
    }
    
    public void removeColaborador(Colaborador user) {
        this.colaboradores.remove(user);
        user.getPermissoes().remove(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rules)) {
            return false;
        }
        Rules other = (Rules) obj;
        return !((this.getRulesID()== null && other.getRulesID() != null) || (this.getRulesID() != null && !this.rulesID.equals(other.rulesID) ) );
    }

    @Override
    public String toString() {
        return getRuleName() + "[ id=" + getRulesID() + "] ";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRulesID() != null ? getRulesID().hashCode() : 0);
        return hash;
    }

}
/*                    End of Class                                            */