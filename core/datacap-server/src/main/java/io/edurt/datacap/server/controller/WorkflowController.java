package io.edurt.datacap.server.controller;

import io.edurt.datacap.service.entity.WorkflowEntity;
import io.edurt.datacap.service.repository.WorkflowRepository;
import io.edurt.datacap.service.service.WorkflowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/workflow")
public class WorkflowController
        extends BaseController<WorkflowEntity>
{
    protected WorkflowController(WorkflowRepository repository, WorkflowService service)
    {
        super(repository, service);
    }
}
