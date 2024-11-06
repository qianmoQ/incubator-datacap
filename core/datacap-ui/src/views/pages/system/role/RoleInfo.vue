<template>
  <ShadcnModal v-model="visible" :title="title" @on-close="onCancel">

    <div class="flex items-center justify-center h-32">Content</div>

    <template #content>
      <div class="relative">
        <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
          <ShadcnFormItem name="name"
                          :label="$t('role.common.name')"
                          :rules="[
                              { required: true, message: $t('role.tip.name') },
                              { min: 2, message: $t('role.validate.nameSize') },
                              { max: 20, message: $t('role.validate.nameSize') }
                          ]">
            <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('role.tip.name')"/>
          </ShadcnFormItem>

          <ShadcnFormItem name="description"
                          :label="$t('role.common.description')"
                          :rules="[
                              { required: true, message: $t('role.tip.description') },
                              { min: 3, message: $t('role.validate.descriptionSize') },
                              { max: 50, message: $t('role.validate.descriptionSize') }
                          ]">
            <ShadcnInput v-model="formState.description"
                         type="textarea"
                         name="description"
                         :placeholder="$t('role.tip.description')"/>
          </ShadcnFormItem>

          <div class="flex justify-end">
            <ShadcnButton submit :loading="loading" :disabled="loading">
              {{ title }}
            </ShadcnButton>
          </div>
        </ShadcnForm>
      </div>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { RoleModel, RoleRequest } from '@/model/role'
import { StringUtils } from '@/utils/string'
import { cloneDeep } from 'lodash'
import RoleService from '@/services/role'

export default defineComponent({
  name: 'RoleInfo',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => RoleModel | null,
      default: null
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
      title: null as string | null,
      formState: null as unknown as RoleModel
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${ StringUtils.replace(this.$t('role.common.edit'), '$NAME', this.info.name as string) }`
        this.formState = cloneDeep(this.info)
      }
      else {
        this.title = this.$t('role.common.create')
        this.formState = RoleRequest.of()
      }
    },
    onCancel()
    {
      this.visible = false
    },
    onSubmit()
    {
      this.loading = true
      RoleService.saveOrUpdate(this.formState)
                 .then(response => {
                   if (response.status) {
                     this.$Message.success({
                       content: `${ this.title } ${ this.$t('common.successfully') }`,
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
  }
})
</script>
