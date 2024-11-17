<template>
  <div v-for="state in states" class="p-2">
    <ShadcnAlert v-if="String(state) === 'START'">
      <span>{{ $t('dataset.common.stateOfStarted') }}</span>
      <span>{{ $t('dataset.complete') }}</span>
    </ShadcnAlert>

    <ShadcnAlert v-else-if="String(state).startsWith('METADATA_')">
      <span>{{ $t('dataset.common.stateOfMetadata') }}</span>
      <span v-if="String(state).endsWith('SUCCESS')">
        {{ $t('dataset.common.complete') }}
      </span>
      <span v-else-if="String(state).endsWith('FAILED')">
        {{ $t('dataset.common.failed') }}
      </span>
    </ShadcnAlert>

    <ShadcnAlert v-else-if="String(state).startsWith('TABLE_')">
      <span>{{ $t('dataset.common.stateOfCreateTable') }}</span>
      <span v-if="String(state).endsWith('SUCCESS')">
        {{ $t('dataset.common.complete') }}
      </span>
      <span v-else-if="String(state).endsWith('FAILED')">
        {{ $t('dataset.common.failed') }}
      </span>
    </ShadcnAlert>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'DatasetState',
  props: {
    states: {
      type: Array
    }
  }
})
</script>
