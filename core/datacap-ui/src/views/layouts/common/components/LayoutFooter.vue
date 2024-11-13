<template>
  <ShadcnGlobalFooter company="DataCap"
                      description="DataCap is integrated software for data transformation, integration, and visualization. Support a variety of data sources, file types, big data related database, relational database, NoSQL database, etc. "
                      :links="links"
                      :socials="socials">
    <template #logo>
      <ShadcnAvatar src="/static/images/logo.png" alt="DataCap Logo"/>
    </template>

    <template #copyright>
      <p>Copyright &copy; 2022 DataCap. All rights reserved.</p>
    </template>
  </ShadcnGlobalFooter>
</template>

<script setup lang="ts">
const links = [
  {
    title: 'Resources',
    links: [
      { label: 'Blog', href: 'https://datacap.devlive.org/', external: true, target: '_blank' },
      { label: 'Gitee', href: 'https://gitee.com/devlive-community/datacap', external: true, target: '_blank' },
      { label: 'Github', href: 'https://github.com/devlive-community/datacap', external: true, target: '_blank' },
      { label: 'Documentation', href: 'https://datacap.devlive.org/', external: true, target: '_blank' }
    ]
  },
  {
    title: 'Community',
    links: [
      { label: 'Website', href: 'https://datacap.devlive.org/', external: true, target: '_blank' },
      { label: 'Issues', href: 'https://github.com/devlive-community/datacap/issues', external: true, target: '_blank' },
      { label: 'Discussions', href: 'https://github.com/devlive-community/datacap/discussions', external: true, target: '_blank' },
      { label: 'Community', href: 'https://community.devlive.org', external: true, target: '_blank' }
    ]
  },
  {
    title: 'Projects',
    links: [
      { label: 'Database Tools', href: 'https://dbm.devlive.org', external: true, target: '_blank' },
      { label: 'Open AI Java SDK', href: 'https://openai-java-sdk.devlive.org', external: true, target: '_blank' },
      { label: 'View Shadcn UI', href: 'https://shadcn.vue.devlive.org', external: true, target: '_blank' },
      { label: 'InfoSphere', href: 'https://infosphere.devlive.org', external: true, target: '_blank' }
    ]
  }
]
const socials = [
  { icon: 'Github', href: 'https://github.com/devlive-community/datacap', label: 'GitHub' }
]
</script>
