---
title: 用户列表 (管理员)
---

请求地址: `/api/v1/user/list`

请求方式: `POST`

!!! note

    该接口用于获取仪表盘列表，需要传递分页数据 `page` 和 `size` 作为参数，分页数据默认为 `1`，`size` 默认为 `12`。详细参考 [分页实体](../common/page.md)

## Response

=== "示例"

```json
[
  {
    "id": 1,
    "name": null,
    "code": null,
    "active": true,
    "createTime": "2023-07-04 21:47:24",
    "updateTime": "2024-11-03 19:38:47",
    "username": "admin",
    "password": "$2a$10$FyWYvR61FHzT1szZtV69j.APDCrAqRcqMO.CiUYOSRiXmvugDsALu",
    "chatConfigure": "{\"type\":\"ChatGPT\",\"host\":null,\"token\":null,\"timeout\":0,\"contentCount\":5}",
    "system": false,
    "editorConfigure": {
      "fontSize": 18,
      "theme": "tomorrow"
    },
    "avatarConfigure": {
      "path": ""
    },
    "roles": [
      {
        "code": "ROLE_ADMIN",
        "description": "这是管理员路由，可以管理站点所有功能"
      }
    ],
    "sources": [
      {
        "id": 2,
        "name": "MySQL",
        "code": "9dbb343af37445b4891e40e2b8dafe84",
        "active": true,
        "createTime": "2024-07-07 19:41:39",
        "updateTime": "2024-11-24 10:18:51",
        "description": null,
        "type": "MySQL",
        "protocol": "JDBC",
        "host": "127.0.0.1",
        "port": 3306,
        "username": "root",
        "password": "12345678",
        "catalog": null,
        "database": "datacap",
        "ssl": false,
        "usedConfig": false,
        "version": "8.3.0",
        "available": true,
        "message": null,
        "configures": {
          "useOldAliasMetadataBehavior": true
        },
        "schema": null,
        "pipelines": null,
        "home": null,
        "user": {
          "id": 1,
          "username": "admin"
        },
        "public": null
      }
    ]
  }
]
```

=== "参数"
