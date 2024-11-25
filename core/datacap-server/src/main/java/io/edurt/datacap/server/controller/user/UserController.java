package io.edurt.datacap.server.controller.user;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.server.controller.BaseController;
import io.edurt.datacap.service.annotation.DynamicJsonView;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.repository.RoleRepository;
import io.edurt.datacap.service.repository.UserRepository;
import io.edurt.datacap.service.service.UserLogService;
import io.edurt.datacap.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController
        extends BaseController<UserEntity>
{
    private final UserRepository repository;
    private final UserService service;
    private final UserLogService userLogService;
    private final RoleRepository roleRepository;

    public UserController(UserRepository repository, UserService service, UserLogService userLogService, RoleRepository roleRepository)
    {
        super(repository, service);
        this.repository = repository;
        this.service = service;
        this.userLogService = userLogService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = {"{id}", ""})
    @DynamicJsonView
    public CommonResponse<UserEntity> info(@PathVariable(required = false) Long id)
    {
        return this.service.info(id);
    }
//
//    @PutMapping(value = "changePassword")
//    public CommonResponse<Long> changePassword(@Validated @RequestBody UserPasswordBody configure)
//    {
//        return this.service.changePassword(configure);
//    }
//
//    @PutMapping(value = "changeUsername")
//    public CommonResponse<Long> changeUsername(@Validated @RequestBody UserNameBody configure)
//    {
//        return this.service.changeUsername(configure);
//    }
//
//    @PutMapping(value = "changeThirdConfigure")
//    public CommonResponse<Long> changeThirdConfigure(@Validated @RequestBody AiModel configure)
//    {
//        return this.service.changeThirdConfigure(configure);
//    }
//
//    @PostMapping(value = "log")
//    public CommonResponse<PageEntity<UserLogEntity>> getAllLogByFilter(@RequestBody FilterBody filter)
//    {
//        return this.userLogService.getAllByFilter(filter);
//    }
//
//    @GetMapping(value = "sugs/{id}")
//    public CommonResponse<List<Object>> getSugs(@PathVariable Long id)
//    {
//        return this.service.getSugs(id);
//    }
//
//    @GetMapping(value = "menus")
//    public CommonResponse<List<TreeRecord>> getMenus()
//    {
//        return this.service.getMenus();
//    }
//
//    @PutMapping(value = "allocationRole")
//    public CommonResponse<UserEntity> allocationRole(@RequestBody UserRole configure)
//    {
//        UserEntity user = new UserEntity();
//        user.setId(configure.getUserId());
//        Set<RoleEntity> roles = Sets.newHashSet();
//        configure.getRoles()
//                .forEach(id -> roleRepository.findById(id)
//                        .ifPresent(roles::add));
//        user.setRoles(roles);
//        return this.service.saveOrUpdate(user);
//    }
//
//    @PostMapping
//    public CommonResponse<UserEntity> saveAndUpdate(@RequestBody UserEntity configure)
//    {
//        return this.service.saveOrUpdate(configure);
//    }
//
//    @PutMapping(value = "changeEditorConfigure")
//    public CommonResponse<Long> changeEditorConfigure(@Validated @RequestBody UserEditorEntity configure)
//    {
//        return this.service.changeEditorConfigure(configure);
//    }
//
//    @SneakyThrows
//    @PostMapping("uploadAvatar")
//    public CommonResponse<FsResponse> uploadAvatar(@RequestParam("file") MultipartFile file)
//    {
//        return this.service.uploadAvatar(file);
//    }
}
