<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2">{{ $t('pipeline.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnTooltip :content="$t('pipeline.common.create')">
        <ShadcnLink link="/admin/pipeline/info">
          <ShadcnButton circle size="small">
            <ShadcnIcon icon="Plus" size="15"/>
          </ShadcnButton>
        </ShadcnLink>
      </ShadcnTooltip>

    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #executor="{row}">
          <ShadcnTooltip :content="row.executor">
            <ShadcnAvatar size="small" :src="`/static/images/executor/${row.executor}.png`"/>
          </ShadcnTooltip>
        </template>

        <template #from="{row}">
          <ShadcnTooltip :content="row.from.name">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + row.from.type + '.png'"/>
          </ShadcnTooltip>
        </template>

        <template #to="{row}">
          <ShadcnTooltip :content="row.to.name">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + row.to.type + '.png'"/>
          </ShadcnTooltip>
        </template>

        <template #state="{row}">
          <ShadcnTag class="w-20" :color="Common.getColor(row.state)">
            {{ Common.getText(i18n, row.state) }}
          </ShadcnTag>
        </template>

        <template #action="{row}">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.error')">
              <ShadcnButton circle
                            size="small"
                            type="error"
                            :disabled="row.state !== 'FAILURE' && !(row.state == 'STOPPED' && row.message)"
                            @click="visibleShowMessage(true, row)">
                <ShadcnIcon icon="TriangleAlert" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnDropdown trigger="click" position="right">
              <template #trigger>
                <ShadcnButton circle size="small">
                  <ShadcnIcon icon="Cog" :size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem :disabled="(loginUserId !== row.user.id) || !row.available">
                <ShadcnLink :link="`/admin/source/${row?.code}`" target="_blank">
                  <div class="flex items-center space-x-2">
                    <ShadcnIcon icon="Cog" size="15"/>
                    <span>{{ $t('source.common.manager') }}</span>
                  </div>
                </ShadcnLink>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="(loginUserId !== row.user.id)" @on-click="visibleHistory(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="History" size="15"/>
                  <span>{{ $t('source.common.syncHistory') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="(loginUserId !== row.user.id) || !row.available" @on-click="visibleSyncMetadata(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="RefreshCcwDot" size="15"/>
                  <span>{{ $t('source.common.syncMetadata') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="loginUserId !== row.user.id" @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Trash" size="15"/>
                  <span>{{ $t('common.deleteData') }}</span>
                </div>
              </ShadcnDropdownItem>
            </ShadcnDropdown>
          </ShadcnSpace>
        </template>
      </ShadcnTable>

      <ShadcnPagination v-if="data.length > 0"
                        v-model="pageIndex"
                        class="py-2"
                        show-total
                        show-sizer
                        :page-size="pageSize"
                        :total="dataCount"
                        :sizerOptions="[10, 20, 50]"
                        @on-change="onPageChange"
                        @on-prev="onPrevChange"
                        @on-next="onNextChange"
                        @on-change-size="onSizeChange"/>
    </div>
  </ShadcnCard>

  <!--              <DropdownMenu>-->
  <!--                <DropdownMenuTrigger as-child>-->
  <!--                  <Button size="icon" class="rounded-full w-6 h-6" variant="outline">-->
  <!--                    <Cog class="w-full justify-center" :size="14"/>-->
  <!--                  </Button>-->
  <!--                </DropdownMenuTrigger>-->
  <!--                <DropdownMenuContent>-->
  <!--                  <DropdownMenuGroup>-->
  <!--                    <DropdownMenuItem class="cursor-pointer" :disabled="row.state !== 'RUNNING'" @click="handlerStop(true, row)">-->
  <!--                      <CircleStop class="mr-2 h-4 w-4"/>-->
  <!--                      <span>{{ $t('pipeline.common.stop') }}</span>-->
  <!--                    </DropdownMenuItem>-->
  <!--                    <DropdownMenuItem class="cursor-pointer" @click="handlerLogger(true, row)">-->
  <!--                      <Rss class="mr-2 h-4 w-4"/>-->
  <!--                      <span>{{ $t('pipeline.common.logger') }}</span>-->
  <!--                    </DropdownMenuItem>-->
  <!--                    <DropdownMenuItem class="cursor-pointer" :disabled="row.state == 'RUNNING'" @click="handlerDelete(true, row)">-->
  <!--                      <Delete class="mr-2 h-4 w-4"/>-->
  <!--                      <span>{{ $t('pipeline.common.delete') }}</span>-->
  <!--                    </DropdownMenuItem>-->
  <!--                    <DropdownMenuItem class="cursor-pointer" @click="handlerFlow(true, row)">-->
  <!--                      <Flower class="mr-2 h-4 w-4"/>-->
  <!--                      <span>{{ $t('pipeline.common.flow') }}</span>-->
  <!--                    </DropdownMenuItem>-->
  <!--                  </DropdownMenuGroup>-->
  <!--                </DropdownMenuContent>-->
  <!--              </DropdownMenu>-->
  <!--            </div>-->
  <!--          </template>-->
  <MarkdownPreview v-if="dataMessageVisible && dataInfo"
                   :is-visible="dataMessageVisible"
                   :content="dataInfo.message"
                   @close="visibleShowMessage(false, null)"/>

  <!--  <PipelineLogger v-if="dataLoggerVisible && dataInfo" :is-visible="dataLoggerVisible" :info="dataInfo" @close="handlerLogger(false, null)"/>-->
  <!--  <PipelineDelete v-if="dataDeleteVisible && dataInfo" :is-visible="dataDeleteVisible" :info="dataInfo" @close="handlerDelete(false, null)"/>-->
  <!--  <PipelineStop v-if="dataStopVisible && dataInfo" :is-visible="dataStopVisible" :info="dataInfo" @close="handlerStop(false, null)"/>-->
  <!--  <PipelineFlow v-if="dataFlowVisible && dataInfo" :is-visible="dataFlowVisible" :info="dataInfo" @close="handlerFlow(false, null)"/>-->
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/admin/pipeline/PipelineUtils'
import { useI18n } from 'vue-i18n'
import PipelineService from '@/services/pipeline'
import Common from '@/utils/common.ts'
import { PipelineModel } from '@/model/pipeline.ts'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue'

export default defineComponent({
  name: 'PipelineHome',
  components: { MarkdownPreview },
  setup()
  {
    const i18n = useI18n()
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(i18n)
    return {
      i18n,
      filter,
      headers
    }
  },
  computed: {
    Common()
    {
      return Common
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as PipelineModel | null,
      dataMessageVisible: false,
      dataLoggerVisible: false,
      dataDeleteVisible: false,
      dataStopVisible: false,
      dataFlowVisible: false
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.loading = true
      PipelineService.getAll(this.filter)
                     .then((response) => {
                       if (response.status) {
                         this.data = response.data.content
                         this.dataCount = response.data.total
                         this.pageSize = response.data.size
                         this.pageIndex = response.data.page
                       }
                     })
                     .finally(() => this.loading = false)
    },
    fetchData(value: number)
    {
      this.filter.page = value
      this.filter.size = this.pageSize
      this.handleInitialize()
    },
    onPageChange(value: number)
    {
      this.fetchData(value)
    },
    onPrevChange(value: number)
    {
      this.fetchData(value)
    },
    onNextChange(value: number)
    {
      this.fetchData(value)
    },
    onSizeChange(value: number)
    {
      this.pageSize = value
      this.fetchData(this.pageIndex)
    },
    visibleShowMessage(opened: boolean, value: null | PipelineModel)
    {
      this.dataMessageVisible = opened
      this.dataInfo = value
    },
    handlerLogger(opened: boolean, value: null | PipelineModel)
    {
      this.dataLoggerVisible = opened
      this.dataInfo = value
    },
    handlerDelete(opened: boolean, value: null | PipelineModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    handlerStop(opened: boolean, value: null | PipelineModel)
    {
      this.dataStopVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    handlerFlow(opened: boolean, value: null | PipelineModel)
    {
      this.dataFlowVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
