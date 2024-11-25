package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DashboardEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.repository.DashboardRepository;
import io.edurt.datacap.service.repository.ReportRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DashboardService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl
        implements DashboardService
{
    private final DashboardRepository repository;
    private final ReportRepository reportRepository;

    public DashboardServiceImpl(DashboardRepository repository, ReportRepository reportRepository)
    {
        this.repository = repository;
        this.reportRepository = reportRepository;
    }

    @Override
    public CommonResponse<PageEntity<DashboardEntity>> getAll(BaseRepository<DashboardEntity, Long> baseRepository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    @Override
    public CommonResponse<DashboardEntity> saveOrUpdate(BaseRepository<DashboardEntity, Long> baseRepository, DashboardEntity configure)
    {
        configure.setUser(UserDetailsService.getUser());
        configure.getReports()
                .forEach(entity -> reportRepository.findByCode(entity.getCode())
                        .ifPresentOrElse(
                                report -> entity.setId(report.getId()),
                                () -> {throw new RuntimeException("Report [ " + entity.getCode() + " ] not found");}
                        ));
        return CommonResponse.success(repository.save(configure));
    }
}
