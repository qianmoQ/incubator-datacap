<template>
  <div class="relative">
    <ShadcnSpin v-model="loading" fixed/>
    <ShadcnForm v-model="formState" v-if="formState">
      <ShadcnFormItem name="avatar"
                      class="w-[40%]"
                      :label="$t('user.common.avatar')"
                      :description="$t('user.tip.avatar')">
        <div class="flex flex-row items-center justify-between">
          <div>
            <ShadcnAvatar :src="formState.avatarConfigure?.path as string" :alt="formState.username"/>
          </div>
          <div class="w-32 space-y-2 items-center">
            <ShadcnAvatar v-if="inputFileBase64" :src="inputFileBase64 as string"/>
            <div class="space-x-1">
              <input type="file" id="fileInput" class="hidden" @change="handleFileChange"/>
              <label v-if="!inputFile" for="fileInput" class="cursor-pointer">
                <ShadcnTooltip :content="$t('common.file')">
                  <ShadcnAvatar size="small"/>
                </ShadcnTooltip>
              </label>
              <div v-else class="space-x-1">
                <ShadcnButton :loading="uploading" :disabled="uploading" size="icon" class="w-6 h-6" @click="handlerUpload">
                  <ShadcnIcon icon="Upload" v-if="!uploading" size="15"/>
                </ShadcnButton>
                <ShadcnButton :disabled="uploading" size="icon" variant="destructive" class="w-6 h-6" @click="inputFile = null">
                  <ShadcnIcon icon="Trash" size="15"/>
                </ShadcnButton>
              </div>
            </div>
          </div>
        </div>
      </ShadcnFormItem>

      <ShadcnFormItem name="username"
                      class="w-[40%]"
                      :label="$t('user.common.username')"
                      :description="$t('user.tip.username')">
        <ShadcnInput v-model="formState.username" disabled/>
      </ShadcnFormItem>

      <ShadcnFormItem name="createTime"
                      class="w-[40%]"
                      :label="$t('user.common.createTime')"
                      :description="$t('user.tip.createTime')">
        <ShadcnInput v-model="formState.createTime" disabled/>
      </ShadcnFormItem>
    </ShadcnForm>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UserService from '@/services/user'
import { UserModel } from '@/model/user'
import Common from '@/utils/common'

export default defineComponent({
  name: 'ProfileForm',
  data()
  {
    return {
      loading: false,
      formState: null as UserModel | null,
      inputFile: null as any,
      inputFileBase64: null as string | null,
      uploading: false
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
      UserService.getInfo()
                 .then(response => {
                   if (response.status) {
                     this.formState = response.data
                   }
                 })
                 .finally(() => this.loading = false)
    },
    handleFileChange(event: Event)
    {
      const input = event.target as HTMLInputElement
      if (input.files && input.files.length > 0) {
        this.inputFile = input.files[0]
        Common.fileToBase64(this.inputFile)
              .then(response => {
                this.inputFileBase64 = response
              })
      }
    },
    handlerUpload()
    {
      if (this.inputFile) {
        this.uploading = true
        const formData = new FormData()
        formData.append('file', this.inputFile)
        UserService.uploadAvatar(formData)
                   .then(response => {
                     if (response.status) {
                       this.inputFile = null
                       this.inputFileBase64 = null
                       this.handlerInitialize()
                     }
                   })
                   .finally(() => this.uploading = false)
      }
    }
  }
})
</script>
