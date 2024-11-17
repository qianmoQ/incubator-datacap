<template>
  <ShadcnModal v-model="visible" :title="$t('common.chat')">

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :label="$t('common.name')"
                      :rules="[ { required: true, message: $t('common.name') } ]">
        <ShadcnInput v-model="formState.name" name="name"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="avatar" :label="$t('common.avatar')">
        <ShadcnInput v-model="formState.avatar" name="avatar"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="description" :label="$t('common.description')">
        <ShadcnInput v-model="formState.description" type="textarea" name="description"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit :disabled="loading" :loading="loading">
            {{ $t('common.save') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>

  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ChatService from '@/services/chat.ts'
import { ChatModel, ChatRequest } from '@/model/chat.ts'

export default defineComponent({
  name: 'ChatInfo',
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
  props: {
    isVisible: {
      type: Boolean
    }
  },
  data()
  {
    return {
      loading: false,
      formState: null as unknown as ChatModel
    }
  },
  created()
  {
    this.formState = ChatRequest.of()
  },
  methods: {
    onSubmit()
    {
      if (this.formState) {
        this.loading = true
        ChatService.saveOrUpdate(this.formState)
                   .then(response => {
                     if (response.status) {
                       this.$Message.success({
                         content: this.$t('common.success'),
                         showIcon: true
                       })

                       this.onCancel()
                     }
                     else {
                       this.$Message.error(
                           {
                             content: response.message,
                             showIcon: true
                           }
                       )
                     }
                   })
                   .finally(() => this.loading = false)
      }
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
