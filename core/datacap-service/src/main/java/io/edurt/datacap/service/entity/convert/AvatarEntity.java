package io.edurt.datacap.service.entity.convert;

import com.fasterxml.jackson.annotation.JsonView;
import io.edurt.datacap.common.view.EntityView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AvatarEntity
{
    @JsonView(value = {EntityView.AdminView.class})
    private String type;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String path;

    @JsonView(value = {EntityView.AdminView.class})
    private String local;
}
