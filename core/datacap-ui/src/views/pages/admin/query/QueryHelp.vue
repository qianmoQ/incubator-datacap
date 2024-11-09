<template>
  <ShadcnModal v-model="visible"
               width="40%"
               height="80%"
               :title="$t('query.common.help')"
               @on-close="onCancel">
    <ShadcnTab v-model="activeTab" @on-change="onChange">
      <ShadcnTabItem v-for="item in helpType" :value="getEnumName(item)" :label="getEnumName(item)">
        <div class="relative h-full">
          <ShadcnSpin v-model="loading" class="mt-2.5" fixed/>

          <MdPreview v-if="helpReplyContent" :modelValue="helpReplyContent" style="padding: 0; width: 100%"/>
        </div>
      </ShadcnTabItem>
    </ShadcnTab>

    <template #footer>
      <ShadcnButton type="error" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UserService from '@/services/user'
import { UserModel, UserQuestionModel } from '@/model/user'
import { isEmpty } from 'lodash'
import { HelpType } from '@/views/pages/admin/query/HelpType'
import MessageService from '@/services/message'

import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'QueryHelp',
  components: { MdPreview },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    },
    engine: {
      type: String,
      default: () => ''
    },
    message: {
      type: String,
      default: () => ''
    },
    helpType: {
      type: Array as () => Array<HelpType>,
      default: () => []
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  data()
  {
    return {
      loading: false,
      activeTab: 'ANALYSIS',
      userInfo: null as UserModel | null,
      helpReplyContent: null as string | null
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    isEmpty,
    handleInitialize()
    {
      UserService.getInfo()
                 .then(response => {
                   if (response.status) {
                     this.userInfo = response.data
                     this.onChange(this.helpType[0] as HelpType)
                   }
                 })
    },
    onChange(value: any)
    {
      this.loading = true
      const userQuestion: UserQuestionModel = {
        type: 'ChatGPT',
        content: this.content,
        transType: value,
        engine: this.engine,
        error: this.message,
        locale: this.$i18n.locale,
        newChat: true
      }
      MessageService.aiReply(userQuestion)
                    .then(response => {
                      if (response.status) {
                        this.helpReplyContent = response.data.content
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
    onCancel()
    {
      this.visible = false
    },
    getEnumName(value: HelpType): string
    {
      return HelpType[value]
    }
  }
})
</script>