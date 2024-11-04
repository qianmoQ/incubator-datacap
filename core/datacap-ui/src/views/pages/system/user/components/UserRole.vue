<template>
  <ShadcnModal v-model="visible" :title="title" @on-close="onClose">
    <template #content>
      <div class="relative">
        <ShadcnSpin v-model="loading" fixed/>
        <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit" @on-error="onError">
          <ShadcnFormItem name="roles"
                          :rules="[
                              { required: true, message: 'Please check the role' },
                          ]">
            <ShadcnCheckboxGroup v-model="formState.roles" name="roles">
              <ShadcnCheckbox v-for="item in data" :value="item.id">{{ item.name }}</ShadcnCheckbox>
            </ShadcnCheckboxGroup>
          </ShadcnFormItem>

          <div class="flex justify-end">
            <ShadcnButton submit :loading="submitted" :disabled="submitted">
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
import { StringUtils } from '@/utils/string'
import { UserModel, UserRoleModel } from '@/model/user'
import { FilterModel } from '@/model/filter'
import RoleService from '@/services/role'
import UserService from '@/services/user'
import { RoleModel } from '@/model/role'

export default defineComponent({
  name: 'UserRole',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => UserModel | null,
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
      submitted: false,
      title: null as string | null,
      data: [] as RoleModel[],
      formState: null as UserRoleModel | null
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
        this.title = `${ StringUtils.replace(this.$t('role.common.assignRole'), '$NAME', this.info.username as string) }`
        this.formState = {
          roles: this.info.roles?.map(v => v.id) as number[],
          userId: this.info.id as number
        }
        this.loading = true
        const filter: FilterModel = new FilterModel()
        filter.size = 1000
        RoleService.getAll(filter)
                   .then((response) => {
                     if (response.status) {
                       this.data = response.data.content
                     }
                   })
                   .finally(() => this.loading = false)
      }
    },
    onClose()
    {
      this.visible = false
    },
    onSubmit()
    {
      if (this.info) {
        this.submitted = true
        UserService.assignRole(this.formState as UserRoleModel)
                   .then((response) => {
                     if (response.status) {
                       this.$Message.success({
                         content: this.$t('user.tip.assignRoleSuccess'),
                         showIcon: true
                       })
                       this.onClose()
                     }
                     else {
                       this.$Message.error({
                         content: response.message,
                         showIcon: true
                       })
                     }
                   })
                   .finally(() => this.submitted = false)
      }
      else {
        this.$Message.error({
          content: this.$t('common.invalidParam'),
          showIcon: true
        })
      }
    },
    onError(errors: any)
    {
      this.$Message.error({
        content: `Validation error field: [ ${ Object.keys(errors).join(', ') } ]`,
        showIcon: true
      })
    }
  }
})
</script>
