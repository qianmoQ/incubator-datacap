<template>
  <ShadcnModal v-model="visible"
               width="80%"
               :title="$t('dataset.common.visual')"
               @on-close="onCancel">

    <ShadcnAlert v-if="message" type="error" :title="message"/>

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :label="$t('common.name')"
                      :rules="[
                            { required: true, message: $t('report.validator.name.required') }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('report.placeholder.name')"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="description"
                      :label="$t('common.description')">
        <ShadcnInput v-model="formState.description"
                     type="textarea"
                     name="description"
                     :placeholder="$t('report.placeholder.description')"/>
      </ShadcnFormItem>

      <ShadcnFormItem>
        <VisualEditor :loading="loading" :configuration="configuration as any" @commitOptions="onCommitOptions"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit :disabled="published" :loading="published">
            {{ $t('common.publish') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import { Configuration } from '@/views/components/visual/Configuration.ts'
import { GridConfigure } from '@/views/components/grid/GridConfigure.ts'
import router from '@/router'
import ReportService from '@/services/report'

export default defineComponent({
  name: 'GridVisual',
  components: { VisualEditor },
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
      type: Boolean,
      default: false
    },
    configure: {
      type: Object as () => GridConfigure,
      default: () => null
    }
  },
  data()
  {
    return {
      loading: false,
      configuration: null as Configuration | null,
      published: false,
      message: null as string | null,
      formState: {
        name: '',
        realtime: true,
        type: 'QUERY',
        configure: null as string | null,
        query: this.configure?.query,
        description: '',
        source: {
          id: this.configure?.sourceId
        }
      }
    }
  },
  created()
  {
    if (this.configure) {
      this.configuration = new Configuration()
      this.configuration.headers = this.configure?.headers as never[]
      this.configuration.columns = this.configure?.columns as never[]
    }
  },
  methods: {
    onCommitOptions(value: any)
    {
      this.formState.configure = JSON.stringify(value)
    },
    onSubmit()
    {
      this.validator()
      if (!this.message) {
        this.published = true
        ReportService.saveOrUpdate(this.formState)
                     .then(response => {
                       if (response.status) {
                         this.$Message.success({
                           content: this.$t('report.tip.publishSuccess').replace('$VALUE', this.formState.name),
                           showIcon: true
                         })
                         router.push('/admin/report')
                       }
                       else {
                         this.$Message.error({
                           content: response.message,
                           showIcon: true
                         })
                       }
                     })
                     .finally(() => this.published = false)
      }
    },
    onCancel()
    {
      this.visible = false
    },
    validator()
    {
      if (!this.formState.name) {
        this.message = this.$t('report.validator.name').toString()
      }
      else {
        this.message = null
      }
    }
  }
})
</script>
