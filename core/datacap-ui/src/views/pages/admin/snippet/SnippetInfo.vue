<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <ShadcnSpin v-if="loading" fixed/>

    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :label="$t('common.name')"
                      :rules="[
                          { required: true, message: $t('common.name') }
                      ]">
        <ShadcnInput v-model="formState.name" name="name"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="description" :label="$t('common.description')">
        <ShadcnInput v-model="formState.description" name="description" type="textarea"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="content" :label="$t('common.content')">
        <AceEditor :value="formState.context" name="content" @update:value="formState.context = $event"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="error" @click="onCancel()">
            {{ $t('common.cancel') }}
          </ShadcnButton>
          <ShadcnButton submit :loading="loading" :disabled="loading">
            {{ $t('common.save') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SnippetModel, SnippetRequest } from '@/model/snippet'
import { cloneDeep } from 'lodash'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import SnippetService from '@/services/snippet'

export default defineComponent({
  name: 'SnippetInfo',
  components: { AceEditor },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => SnippetModel | null
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
      formState: null as unknown as SnippetModel,
      title: null as string | null
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.title = this.$t('snippet.common.create')
      if (this.info) {
        this.formState = cloneDeep(this.info)
        if (this.info.id) {
          this.title = `${ this.$t('snippet.common.modify').replace('$VALUE', this.info.name as string) }`
        }
      }
      else {
        this.formState = SnippetRequest.of()
      }
    },
    onSubmit()
    {
      this.loading = true
      SnippetService.saveOrUpdate(this.formState)
                    .then((response) => {
                      if (response.status) {
                        this.$Message.success({
                          content: this.$t('snippet.tip.createSuccess').replace('$VALUE', this.formState.name as string),
                          showIcon: true
                        })
                        this.onCancel()
                      }
                    })
                    .finally(() => this.loading = false)
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
