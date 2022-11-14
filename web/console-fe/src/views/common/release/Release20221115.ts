import {ReleaseNote} from "@/views/common/release/ReleaseNote";

const content = `
> The current release involves several major updates. The following link is [**Roadmap**](https://github.com/orgs/EdurtIO/projects/1)

#### General

- Replace plugin name to id
- Support for internationalization [issues-82](https://github.com/EdurtIO/incubator-datacap/pull/82)
- Reduce size of docker image
- Switch bash docker image to eclipse-temurin:8-jdk-focal
- Support ssl [issues-75](https://github.com/EdurtIO/incubator-datacap/pull/75)
- Extract the plug-in to get the global tool
- Support database write operation [issues-70](https://github.com/EdurtIO/incubator-datacap/pull/70)
- Supports user rights management
- Support code snippet [issues-74](https://github.com/EdurtIO/incubator-datacap/pull/74)
- Support editor auto completion
- Support to provide data source schema tree bar [issues-106](https://github.com/EdurtIO/incubator-datacap/pull/106)
- Support multiple editor [issues-110](https://github.com/EdurtIO/incubator-datacap/pull/110)
- Add profile for user
- Support change user password
- Add data source radar map within 7 days
- Add about page
- Add feedback [issues-126](https://github.com/EdurtIO/incubator-datacap/pull/126)

#### SPI

- Add custom validator

#### Plugins

- Support MongoDB
- Support Dremio
- Support HBase jdbc for Phoenix [issues-103](https://github.com/EdurtIO/incubator-datacap/issues/103)
- Support H2
- Support SqlServer
- Support Oracle

#### Redis

- Fix cannot init RedisConnection [issues-71](https://github.com/EdurtIO/incubator-datacap/issues/71)

#### ElasticSearch

- Update version to \`7.10.0\`

#### Kyuubi

- Bump Kyuubi \`1.6.0-incubating\`
`;

const releaseNote: ReleaseNote = {
  content: content,
  latest: true,
  time: "2022-11-15",
  version: "1.1.0"
};

export default {
  releaseNote
}
