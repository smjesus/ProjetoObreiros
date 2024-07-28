/**
 * Projeto:  Obreiros do Cristo  -  Sistema de Cadastro para trabalhadores espíritas.
 * Gerente:  Sergio Murilo  -  smurilo at GMail
 * Data:     Manaus/AM  -  2024
 * Equipe:   Murilo, Victor, Allan
 */
package br.com.aeroceti.obreiros.modelos.colaboradores;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import br.com.aeroceti.obreiros.componentes.StringTools;
import br.com.aeroceti.obreiros.componentes.Genero;
import br.com.aeroceti.obreiros.modelos.instituicoes.Instituicao;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *  Objeto base Colaborador.
 *
 * Esta classe representa um colaborador no movimento espirita.
 * 
 * Copyright (c) 2016 AeroCETI   -  Todos os direitos reservados.
 *                http://www.aeroceti.com
 * 
 * @author Sergio Murilo - smurilo at Gmail.com
 * @version 1.0
 */
@Entity
@Table(name = "Colaborador", uniqueConstraints = {
@UniqueConstraint(columnNames = {"CPF"})})
public class Colaborador implements Serializable {

    private static final long serialVersionUID = 3L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "colaboradorID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(example = "0", description = "ID na Base de dados do objeto")
    private Long colaboradorID;
    
    @NotNull
    @NotBlank
    @Size(min=11, max=11)
    @CPF(message = "CPF inválido!")
    @Column(name = "CPF",            length = 11)
    @Schema(example = "111.222.333-99", description = "CPF Válido da Colaborador")
    private String cpf             = "";
    
    @NotNull
    @NotBlank
    @Column(name = "nome",           length = 50)
    @Size(min=5, max=50, message = "Nome precisa ter no mínimo 5 caracteres")
    @Schema(example = "Joao Maria", description = "Nome principal do usuario")
    private String nome            = "";
    
    @NotNull
    @NotBlank
    @Column(name = "sobrenome",      length = 50)
    @Size(min=5, max=50, message = "Sobrenome precisa ter no mínimo 5 caracteres")
    @Schema(example = "da Silva", description = "Sobrenome do usuario")
    private String sobrenome       = "";

    @NotNull
    @NotBlank
    @Size(min=6, max=45)
    @Email(message = "Email inválido")
    @Column(name = "email",          length = 45)
    @Schema(example = "joao@gmail.com", description = "Endereço Eletrônico (e-mail) do usuario")
    private String email           = "";
    
    @NotNull
    @NotBlank
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dataNascimento", length = 8)
    @Size(min=8, max=10, message = "Data de nascimento do Colaborador no formato dd/mm/AAAA")
    @Schema(example = "01/01/2024", description = "Data de nascimento do usuario no formato dd/mm/AAAA")
    private String dataNascimento  = "";

    @Column(name = "whatsapp",       length = 20)
    @Size(min=0, max=20, message = "Telefone de contato com Whatsapp")
    @Schema(example = "99 9.9999-8888", description = "Telefone de Contato do Usuário com Whatsapp")
    private String whatsapp        = "";

    @Column(name = "sexo",           length = 1)
    @Schema(example = "M", description = "Sexo do Usuário, sendo [M] para Masculino; [F] pra feminino e [N] para Nao Informar.")
    private Genero sexo;

    @Column(name = "password",       length = 20)
    @Schema(example = "minhaSenha2024", description = "Senha do Usuário para acesso ao sistema")
    private String password        = "";

    @Size(min=0, max=65)
    @Column(name = "verificationCode",   length =  65)
    @Schema(example = "", description = "Código de Verificação da conta. Será gerado pelo sistema.")
    private String verificationCode      = "";

    @Column(name = "ativo")
    @Schema(example = "false", description = "Status do Usuário ao se cadastrar é Inativo(False). Após a verificação o sistema ativa a conta.")
    private boolean ativo          = false ;
    
    @Version
    @Column(name = "versao")
    @Schema(example = "0", description = "Controle de Versão do Objeto na Base de dados feita pela Spring-JPA")
    private Long versao;
    
    @Transient
    @Schema(example = "0", description = "Idade do Usuário. Será calculado pelo sistema")
    private int idade = 0;
    
    // Um colaborador tem varias PERMISSOES
    @JsonManagedReference("permissoes")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Permissoes_dos_Colaboradores",
        joinColumns={@JoinColumn(name="colaborador_ID")},
        inverseJoinColumns={@JoinColumn(name="rules_ID")})
    @Schema(description = "Lista das Permissões que um Colaborador possue no sistema em seu perfil", implementation = Rules.class)
    private Set<Rules> permissoes = new HashSet<>();    
    
    // Um colaborador tem um ENDERECO
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco", referencedColumnName = "enderecoID")
    @Schema(description = "Endereço Residencial do Colaborador",  implementation = Endereco.class)
    private Endereco endereco;

    // Um Colaborador participa em uma INSTITUICAO  
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "instituicao", referencedColumnName = "instituicaoID")
    @Schema(description = "Instituição (Casa Espírita) que o Colaborador está vinculado",  implementation = Instituicao.class )
    private Instituicao instituicao;
    
    public Colaborador() {
    }

    public Colaborador(Long colaboradorID) {
        this.colaboradorID = colaboradorID;
    }
        
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    public Long getColaboradorID() {
        return colaboradorID;
    }

    public void setColaboradorID(Long colaboradorID) {
        this.colaboradorID = colaboradorID;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setPermissoes(Set<Rules> permissoes) {
        this.permissoes = permissoes;
    }

    public Set<Rules> getPermissoes() {
        return permissoes;
    }
    
    public void addRules(Rules regra) {
        this.permissoes.add(regra);
        regra.getColaboradores().add(this);
    }
    
    public void removeRules(Rules regra) {
        this.permissoes.remove(regra);
        regra.getColaboradores().remove(this);
    }
    
    // Grava o CPF sem formatacao:
    public void setCpf(String cpf) {
        this.cpf = cpf.replaceAll("\\D", "").trim();
    }
    
    // obtem o CPF FORMATADO 111.222.333-44
    public String getCpf() {
       return cpf.replaceAll( "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4" ).trim();
    }

    // obtem o CPF SEM FORMATACAO
    public String getCleanCPF() {
        return cpf ;
    }
    
    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    // Obtem o Nome Completo do usuario
    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }
    
    // Formata o nome fornecido
    public void setNome(String nome) {
        this.nome = StringTools.formatName(nome) ;
    }

    // Formata o sobrenome fornecido
    public void setSobrenome(String sobrenome) {
        this.sobrenome = StringTools.formatName(sobrenome) ;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String novoValor;
        try {
                novoValor = email.toLowerCase().trim();
        } catch(NullPointerException nulo) {
                novoValor = "";  
        }
        this.email = novoValor ;
    }    

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = StringTools.getTelefoneLimpo(whatsapp);
    }

    public String getWhatsapp() {
           return StringTools.getTelefoneFormatado(this.whatsapp);
    }

    public String getDataNascimento() {
        return dataNascimento.replaceAll( "([0-9]{2})([0-9]{2})([0-9]{4})", "$1/$2/$3" ).trim();
    }

    // Grava a data no formato ddmmaaaa
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento.replaceAll("\\D", "");
    }

    public Genero getSexo() {
        return sexo;
    }

    public void setSexo(Genero sexo) {
        this.sexo = sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
    
    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }

    // Obtem a idade do usuario com base na data atual conforme a data do nascimento 
    public int getIdade() {
        DateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date dataNascInput= sdf.parse(this.getDataNascimento());
            Calendar dateOfBirth = new GregorianCalendar();
            dateOfBirth.setTime(dataNascInput);
            // Cria um objeto calendar com a data atual
            Calendar today = Calendar.getInstance();
            // Obtém a idade baseado no ano
            this.idade = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
            dateOfBirth.add(Calendar.YEAR, this.idade);
            if (today.before(dateOfBirth)) {
                this.idade--;
            } 
        } catch (ParseException e) {
            Logger.getLogger("Colaborador").log(Level.INFO, "Falha na conversao da data: {0}", e.getMessage());
        }       
        return this.idade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (colaboradorID != null ? colaboradorID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Colaborador)) {
            return false;
        }
        Colaborador other = (Colaborador) object;
        return !((this.colaboradorID == null && other.colaboradorID != null) || (this.colaboradorID != null && !this.colaboradorID.equals(other.colaboradorID)));
    }

    @Override
    public String toString() {
        return "Colaborador[ colaboradorID=" + colaboradorID + " ]";
    }

}
/*                    End of Class                                            */