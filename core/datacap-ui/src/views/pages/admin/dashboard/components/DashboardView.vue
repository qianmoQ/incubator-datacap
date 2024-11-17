<template>
  <div class="relative">
    <GridLayout ref="refLayout"
                :layout="layouts"
                :responsive="true"
                :col-num="12"
                :row-height="60"
                :is-draggable="false"
                :is-resizable="false"
                :vertical-compact="true"
                :use-css-transforms="true">
      <GridItem v-for="item in layouts"
                :key="item.i"
                :x="item.x"
                :y="item.y"
                :w="item.w"
                :h="item.h"
                :i="item.i"
                :min-h="3"
                :is-resizable="false"
                :min-w="3">
        <ShadcnCard class="h-full w-full">
          <template #title>{{ item.title ? item.title : $t('dataset.common.notSpecifiedTitle') }}</template>

          <template #extra>
            <ShadcnTooltip v-if="item.description" :content="item.description">
              <ShadcnButton circle size="small" type="error">
                <ShadcnIcon icon="Trash" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>
          </template>

          <VisualView v-if="item.original"
                      :width="calculateWidth(item)"
                      :height="calculateHeight(item)"
                      :code="item.node.code"
                      :configuration="JSON.parse(item.node.configure)"
                      :type="item.original?.type"
                      :query="item.original.type === 'DATASET' ? JSON.parse(item.original.query as string) : item.original.query"
                      :original="item?.original?.source?.id"/>

          <VisualView v-else
                      :width="calculateWidth(item)"
                      :height="calculateHeight(item)"
                      :code="item.node.code"
                      :configuration="JSON.parse(item.node.configure)"
                      :query="JSON.parse(item.node.query)"/>
        </ShadcnCard>
      </GridItem>
    </GridLayout>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { GridItem, GridLayout } from 'vue3-grid-layout-next'
import VisualView from '@/views/components/visual/VisualView.vue'

export default defineComponent({
  name: 'DashboardView',
  components: { VisualView, GridItem, GridLayout },
  props: {
    layouts: {
      type: Array as () => any[],
      default: () => []
    }
  },
  data()
  {
    return {
      columnNumber: 12,
      rowHeight: 70
    }
  },
  methods: {
    // The method of calculating the width
    calculateWidth(item: any): string
    {
      const widthPercentage = (item.w * (100 / this.columnNumber))
      return `calc(${ widthPercentage }% - rem)` // Subtract margins
    },
    // How to calculate the height
    calculateHeight(item: any): string
    {
      const totalHeight = item.h * this.rowHeight
      return `${ totalHeight - 48 }px` // Subtract the height of the card head
    }
  }
})
</script>
