package io.edurt.datacap.service.wrapper;

import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.view.EntityView;
import io.edurt.datacap.spi.model.Pagination;
import io.edurt.datacap.spi.model.Response;
import lombok.Data;

import java.util.List;

@Data
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ResponseWrapper
{
    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private List<String> headers;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private List<String> types;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private List<Object> columns;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Boolean isConnected;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Boolean isSuccessful;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String message;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private TimeWrapper connection;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private TimeWrapper processor;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private String content;

    @JsonView(value = {EntityView.UserView.class, EntityView.AdminView.class})
    private Pagination pagination;

    public static ResponseWrapper from(Response response)
    {
        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.setHeaders(response.getHeaders());
        wrapper.setTypes(response.getTypes());
        wrapper.setColumns(response.getColumns());
        wrapper.setIsConnected(response.getIsConnected());
        wrapper.setIsSuccessful(response.getIsSuccessful());
        wrapper.setMessage(response.getMessage());
        wrapper.setConnection(TimeWrapper.from(response.getConnection()));
        wrapper.setProcessor(TimeWrapper.from(response.getProcessor()));
        wrapper.setContent(response.getContent());
        wrapper.setPagination(response.getPagination());
        return wrapper;
    }
}
