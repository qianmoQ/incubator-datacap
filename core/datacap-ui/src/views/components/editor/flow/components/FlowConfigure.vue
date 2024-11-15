<template>
  <div>
    <ShadcnDrawer v-model="visible"
                  width="25%"
                  :title="`[ ${ data?.name } ] ${ $t('common.configure') }`"
                  @close="onCancel">

      <ShadcnForm v-model="formState">
        <div v-if="data" v-for="item in data.configure">

          <ShadcnFormItem v-if="item.input"
                          :key="item.field"
                          :name="item.field"
                          :label="item.field"
                          :description="item.description">
            <div v-if="item.type === 'INPUT'" class="flex flex-row items-start font-normal space-x-1">
              <ShadcnInput v-model="item.value" :disabled="item.override && item.tooltip"/>
              <ShadcnTooltip v-if="item.tooltip" :content="item.tooltip">
                <ShadcnCheckbox v-model="item.override" :value="true"/>
              </ShadcnTooltip>
            </div>

            <div v-if="item.type === 'TEXT'" class="flex flex-row items-start font-normal space-x-1">
              <ShadcnInput v-model="item.value" type="textarea" :disabled="item.override && item.tooltip"/>
              <ShadcnTooltip v-if="item.tooltip" :content="item.tooltip">
                <ShadcnCheckbox v-model="item.override" value="True"/>
              </ShadcnTooltip>
            </div>

            <div v-if="item.type === 'SWITCH'" class="flex flex-row items-start font-normal space-x-1">
              <ShadcnSwitch v-model="item.value" :disabled="item.override && item.tooltip"/>
              <ShadcnTooltip v-if="item.tooltip" :content="item.tooltip">
                <ShadcnCheckbox v-model="item.override" class="ml-2"/>
              </ShadcnTooltip>
            </div>

            <div v-if="item.type === 'SELECT'" class="flex flex-row items-start font-normal space-x-1">
              <ShadcnSelect v-model="item.value" :disabled="item.override && item.tooltip">
                <template #options>
                  <ShadcnSelectOption v-for="option in item.defaultValues" :value="option" :label="option"/>
                </template>
              </ShadcnSelect>
              <ShadcnTooltip v-if="item.tooltip" :content="item.tooltip">
                <ShadcnCheckbox v-model="item.override" class="ml-2"/>
              </ShadcnTooltip>
            </div>

            <div v-if="item.type === 'NUMBER'" class="flex flex-row items-start font-normal space-x-1">
              <ShadcnNumber v-model="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}"/>
              <ShadcnTooltip v-if="item.tooltip" :content="item.tooltip">
                <ShadcnCheckbox v-model="item.override" class="ml-2"/>
              </ShadcnTooltip>
            </div>
          </ShadcnFormItem>
        </div>
      </ShadcnForm>

      <template #footer>
        <ShadcnButton @click="onSubmit()">
          <ShadcnIcon icon="Save"/>
          <span>{{ $t('common.save') }}</span>
        </ShadcnButton>
      </template>
    </ShadcnDrawer>
  </div>
</template>
<script lang="ts">
import { defineComponent, ref } from 'vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'

export default defineComponent({
  name: 'FlowConfigure',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => Configuration | null
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
  setup(props)
  {
    const formState = ref({
      items: props.data?.configure || []
    })

    return {
      formState
    }
  },
  methods: {
    onSubmit()
    {
      if (this.data) {
        const configure = {
          id: this.data.id,
          protocol: this.data.protocol,
          configures: this.filterConfigure(this.formState.items),
          type: this.data.nodeType
        }
        this.$emit('onChange', configure)
      }
    },
    onCancel()
    {
      this.visible = false
    },
    filterConfigure(meta: any)
    {
      const result = {} as any
      meta.filter((item: { input: any; }) => item.input)
          .forEach((item: { origin: any; value: any; }) => {
            if (item.value !== null) {
              result[item.origin] = item.value
            }
          })
      return result
    }
  }
})
</script>
