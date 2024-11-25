package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.view.EntityView;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.body.PipelineFieldBody;
import io.edurt.datacap.service.converter.PropertiesConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Properties;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "datacap_pipeline")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PipelineEntity
        extends BaseEntity
{
    @Column(name = "content", unique = true)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String content;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private RunState state;

    @Column(name = "message")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String message;

    @Column(name = "work")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String work;

    @Column(name = "elapsed")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Long elapsed;

    @Column(name = "executor")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String executor = "Seatunnel";

    @ManyToOne
    @JoinColumn(name = "from_id")
    @JsonIncludeProperties(value = {"id", "name", "type"})
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private SourceEntity from;

    @Column(name = "from_configures")
    @Convert(converter = PropertiesConverter.class)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Properties fromConfigures;

    @ManyToOne
    @JoinColumn(name = "to_id")
    @JsonIncludeProperties(value = {"id", "name", "type"})
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private SourceEntity to;

    @Column(name = "to_configures")
    @Convert(converter = PropertiesConverter.class)
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Properties toConfigures;

    @Column(name = "flow_configure")
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String flowConfigure;

    @ManyToOne
    @JoinTable(name = "datacap_pipeline_user_relation",
            joinColumns = @JoinColumn(name = "pipeline_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private UserEntity user;

    /**
     * Converts a PipelineEntity object to a PipelineBody object.
     *
     * @return the converted PipelineBody object
     */
    public PipelineBody entityToBody()
    {
        PipelineFieldBody from = PipelineFieldBody.builder()
                .code(this.getFrom().getCode())
                .configures(this.getFromConfigures())
                .build();
        PipelineFieldBody to = PipelineFieldBody.builder()
                .code(this.getTo().getCode())
                .configures(this.getToConfigures())
                .build();
        return PipelineBody.builder()
                .code(this.getCode())
                .content(this.getContent())
                .from(from)
                .to(to)
                .executor(this.getExecutor())
                .build();
    }
}
