<template>
  <ShadcnModal v-model="visible" :title="title" @on-close="onCancel">
    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTree v-model="menus" cascade checkable :data="data"/>

      <div class="flex justify-end">
        <ShadcnButton :loading="saving" :disabled="saving" @click="onSubmit()">
          {{ title }}
        </ShadcnButton>
      </div>
    </div>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { RoleModel } from '@/model/role'
import UserService from '@/services/user'
import RoleService from '@/services/role'
import CommonUtils from '@/utils/common'

export default defineComponent({
  name: 'RoleMenu',
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
      saving: false,
      title: null as string | null,
      data: [] as any[],
      menus: [] as any[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      if (this.info) {
        this.title = this.$t('role.common.assignMenu').replace('$NAME', this.info.name as string)
        RoleService.getAllMenuById(this.info?.id as number)
                   .then((response) => {
                     if (response.status) {
                       const data = response.data
                       this.data = this.handleData(data)
                       this.menus = this.extractSelectedItems(data)
                                        .map(item => item.id)
                     }
                   })
                   .finally(() => this.loading = false)
      }
    },
    handleData(data: any[])
    {
      return data.map(item => {
        const node = {
          value: item.id,
          label: item.title,
          children: []
        }
        if (item.children) {
          node.children = this.handleData(item.children)
        }
        return node
      })
    },
    extractSelectedItems(data: any)
    {
      let selectedItems = [] as any[]
      data.forEach((item: { selected: boolean; children: any[] }) => {
        if (item.selected) {
          selectedItems.push(item)
        }
        if (item.children) {
          const childrenSelected = this.extractSelectedItems(item.children)
          selectedItems = selectedItems.concat(childrenSelected)
        }
      })
      return selectedItems
    },
    onSubmit()
    {
      this.saving = true
      RoleService.saveMenu(this.info?.id as number, this.menus)
                 .then((response) => {
                   if (response.status) {
                     UserService.getMenus()
                                .then(response => {
                                  if (response.status) {
                                    localStorage.setItem(CommonUtils.menu, JSON.stringify(response.data))
                                  }
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