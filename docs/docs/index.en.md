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
<p/>DataCap is integrated software for data transformation, integration, and visualization.
    <p/>Supports multiple data sources, file types, big data-related databases, relational databases, NoSQL databases, and more.
    <p/>Through the software, the management of multiple data sources can be realized, and various operations can be performed on the data under the source, data charts can be made, data sources can be monitored, and other functions can be performed.
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
    <a href="/reference/getStarted/install.html" title="Get Started" class="md-button">
        Get Started
    </a>
    <a href="/download.html" title="Download" class="md-button">
      Download
    </a>
    <a href="https://github.com/devlive-community/datacap" target="_blank" title="Join us on GitHub" class="md-button md-button--primary">
      Join us on GitHub
    </a>
    <p/><p/><p/><p/>
</div>

## Features

<br />

<div style="margin: 0 auto" class="grid cards" markdown>

- __Unified Query Language__

    ---
    
    DataCap unifies the query language for all data sources into SQL, whether it's a relational database, NoSQL, file system, or other middleware.

- __Extensive data source support__

    ---
    
    DataCap supports more than 40+ data sources, including mainstream databases and middleware such as ClickHouse, MySQL, PostgreSQL, MongoDB, Redis, Elasticsearch, and Kafka.

- __Flexible Connectivity__
    
    ---
    
    The DataCap system supports connecting to different data sources through multiple protocols such as JDBC, Native, HTTP, etc., providing greater flexibility and compatibility.

- __Plug-in Architecture Design__

    ---
    
    DataCap is designed as a plug-in system that supports online installation, uninstallation, updates, and hot deployment, facilitating system expansion and maintenance.

- __Full SQL parsing capabilities__

    ---
    
    DataCap has a built-in full SQL parser to ensure accurate parsing and execution of SQL queries.

- __Data Visualization Capabilities__

    ---
    
    DataCap provides data visualization capabilities, which can convert data into charts, graphs, and reports to visualize data and help users understand and analyze data more intuitively.

- __Data Source Monitoring__

    ---
    
    DataCap provides the data source monitoring function, which can monitor the status, performance, and health metrics of data sources in real time, help users understand the running status of data sources, and ensure the reliability of data sources.

- __Data Transformation & Integration__

    ---
    
    DataCap supports data transformation and integration, converts data from different data sources into a unified format, and integrates data to achieve unified management and analysis of data, facilitating data migration and integration.

- __Open Source Transparent__
    
    ---
    
    DataCap is open-source, allowing users to freely view, modify, and use the source code, ensuring transparency and customizability of the system.

- __Multi-user support__

    ---
    
    DataCap has a built-in multi-user management system that supports the configuration and management of different user permissions.

- __Full web UI__

    ---
    
    DataCap has a built-in full web UI that provides a user-friendly interface for data query, management, and visualization for full control of data management.

- __Join (DingTalk | WeChat)__

    ---

    <img src="/assets/dingtalk.png" alt="钉钉" style="height: 160px;" /><img src="/assets/wechat.png" alt="微信" style="height: 160px;" />

</div>

## Supported connectors

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
