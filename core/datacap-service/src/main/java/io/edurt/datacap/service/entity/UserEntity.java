package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.view.EntityView;
import io.edurt.datacap.service.converter.MapConverter;
import io.edurt.datacap.service.converter.UserEditorConverter;
import io.edurt.datacap.service.entity.itransient.user.UserEditorEntity;
import io.edurt.datacap.service.validation.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "datacap_user")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class UserEntity
        extends BaseEntity
{
    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class,
            ValidationGroup.Crud.Auth.class
    })
    @Size(max = 20)
    @Column(name = "username")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String username;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class,
            ValidationGroup.Crud.Auth.class
    })
    @Size(max = 120)
    @Column(name = "password")
    @JsonView(value = {EntityView.NoneView.class})
    private String password;

    @Column(name = "chat_configure")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String chatConfigure;

    @Column(name = "is_system")
    @JsonView(value = {EntityView.AdminView.class})
    private boolean system;

    @Column(name = "editor_configure")
    @Convert(converter = UserEditorConverter.class)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private UserEditorEntity editorConfigure;

    @Column(name = "avatar_configure")
    @Convert(converter = MapConverter.class)
    @JsonIgnoreProperties(value = {"fsType", "local"})
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Map<String, String> avatarConfigure;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "datacap_user_role_relation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIncludeProperties(value = {"name", "code", "description"})
    @JsonView(value = {EntityView.AdminView.class})
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonView(value = {EntityView.NoneView.class})
    private List<SourceEntity> sources;
}
