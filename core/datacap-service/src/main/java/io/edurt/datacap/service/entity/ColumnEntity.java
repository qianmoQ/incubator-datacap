package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.view.EntityView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_column")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_DOESNT_OVERRIDE_EQUALS"})
public class ColumnEntity
        extends BaseEntity
{
    @Column(name = "description")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String description;

    @Column(name = "type")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String type;

    @Column(name = "comment")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String comment;

    @Column(name = "default_value")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String defaultValue;

    @Column(name = "position")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String position;

    @Column(name = "is_nullable")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String isNullable;

    @Column(name = "maximum_length")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String maximumLength;

    @Column(name = "collation")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String collation;

    @Column(name = "is_key")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String isKey;

    @Column(name = "privileges")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String privileges;

    @Column(name = "data_type")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String dataType;

    @Column(name = "extra")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String extra;

    @ManyToOne
    @JoinTable(name = "datacap_metadata_column_table_relation",
            joinColumns = @JoinColumn(name = "column_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private TableEntity table;

    public ColumnEntity(Long id, String name, Date createTime)
    {
        super(id, name, null, true, createTime, null);
    }
}
