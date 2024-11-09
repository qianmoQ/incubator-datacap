<template>
  <ShadcnModal v-model="visible" :title="title" @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('snippet.tip.deleteAlert1')"/>
      <ShadcnAlert type="error" :title="$t('snippet.tip.deleteAlert2')"/>
      <ShadcnAlert type="error" :title="$t('snippet.tip.deleteAlert3')"/>
      <ShadcnAlert :title="$t('snippet.tip.deleteAlert4').replace('$VALUE', info?.name as string)"/>
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-error="console.log($event)" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('snippet.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('snippet.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :loading="loading">
            {{ $t('snippet.common.delete') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SnippetModel } from '@/model/snippet'
import SnippetService from '@/services/snippet'

export default defineComponent({
  name: 'SnippetDelete',
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
    },
    info: {
      type: Object as () => SnippetModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      title: null as string | null,
      formState: {
        name: ''
      }
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('snippet.common.deleteInfo').replace('$VALUE', this.info.name as string)
    }
  },
  methods: {
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        SnippetService.deleteById(Number(this.info.id))
                      .then(response => {
                        if (response.status) {
                          this.$Message.success({
                            content: this.$t('snippet.tip.deleteSuccess').replace('$VALUE', this.info?.name as string),
                            showIcon: true
                          })
                          this.onCancel()
                        }
                        else {
                          this.$Message.error({
                            content: response.message,
                            showIcon: true
                          })
                        }
                      })
                      .finally(() => this.loading = false)
      }
    },
    onCancel()
    {
      this.visible = false
    },
    validateMatch(value: string)
    {
      if (value !== String(this.info?.name)) {
        return Promise.reject(new Error(this.$t('snippet.validator.name.match').replace('$VALUE', String(this.info?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>