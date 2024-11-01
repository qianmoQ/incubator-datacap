<template>
  <Loader2 v-if="show" class="w-full justify-center animate-spin mt-10"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'


export default defineComponent({
  name: 'CircularLoading',
  props: {
    show: {
      type: Boolean,
      default: () => false
    }
  }
})
</script>
