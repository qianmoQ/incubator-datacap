---
template: main.html
title: DataCap

hide:
- navigation
- toc
- footer
---

<style xmlns="http://www.w3.org/1999/html">
.md-typeset h1 {
  text-align: center;
  color: transparent;
}
.md-typeset h2 {
  text-align: center;
  font-weight: 1000;
  font-size: 50px;
  margin-top: 60px;
  margin-bottom: 0;
}
</style>

<div style="text-align: center;">
    <img width="100" height="85" style="margin-top: -50px;" src="/assets/logo.png" />
    <p/>DataCap 是用于数据转换、集成和可视化的集成软件。
    <p/>支持多种数据源、文件类型、大数据相关数据库、关系数据库、NoSQL数据库等。
    <p/>通过软件可以实现多数据源的管理，对源下的数据进行各种操作转换、制作数据图表、监控数据源和其他功能。
    <p/>
    <a target="_blank" href="https://gitee.com/devlive-community/datacap/members">
        <img src='https://gitee.com/devlive-community/datacap/badge/fork.svg?theme=white' alt='fork'/>
    </a>
    <a target="_blank" href="https://gitee.com/devlive-community/datacap/stargazers">
        <img src='https://gitee.com/devlive-community/datacap/badge/star.svg?theme=white' alt='star'/>
    </a>
    <a target="_blank" href="https://github.com/devlive-community/datacap/fork">
        <img alt="GitHub stars" src="https://img.shields.io/github/forks/devlive-community/datacap?logo=github">
    </a>
    <a target="_blank" href="https://github.com/devlive-community/datacap/stargazers">
        <img alt="GitHub stars" src="https://img.shields.io/github/stars/devlive-community/datacap?logo=github">
    </a>
    <p/> 
    <p/>
    <a href="/reference/getStarted/install.html" title="马上开始" class="md-button">
        马上开始
    </a>
    <a href="/download.html" title="下载" class="md-button">
      下载
    </a>
    <a href="https://github.com/devlive-community/datacap" target="_blank" title="在 GitHub 上加入我们" class="md-button md-button--primary">
      在 GitHub 上加入我们
    </a>
    <p/><p/><p/><p/>
</div>

## 特性

<br />

<div style="margin: 0 auto" class="grid cards" markdown>

- __统一查询语言__ 

    ---

    DataCap 将所有数据源的查询语言统一为 SQL，无论是关系型数据库、NoSQL、文件系统还是其他中间件，都可以使用 SQL 进行操作。

- __广泛的数据源支持__

    ---

    DataCap 支持超过 40+ 数据源，包括 ClickHouse、MySQL、PostgreSQL、MongoDB、Redis、Elasticsearch、Kafka 等主流数据库和中间件。

- __灵活的连接方式__

    ---

    DataCap 系统支持通过 JDBC、Native、HTTP 等多种协议连接到不同的数据源，提供了更大的灵活性和兼容性。

- __插件化架构设计__

    ---

    DataCap 采用插件化系统设计，支持在线安装、卸载、更新和热部署，方便系统的扩展和维护。

- __完整的 SQL 解析能力__

    ---

    DataCap 内置完整的 SQL 解析器，确保对 SQL 查询的准确解析和执行。

- __数据可视化功能__

    ---

    DataCap 提供了数据可视化的功能，可以将数据转换为图表、图形和报表，实现数据的可视化展示，帮助用户更直观地理解和分析数据。

- __数据源监控__

    ---

    DataCap 提供了数据源监控的功能，可以实时监控数据源的状态、性能和健康指标，帮助用户了解数据源的运行情况，提供数据源的可靠性保障。

- __数据转换与集成__

    ---

    DataCap 支持数据转换和集成，可以将不同数据源的数据转换为统一的格式，并进行数据集成，实现数据的统一化管理和分析，方便用户进行数据迁移和整合。

- __开源透明__

    ---

    DataCap 是开源的，用户可以自由查看、修改和使用源代码，确保系统的透明度和可定制性。

- __多用户支持__

    ---

    DataCap 内置多用户管理系统，支持不同用户权限的配置和管理。

- __完整的 Web UI__

    ---

    DataCap 内置完整的 Web UI，提供了用户友好的界面，方便用户进行数据查询、管理和可视化，实现数据管理的全面控制。

- __加入 (钉钉 ｜ 微信)__

    ---

    <img src="/assets/dingtalk.png" alt="钉钉" style="height: 160px;" /><img src="/assets/wechat.png" alt="微信" style="height: 160px;" />

</div>

## 支持的连接器

<div class="grid cards custom" markdown>

- <a href="https://clickhouse.com" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/clickhouse.svg" alt="ClickHouse" height="60" />
  </a>
- <a href="https://redis.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/redis.svg" alt="Redis" height="60" />
  </a>
- <a href="https://www.postgresql.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/postgresql.svg" alt="PostgreSQL" height="60" />
  </a>
- <a href="https://prestodb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/presto.svg" alt="Presto" height="60" />
  </a>
- <a href="https://www.mysql.com" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/mysql.svg" alt="MySQL" height="60" />
  </a>
- <a href="https://hive.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hive2x.svg" alt="Hive" height="60" />
  </a>
- <a href="https://kyuubi.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kyuubi.svg" alt="Kyuubi" height="60" />
  </a>
- <a href="https://druid.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/druid.svg" alt="Druid" height="60" />
  </a>
- <a href="https://www.elastic.co/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/elasticsearch8x.svg" alt="ElasticSearch" height="60" />
  </a>
- <a href="https://trino.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/trino.svg" alt="Trino" height="60" />
  </a>
- <a href="https://kylin.apache.org" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kylin.svg" alt="Kylin" height="60" />
  </a>
- <a href="https://ignite.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ignite.svg" alt="Ignite" height="60" />
  </a>
- <a href="https://www.ibm.com/db2/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/db2.svg" alt="IBM DB2" height="60" />
  </a>
- <a href="https://www.mongodb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/mongocommunity.svg" alt="MongoDB" height="60" />
  </a>
- <a href="https://www.dremio.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/dremio.svg" alt="Dremio" height="60" />
  </a>
- <a href="https://www.monetdb.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/monetdb.svg" alt="MonetDB" height="60" />
  </a>
- <a href="https://phoenix.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/phoenix.svg" alt="Phoenix" height="60" />
  </a>
- <a href="https://www.h2database.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/h2.svg" alt="H2" height="60" />
  </a>
- <a href="https://www.microsoft.com/sql-server" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/sqlserver.svg" alt="SqlServer" height="60" />
  </a>
- <a href="https://www.oracle.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/oracle.svg" alt="Oracle" height="60" />
  </a>
- <a href="https://crate.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/cratedb.svg" alt="CrateDB" height="60" />
  </a>
- <a href="https://www.dameng.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/dm.svg" alt="DaMeng" height="60" />
  </a>
- <a href="https://tdengine.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/tdengine.svg" alt="TDengine" height="60" />
  </a>
- <a href="https://impala.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/impala.svg" alt="Impala" height="60" />
  </a>
- <a href="https://www.oceanbase.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/oceanbase.svg" alt="OceanBase" height="60" />
  </a>
- <a href="https://neo4j.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/neo4j.svg" alt="Neo4j" height="60" />
  </a>
- <a href="https://iotdb.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/iotdb.svg" alt="IoTDB" height="60" />
  </a>
- <a href="https://www.snowflake.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/snowflake.svg" alt="Snowflake" height="60" />
  </a>
- <a href="https://ydb.tech/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ydb.svg" alt="YDB" height="60" />
  </a>
- <a href="https://zookeeper.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/zookeeper.svg" alt="Zookeeper" height="60" />
  </a>
- <a href="https://duckdb.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/duckdb.svg" alt="DuckDB" height="60" />
  </a>
- <a href="https://www.alibabacloud.com/product/oss" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/alioss.svg" alt="Aliyun OSS" height="60" />
  </a>
- <a href="https://kafka.apache.org" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kafka.svg" alt="Apache Kafka" height="60" />
  </a>
- <a href="https://ceresdb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ceresdb.svg" alt="CeresDB" height="60" />
  </a>
- <a href="https://greptime.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/greptimedb.svg" alt="GreptimeDB" height="60" />
  </a>
- <a href="https://questdb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/questdb.svg" alt="QuestDB" height="60" />
  </a>
- <a href="https://doris.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/doris.svg" alt="Apache Doris" height="60" />
  </a>
- <a href="https://www.starrocks.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/starrocks.svg" alt="StarRocks" height="60" />
  </a>
- <a href="https://www.alibabacloud.com/product/hologres" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hologres.svg" alt="Hologres" height="60" />
  </a>
- <a href="https://hadoop.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hdfs.svg" alt="Apache Hdfs" height="60" />
  </a>
- <a href="https://pinot.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/pinot.svg" alt="Apache Pinot" height="60" />
  </a>
- <a href="https://cassandra.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/cassandra.svg" alt="Apache Cassandra" height="60" />
  </a>
- <a href="https://matrixorigin.cn/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/matrixone.svg" alt="MatrixOne" height="60" />
  </a>
- <a href="https://www.scylladb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/scylladb.svg" alt="ScyllaDB" height="60" />
  </a>
- <a href="https://www.paradedb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/paradedb.svg" alt="ParadeDB" height="60" />
  </a>
- <a href="https://www.timescale.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/timescale.svg" alt="Timescale" height="60" />
  </a>
- <a href="https://solr.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/solr.svg" alt="Apache Solr" height="60" />
  </a>
- <a href="https://www.influxdata.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/influxdb.svg" alt="InfluxDB" height="60" />
  </a>

</div>
