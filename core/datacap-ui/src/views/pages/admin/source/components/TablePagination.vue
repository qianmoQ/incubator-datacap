<template>
  <ShadcnModal v-model="visible"
               :title="$t('source.common.jumpPage')"
               @on-close="onCancel">
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="showPageSize" :label="$t('source.common.showPageSize')">
        <ShadcnNumber v-model="formState.pageSize"
                      min="1"
                      :max="10000"
                      name="pageSize"/>
      </ShadcnFormItem>

      <ShadcnButton submit type="primary">
        {{ $t('common.apply') }}
      </ShadcnButton>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'TablePagination',
  props: {
    isVisible: {
      type: Boolean
    },
    configure: {
      type: Object
    },
    page: {
      type: Number
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
      formState: {
        pageSize: 500
      }
    }
  },
  methods: {
    onCancel()
    {
      this.visible = false
    },
    onSubmit()
    {
      this.visible = false
      this.$emit('change', this.formState)
    }
  }
})
</script>
