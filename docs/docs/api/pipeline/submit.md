---
title: 提交任务
---

请求地址：`/api/v1/pipeline/submit`

请求方式：`POST`

## Body

=== "示例"

    ```json
    {
      "executor": "LocalExecutor",
      "name": "测试",
      "configure": {
        "nodes": [
          {
            "id": "60fa3386-f767-4993-a023-27dbb3c71b7f",
            "category": "source",
            "position": {
              "x": 2,
              "y": 80
            },
            "data": { ... }
          }
        ],
        "connections": [
          {
            "id": "03328bfe-47f0-4017-9843-b6f87584950d",
            "source": "60fa3386-f767-4993-a023-27dbb3c71b7f-output",
            "target": "ed4c0907-0878-4071-8994-bebee43e4e58-input"
          }
        ]
      }
    }
    ```

=== "参数"

    | 参数          |类型| 描述                                                       |
    |-------------|---|----------------------------------------------------------|
    | `name`      |String| 流水线名称                                                    |
    | `configure` |String| 流水线配置                                                    |
    | `executor`  |String| 执行器                                                      |

    > configure 参数请参考组件：https://view-shadcn-ui.devlive.org/components/workflow.html#workflow-props

## Response

=== "示例"

    ```json
    {
      "name": "M 1.0",
      "code": "9e78e83f55864da7a4802e0989ae1f33",
      "active": true,
      "createTime": "2024-11-25 13:48:27",
      "updateTime": "2024-11-25 13:48:27",
      "configure": "[...]",
      "description": "随意修改的仪表盘",
      "avatar": {},
      "version": "1.0",
      "reports": [
        {
          "name": null,
          "code": "24f38722eb6d432a9ee574e675bd0509",
          "active": true,
          "createTime": null,
          "updateTime": null,
          "configure": null,
          "realtime": false,
          "type": null,
          "query": null,
          "description": null,
          "source": null,
          "dataset": null
        }
      ]
    }
    ```

=== "参数"

    |参数|类型| 描述                  |
    |---|---|---------------------|
    |`name`|String| 仪表盘名称               |
    |`code`|String| 仪表盘编码               |
    |`active`|Boolean| 是否激活               |
    |`createTime`|String| 创建时间               |
    |`updateTime`|String| 更新时间               |
    |`configure`|String| 仪表盘配置               |
    |`description`|String| 仪表盘描述               |
    |`avatar`|Object| 仪表盘头像               |
    |`version`|String| 仪表盘版本               |
    |`reports`|Array| 报表列表详见[报表](../report/list.md) |
