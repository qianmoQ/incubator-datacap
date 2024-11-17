<template>
  <ShadcnLayout>
    <ShadcnLayoutWrapper>
      <ShadcnLayoutSider>
        <ShadcnCard :title="$t('common.chat')">
          <template #extra>
            <ShadcnButton circle size="small" @click="visibleInfo(true)">
              <ShadcnIcon icon="Plus" size="15"/>
            </ShadcnButton>
          </template>

          <div class="relative min-h-screen">
            <ShadcnSpin v-model="loading" fixed/>

            <ShadcnToggleGroup v-model="toggleValue" @on-change="onChange" class="p-1">
              <ShadcnRow gutter="8">
                <ShadcnCol v-for="item of data" span="12">
                  <ShadcnToggle class="w-full" :key="item.id" :value="item.id">
                    <div class="flex items-center space-x-4">
                      <ShadcnAvatar size="small" :src="item.avatar" :alt="item.name"/>

                      <div class="text-sm font-medium leading-none">{{ item.name }}</div>
                    </div>
                  </ShadcnToggle>
                </ShadcnCol>
              </ShadcnRow>

            </ShadcnToggleGroup>
          </div>
        </ShadcnCard>
      </ShadcnLayoutSider>

      <ShadcnLayoutMain>
        <ShadcnLayoutContent>
          <div v-if="dataInfo">
            <ShadcnCard>
              <template #title>
                <div class="flex flex-row items-center justify-between">
                  <div class="flex items-center space-x-4">
                    <ShadcnAvatar :src="dataInfo.avatar" :alt="dataInfo.name"/>
                    <div>
                      <p class="text-sm font-medium leading-none">{{ dataInfo.name }}</p>
                      <p class="text-sm text-muted-foreground">{{ dataInfo.description }}</p>
                    </div>
                  </div>
                </div>
              </template>

              <template #extra>
                <div class="flex h-5 items-center space-x-4 text-sm">
                  <div>
                    Prompt Tokens: {{ promptTokens }}
                  </div>
                  <Separator orientation="vertical"/>
                  <div>
                    Completion Tokens: {{ completionTokens }}
                  </div>
                  <Separator orientation="vertical"/>
                  <div>
                    Total Tokens: {{ totalTokens }}
                  </div>
                </div>
              </template>

              <div class="relative min-h-screen">
                <ShadcnSpin v-model="loadingMessages" fixed/>

                <div class="w-full overflow-y-auto overflow-x-hidden h-full flex flex-col">
                  <div ref="messagesContainer" class="w-full overflow-y-auto overflow-x-hidden flex flex-col" style="height: 550px;">
                    <template v-for="item in messages">
                      <div :class="cn('flex flex-col gap-2 p-4 whitespace-pre-wrap',
                                  item.type === 'question' ? 'items-end' : 'items-start'
                            )">
                        <div class="flex gap-3 items-center">
                          <span class="bg-accent p-3 rounded-md max-w-lg">{{ item.content }}</span>
                        </div>

                        <div v-if="item.type === 'answer'" class="flex text-sm text-muted-foreground mt-0.5 space-x-2">
                          <div>Model: {{ item.model }}</div>
                          <ShadcnDivider type="horizontal"/>
                          <div>Prompt Tokens: {{ item.promptTokens }}</div>
                          <ShadcnDivider type="horizontal"/>
                          <div>Completion Tokens: {{ item.completionTokens }}</div>
                          <ShadcnDivider type="horizontal"/>
                          <div>Total Tokens: {{ item.totalTokens }}</div>
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </div>

              <template #footer>
                <div class="p-2 flex justify-between w-full items-center gap-2">
                  <ShadcnInput v-model="inputValue" type="textarea" placeholder="Type a message ..."/>

                  <ShadcnButton :loading="submitting" :disabled="!inputValue || submitting" @click="onSubmit">
                    <ShadcnIcon icon="Send" size="18"/>
                  </ShadcnButton>
                </div>
              </template>
            </ShadcnCard>
          </div>
        </ShadcnLayoutContent>
      </ShadcnLayoutMain>
    </ShadcnLayoutWrapper>
  </ShadcnLayout>

  <ChatInfo v-if="dataVisible" :is-visible="dataVisible" @close="visibleInfo($event)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import { FilterModel } from '@/model/filter.ts'
import ChatService from '@/services/chat.ts'
import ChatInfo from '@/views/pages/admin/chat/ChatInfo.vue'
import { ChatModel } from '@/model/chat.ts'
import { toNumber } from 'lodash'
import { cn } from '@/lib/utils.ts'
import { MessageModel } from '@/model/message.ts'
import MessageService from '@/services/message.ts'

export default defineComponent({
  name: 'ChatHome',
  components: { ChatInfo },
  setup()
  {
    const filter: FilterModel = new FilterModel()

    return {
      filter,
      cn
    }
  },
  data()
  {
    return {
      loading: true,
      data: [] as ChatModel[],
      dataInfo: null as ChatModel | null,
      dataVisible: false,
      loadingMessages: false,
      messages: [] as MessageModel[],
      promptTokens: 0,
      completionTokens: 0,
      totalTokens: 0,
      submitting: false,
      toggleValue: null,
      inputValue: ''
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
      ChatService.getAll(this.filter)
                 .then(response => {
                   if (response.status) {
                     this.data = response.data.content
                   }
                   else {
                     this.$Message.error({
                       content: response.message,
                       showIcon: true
                     })
                   }
                 })
                 .finally(() => this.loading = false)
    },
    visibleInfo(opened: boolean)
    {
      this.dataVisible = opened
      if (!opened) {
        this.handleInitialize()
      }
    },
    onChange(value: string)
    {
      this.dataInfo = this.data.find(item => item.id === toNumber(value)) as unknown as ChatModel
      this.loadingMessages = true
      ChatService.getMessages(toNumber(value))
                 .then(response => {
                   this.messages = response.data
                 })
                 .finally(() => {
                   this.loadingMessages = false
                   this.counterToken()
                   this.handlerGoBottom()
                 })
    },
    onSubmit()
    {
      this.submitting = true
      const message = {
        content: this.inputValue,
        chat: this.dataInfo,
        type: 'question'
      }
      this.messages.push(message)
      this.handlerGoBottom()
      MessageService.saveOrUpdate(message)
                    .then(response => {
                      if (response.status) {
                        this.messages.push(response.data)
                        this.inputValue = ''
                      }
                      else {
                        this.$Message.error({
                          content: response.message,
                          showIcon: true
                        })
                      }
                    })
                    .finally(() => {
                      this.submitting = false
                      this.counterToken()
                      this.handlerGoBottom()
                    })
    },
    handlerGoBottom()
    {
      const scrollElem = this.$refs.messagesContainer as any
      setTimeout(() => {
        scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' })
      }, 0)
    },
    counterToken()
    {
      const answers = this.messages.filter(message => message.type === 'answer')
      this.promptTokens = answers.reduce((sum, message) => sum + toNumber(message.promptTokens), 0)
      this.completionTokens = answers.reduce((sum, message) => sum + toNumber(message.completionTokens), 0)
      this.totalTokens = answers.reduce((sum, message) => sum + toNumber(message.totalTokens), 0)
    }
  }
})
</script>
