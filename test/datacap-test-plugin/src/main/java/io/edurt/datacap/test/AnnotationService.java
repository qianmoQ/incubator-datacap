package io.edurt.datacap.test;

import io.edurt.datacap.plugin.annotation.InjectService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@InjectService
public class AnnotationService
        implements DataService
{
    @Override
    public void print()
    {
        log.info("Annotation Service");
    }
}
