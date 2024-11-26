package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.view.EntityView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_table")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_DOESNT_OVERRIDE_EQUALS"})
public class TableEntity
        extends BaseEntity
{
    @Column(name = "description")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String description;

    @Column(name = "type")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String type;

    @Column(name = "engine")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String engine;

    @Column(name = "format")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String format;

    @Column(name = "in_rows")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String rows;

    @Column(name = "in_create_time")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String inCreateTime;

    @Column(name = "in_update_time")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String inUpdateTime;

    @Column(name = "collation")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String collation;

    @Column(name = "comment")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String comment;

    @Column(name = "avg_row_length")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String avgRowLength;

    @Column(name = "data_length")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String dataLength;

    @Column(name = "index_length")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String indexLength;

    @Column(name = "auto_increment")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String autoIncrement;

    @ManyToOne()
    @JoinTable(name = "datacap_metadata_table_database_relation",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "database_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private DatabaseEntity database;

    @OneToMany(mappedBy = "table", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ColumnEntity> columns;

    public TableEntity(Long id, String name, Date createTime)
    {
        super(id, name, null, true, createTime, null);
    }

    public String getRows()
    {
        if (isStringNull(rows)) {
            return "0";
        }
        return rows;
    }

    private boolean isStringNull(String cs)
    {
        return "null".equals(cs);
    }
}
