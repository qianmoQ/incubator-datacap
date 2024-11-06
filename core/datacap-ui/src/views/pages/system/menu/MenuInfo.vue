<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <ShadcnSpin v-if="loading" fixed/>
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnFormItem name="name"
                          :label="$t('common.name')"
                          :rules="[
                          { required: true, message: $t('common.name') }
                      ]">
            <ShadcnInput v-model="formState.name" name="name"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnFormItem name="code"
                          :label="$t('common.code')"
                          :rules="[
                          { required: true, message: $t('common.code') }
                      ]">
            <ShadcnInput v-model="formState.code" name="code"/>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnFormItem name="url"
                          :label="$t('common.url')"
                          :rules="[
                              { required: true, message: $t('common.url') }
                          ]">
            <ShadcnInput v-model="formState.url" name="url"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnFormItem name="group" :label="$t('common.group')">
            <ShadcnInput v-model="formState.group" name="group"/>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnFormItem name="sorted"
                          :label="$t('common.sorted')"
                          :rules="[
                              { required: true, message: $t('common.sorted') }
                          ]">
            <ShadcnNumber v-model="formState.sorted" name="sorted"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnFormItem name="type"
                          :label="$t('common.type')"
                          :rules="[
                              { required: true, message: $t('common.type') }
                          ]">
            <ShadcnSelect v-model="formState.type"
                          name="type"
                          :label="$t('common.type')"
                          :placeholder="$t('menu.tip.selectType')">
              <template #options>
                <ShadcnSelectOption label="VIEW" value="VIEW"/>
              </template>
            </ShadcnSelect>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnSelect v-model="formState.parent" :placeholder="$t('menu.tip.selectParent')" name="parent" :label="$t('common.parent')">
            <template #options>
              <ShadcnSelectOption v-for="menu in fullMenus" :label="menu.name as string" :value="menu.id as string"/>
            </template>
          </ShadcnSelect>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnSelect v-model="formState.redirect"
                        name="parent"
                        :placeholder="$t('menu.tip.selectRedirect')"
                        :label="$t('menu.common.redirect')">
            <template #options>
              <ShadcnSelectOption v-for="menu in fullMenus" :label="menu.name as string" :value="menu.id as string"/>
            </template>
          </ShadcnSelect>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnFormItem name="new" :label="$t('menu.common.new')">
            <ShadcnSwitch v-model="formState.new" name="new"/>
          </ShadcnFormItem>
        </ShadcnCol>

        <ShadcnCol span="6">
          <ShadcnFormItem name="i18nKey" :label="$t('menu.common.i18nKey')">
            <ShadcnInput v-model="formState.i18nKey" name="i18nKey"/>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnRow class="space-x-2">
        <ShadcnCol span="6">
          <ShadcnFormItem name="icon" :label="$t('menu.common.icon')">
            <ShadcnInput v-model="formState.icon" name="icon"/>
          </ShadcnFormItem>
        </ShadcnCol>
      </ShadcnRow>

      <ShadcnFormItem name="description" :label="$t('common.description')">
        <ShadcnInput v-model="formState.description" type="textarea" name="description"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnButton submit :loading="loading" :disabled="loading">
          {{ $t('common.save') }}
        </ShadcnButton>
      </div>
    </ShadcnForm>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MenuModel, MenuRequest } from '@/model/menu'
import { cloneDeep } from 'lodash'
import MenuService from '@/services/menu'
import { FilterModel } from '@/model/filter'

export default defineComponent({
  name: 'MenuInfo',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => MenuModel | null,
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
      title: null as string | null,
      formState: null as unknown as MenuModel,
      loading: false,
      fullMenus: [] as MenuModel[],
      saving: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${ this.$t('menu.common.create') }`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        this.title = `${ this.$t('menu.common.modify').replace('$NAME', this.info.name as string) }`
      }
      else {
        this.formState = MenuRequest.of()
      }

      const filter: FilterModel = new FilterModel()
      this.loading = true
      MenuService.getAll(filter)
                 .then((response) => {
                   if (response.status) {
                     this.fullMenus = response.data.content
                   }
                 })
                 .finally(() => this.loading = false)
    },
    onSubmit()
    {
      this.saving = true
      MenuService.saveOrUpdate(this.formState)
                 .then(response => {
                   if (response.status) {
                     this.$Message.success({
                       content: 'successful',
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
                 .finally(() => this.saving = false)
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
