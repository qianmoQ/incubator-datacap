<template>
  <ShadcnDrawer v-model="visible" :title="title">
    <UserForm class="mt-3" :info="info" @close="handlerCancel"/>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { UserModel } from '@/model/user.ts'
import UserForm from '@/views/pages/system/user/components/UserForm.vue'

export default defineComponent({
  name: 'UserInfo',
  components: { UserForm },
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
      type: Object as () => UserModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('user.common.edit').replace('$VALUE', this.info.username as string)
    }
    else {
      this.title = this.$t('user.common.create')
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
