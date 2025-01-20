package br.com.alura.forum_hub.domain.usuario;

import br.com.alura.forum_hub.domain.usuario.dto.AtualizarUsuarioDTO;
import br.com.alura.forum_hub.domain.usuario.dto.CriarUsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@Entity(name = "Usuario")
@EqualsAndHashCode(of ="id")
public class Usuario implements UserDetails {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String nome;
    private String sobrenome;
    private String email;
    private Boolean enabled;

    public Usuario(CriarUsuarioDTO criarUsuarioDTO,
                   String hashedPassword) {
        this.username = criarUsuarioDTO.username();
        this.password = hashedPassword;
        this.role = Role.User;
        this.nome = capitalizado(criarUsuarioDTO.nome());
        this.sobrenome = capitalizado(criarUsuarioDTO.sobrenome());
        this.email = criarUsuarioDTO.email();
        this.enabled = true;
    }

    public void atualizarUsuarioComSenha(AtualizarUsuarioDTO atualizarUsuarioDTO,
                                         String hashedPassword) {
        if (atualizarUsuarioDTO.password() != null) {
            this.password = hashedPassword;
        }
        if (atualizarUsuarioDTO.role() != null) {
            this.role = atualizarUsuarioDTO.role();
        }
        if (atualizarUsuarioDTO.nome() != null) {
            this.nome = capitalizado(atualizarUsuarioDTO.nome());
        }
        if (atualizarUsuarioDTO.sobrenome() != null){
            this.sobrenome = capitalizado(atualizarUsuarioDTO.sobrenome());
        }
        if (atualizarUsuarioDTO.email() != null){
            this.email = atualizarUsuarioDTO.email();
        }
        if (atualizarUsuarioDTO.enabled() != null){
            this.enabled = atualizarUsuarioDTO.enabled();
        }
    }

    public void atualizarUsuario(AtualizarUsuarioDTO atualizarUsuarioDTO) {
        if (atualizarUsuarioDTO.role() != null) {
            this.role = atualizarUsuarioDTO.role();
        }
        if (atualizarUsuarioDTO.nome() != null) {
            this.nome = capitalizado(atualizarUsuarioDTO.nome());
        }
        if (atualizarUsuarioDTO.sobrenome() != null){
            this.sobrenome = capitalizado(atualizarUsuarioDTO.sobrenome());
        }
        if (atualizarUsuarioDTO.email() != null){
            this.email = atualizarUsuarioDTO.email();
        }
        if (atualizarUsuarioDTO.enabled() != null){
            this.enabled = atualizarUsuarioDTO.enabled();
        }
    }

    public void deletarUsuario() {
        this.enabled = false;
    }

    public String capitalizado(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("Role_User"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
