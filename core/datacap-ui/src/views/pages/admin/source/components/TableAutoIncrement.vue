<template>
  <ShadcnModal v-model="localVisible"
               :title="$t('source.common.resetAutoIncrement')"
               :mask-closable="false"
               @on-close="onCancel">
    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="autoIncrement" :label="$t('source.common.resetTo')">
        <ShadcnNumber v-model="formState.autoIncrement"
                      name="autoIncrement"
                      :min="1"
                      :placeholder="$t('snippet.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="primary" :loading="loading">
            {{ $t('common.apply') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref } from 'vue'
import { SqlType, TableFilter } from '@/model/table.ts'
import TableService from '@/services/table.ts'

const emit = defineEmits(['close'])
const { proxy } = getCurrentInstance()!

const props = withDefaults(defineProps<{
  visible: boolean
  info?: any
}>(), {
  visible: false
})

const localVisible = ref(props.visible)
const loading = ref(false)
const formState = ref({ autoIncrement: props.info?.autoIncrement })

const onCancel = () => emit('close')

const onSubmit = () => {
  if (props.info) {
    loading.value = true
    const configure: TableFilter = {
      type: SqlType.ALTER,
      value: formState.value.autoIncrement
    }
    TableService.getData(props.info.code, configure)
                .then(response => {
                  if (response.status) {
                    // @ts-ignore
                    proxy.$Message.success({
                      content: proxy.$t('source.tip.resetAutoIncrementSuccess').replace('$VALUE', String(formState.value.autoIncrement)),
                      showIcon: true
                    })

                    onCancel()
                  }
                  else {
                    // @ts-ignore
                    proxy.$Message.error({
                      content: response.message,
                      showIcon: true
                    })
                  }
                })
                .finally(() => loading.value = false)
  }
}
</script>
