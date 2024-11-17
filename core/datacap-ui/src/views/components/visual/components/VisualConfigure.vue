<template>
  <ShadcnForm v-model="formState" class="mt-2" @on-submit="onSubmit">
    <ShadcnDrawer v-model="visible"
                  width="40%"
                  style="margin-right: -1rem;"
                  :title="$t('common.configure')"
                  @close="onCancel">

      <ShadcnTab v-model="activeGroup" direction="vertical" position="right">
        <ShadcnTabItem v-for="group in fieldGroup"
                       class="space-y-4"
                       :name="group.label"
                       :label="group.label"
                       :key="group.label"
                       :value="group.label">
          <ShadcnFormItem v-for="item in fieldGroup.find(value => value.label === activeGroup)?.fields"
                          :name="item.field"
                          :key="item.field"
                          :label="item.label">
            <ShadcnSwitch v-if="item.type === 'SWITCH'" v-model="formState[Object(item.field)]"/>

            <ShadcnSlider v-else-if="item.type === 'SLIDER'" v-model="formState[Object(item.field)]"
                          :min="item.min"
                          :max="item.max"
                          :step="item.step"/>

            <ShadcnInput v-else-if="item.type === 'TEXT'"
                         v-model="formState[Object(item.field)]"
                         :placeholder="item.label"
                         :disabled="item.disabled?.field ? formState[Object(item.disabled?.field)] === item.disabled?.value : false"/>

            <ShadcnSelect v-else v-model="formState[Object(item.field)]"
                          :disabled="item.disabled?.field ? formState[Object(item.disabled?.field)] === item.disabled?.value : false">
              <template #options>
                <ShadcnSelectOption v-if="item.values"
                                    v-for="data in item.values"
                                    :value="data.value"
                                    :label="data.label"/>

                <ShadcnSelectOption v-else
                                    v-for="item in configuration.headers"
                                    :value="item"
                                    :label="item"/>
              </template>
            </ShadcnSelect>
          </ShadcnFormItem>
        </ShadcnTabItem>
      </ShadcnTab>

      <template #footer>
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>
          <ShadcnButton submit>
            {{ $t('common.apply') }}
          </ShadcnButton>
        </ShadcnSpace>
      </template>
    </ShadcnDrawer>
  </ShadcnForm>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ChartFieldGroup, Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'

export default defineComponent({
  name: 'VisualConfigure',
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
    configuration: {
      type: Object as () => Configuration
    },
    fieldGroup: {
      type: Array as () => ChartFieldGroup[],
      default: []
    },
    isVisible: {
      type: Boolean
    }
  },
  data()
  {
    return {
      activeGroup: this.fieldGroup[0]?.label,
      formState: null as IChart | null
    }
  },
  created()
  {
    if (this.configuration && keys(this.configuration.chartConfigure).length > 0) {
      this.formState = cloneDeep(this.configuration.chartConfigure) as IChart
    }
    else {
      const obj = {} as any
      this.fieldGroup.forEach(group => {
        group?.fields?.forEach(field => {
          if (field.field) {
            obj[field.field] = undefined
          }
        })
      })
      this.formState = obj
    }
  },
  methods: {
    onCancel()
    {
      this.visible = false
    },
    onSubmit()
    {
      this.$emit('change', this.formState)
      this.onCancel()
    }
  }
})
</script>
