<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-if="loading" fixed/>

    <ShadcnTable size="small" :columns="headers" :data="data">
      <template #isNullable="{ row }">
        <ShadcnSwitch v-model="row.isNullable" size="small" disabled/>
      </template>
    </ShadcnTable>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'
import { createHeaders } from '@/views/pages/admin/source/components/TableUtils.ts'
import ColumnService from '@/services/column.ts'
import { ColumnModel } from '@/model/column.ts'

const { t } = useI18n()
const route = useRoute()

const headers = createHeaders({ t })

const loading = ref(false)
const data = ref<Array<ColumnModel>>([])

const handleInitialize = () => {
  const code = String(route.params.table)
  if (code) {
    loading.value = true
    ColumnService.getAllByTable(code)
                 .then(response => {
                   if (response.status) {
                     data.value = response.data
                   }
                 })
                 .finally(() => loading.value = false)
  }
}

watch(
    () => route.params.table,
    () => handleInitialize()
)

onMounted(() => handleInitialize())
</script>
