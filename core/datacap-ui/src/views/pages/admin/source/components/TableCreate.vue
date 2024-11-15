<template>
  <ShadcnModal v-model="visible"
               height="80%"
               width="40%"
               :title="$t('source.common.menuNewTable')"
               @on-close="onCancel">
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnRow gutter="16">
        <ShadcnCol span="6">
          <ShadcnFormItem name="name"
                          :label="$t('source.common.tableName')"
                          :rules="[
                              { required: true, message: $t('source.validator.tableName.required') },
                              { pattern: /^[A-Za-z][A-Za-z0-9_-]*$/, message: $t('source.validator.tableName.pattern') }
                          ]">
            <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('source.placeholder.tableName')"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnFormItem name="engine"
                          :label="$t('source.common.engine')"
                          :rules="[{ required: true, message: $t('source.validator.tableEngine.required') }]">
            <ShadcnInput v-model="formState.engine" name="engine" :placeholder="$t('source.placeholder.tableEngine')"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="12">
          <ShadcnFormItem name="comment"
                          :label="$t('source.common.comment')">
            <ShadcnInput v-model="formState.comment"
                         name="comment"
                         type="textarea"
                         :placeholder="$t('source.placeholder.tableComment')"/>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow gutter="16">
        <ShadcnButton circle size="small" @click="onAdd">
          <ShadcnIcon icon="Plus" size="15"/>
        </ShadcnButton>

        <ShadcnCol v-for="(item, index) in formState.columns" span="12">
          <ShadcnDivider orientation="left">{{ item.name }}</ShadcnDivider>

          <ShadcnButton circle
                        size="small"
                        type="error"
                        :disabled="item.removed"
                        @click="onRemove(index)">
            <ShadcnIcon icon="Minus" size="15"/>
          </ShadcnButton>

          <ShadcnRow gutter="16">
            <ShadcnCol span="6">
              <ShadcnFormItem :name="`columns[${index}].name`"
                              :label="`${$t('source.common.columnName')} ${index + 1}`"
                              :rules="[
                                  { required: true, message: $t('source.validator.columnName.required') },
                                  { pattern: /^[A-Za-z][A-Za-z0-9_-]*$/, message: $t('source.validator.columnName.pattern') }
                              ]">
                <ShadcnInput v-model="formState.columns[index].name" :placeholder="$t('source.placeholder.columnName')" :name="`columns[${index}].name`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="6">
              <ShadcnFormItem :name="`columns[${index}].type`"
                              :label="$t('source.common.columnType')"
                              :rules="[{ required: true, message: $t('source.validator.columnType.required') }]">
                <ShadcnInput v-model="formState.columns[index].type" :placeholder="$t('source.placeholder.columnType')" :name="`columns[${index}].type`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="6">
              <ShadcnFormItem :name="`columns[${index}].length`"
                              :label="$t('source.common.columnLength')"
                              :rules="[
                                  { required: true, message: $t('source.validator.columnLength.required') },
                                  { min: 1, message: $t('source.validator.columnLength.min') }
                              ]">
                <ShadcnNumber v-model="formState.columns[index].length"
                              :placeholder="$t('source.placeholder.columnLength')"
                              :name="`columns[${index}].length`"
                              :min="1"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="6">
              <ShadcnFormItem :name="`columns[${index}].defaultValue`" :label="$t('source.common.columnDefaultValue')">
                <ShadcnInput v-model="formState.columns[index].defaultValue" :placeholder="$t('source.placeholder.columnDefaultValue')" :name="`columns[${index}].defaultValue`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="4">
              <ShadcnFormItem :name="`columns[${index}].primaryKey`" :label="$t('source.common.columnPrimaryKey')">
                <ShadcnSwitch v-model="formState.columns[index].primaryKey" :placeholder="$t('source.placeholder.columnPrimaryKey')" :name="`columns[${index}].primaryKey`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="4">
              <ShadcnFormItem :name="`columns[${index}].autoIncrement`" :label="$t('source.common.columnAutoIncrement')">
                <ShadcnSwitch v-model="formState.columns[index].autoIncrement" :placeholder="$t('source.placeholder.columnAutoIncrement')"
                              :name="`columns[${index}].autoIncrement`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="4">
              <ShadcnFormItem :name="`columns[${index}].isNullable`" :label="$t('source.common.columnIsNullable')">
                <ShadcnSwitch v-model="formState.columns[index].isNullable" :placeholder="$t('source.placeholder.columnIsNullable')" :name="`columns[${index}].isNullable`"/>
              </ShadcnFormItem>
            </ShadcnCol>

            <ShadcnCol span="12">
              <ShadcnFormItem :name="`columns[${index}].comment`" :label="$t('source.common.columnComment')">
                <ShadcnInput v-model="formState.columns[index].comment"
                             type="textarea"
                             :placeholder="$t('source.placeholder.columnComment')"
                             :name="`columns[${index}].comment`"/>
              </ShadcnFormItem>
            </ShadcnCol>
          </ShadcnRow>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">{{ $t('common.cancel') }}</ShadcnButton>

        <ShadcnButton submit :loading="loading" :disabled="loading">
          {{ $t('common.save') }}
        </ShadcnButton>
      </ShadcnSpace>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { StructureModel } from '@/model/structure'
import { TableModel, TableRequest } from '@/model/table'
import TableService from '@/services/table'
import { ColumnRequest } from '@/model/column'

export default defineComponent({
  name: 'TableCreate',
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
      type: Object as () => StructureModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      formState: null as unknown as TableModel
    }
  },
  created()
  {
    this.formState = TableRequest.of()
    this.onAdd()
  },
  methods: {
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        TableService.createTable(Number(this.info.databaseId), this.formState)
                    .then(response => {
                      if (response.status) {
                        if (response.data.isSuccessful) {
                          this.$Message.success({
                            content: this.$t('source.tip.createTableSuccess').replace('$VALUE', String(this.formState.name)),
                            showIcon: true
                          })

                          this.onCancel()
                        }
                        else {
                          this.$Message.error({
                            content: response.data.message,
                            showIcon: true
                          })
                        }
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
    onAdd()
    {
      if (this.formState.columns) {
        const newColumn = ColumnRequest.of()
        if (this.formState.columns.length === 0) {
          newColumn.removed = true
        }
        this.formState.columns.push(newColumn)
      }
    },
    onRemove(index: number)
    {
      if (this.formState.columns) {
        this.formState.columns.splice(index, 1)
      }
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
