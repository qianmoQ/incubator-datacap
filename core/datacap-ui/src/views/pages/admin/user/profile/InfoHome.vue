<template>
  <ShadcnCard :border="false"
              :title="$t('user.common.info')"
              :description="$t('user.tip.info')">
    <ShadcnDivider class="my-2"/>
    <ShadcnRow :gutter="16">
      <ShadcnCol :span="12">
        <ShadcnCard only-content-loading
                    :title="$t('user.common.contribution')"
                    :description="$t('user.tip.contribution')"
                    :loading="loading">
          <div class="p-2">
            <CalendarHeatmap :tooltip-unit="$t('heatmap.common.query')"
                             :end-date="heatmap.endDate"
                             :round="10"
                             :values="heatmap.data"
                             :locale="{
                                  months: [
                                            $t('heatmap.common.jan'),
                                            $t('heatmap.common.feb'),
                                            $t('heatmap.common.mar'),
                                            $t('heatmap.common.apr'),
                                            $t('heatmap.common.mai'),
                                            $t('heatmap.common.jun'),
                                            $t('heatmap.common.jul'),
                                            $t('heatmap.common.aug'),
                                            $t('heatmap.common.sep'),
                                            $t('heatmap.common.okt'),
                                            $t('heatmap.common.nov'),
                                            $t('heatmap.common.dez')
                                          ],
                                  days  : [
                                            $t('heatmap.common.so'),
                                            $t('heatmap.common.mo'),
                                            $t('heatmap.common.di'),
                                            $t('heatmap.common.mi'),
                                            $t('heatmap.common.do'),
                                            $t('heatmap.common.fr'),
                                            $t('heatmap.common.sa')
                                           ],
                                  on    : $t('heatmap.common.am'),
                                  less  : $t('heatmap.common.less'),
                                  more  : $t('heatmap.common.more')
                             }"/>
          </div>
        </ShadcnCard>
      </ShadcnCol>
      <ShadcnCol :span="12">
        <ShadcnCard only-content-loading
                    :title="$t('user.common.radar7Days')"
                    :description="$t('user.tip.radar7Days')"
                    :loading="loading">
          <div class="p-2">
            <VisualPie v-if="radar.configuration" :configuration="radar.configuration as any" :height="'200px'" :submitted="false"/>
          </div>
        </ShadcnCard>
      </ShadcnCol>
    </ShadcnRow>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { CalendarHeatmap } from 'vue3-calendar-heatmap'
import './vue3-calendar-heatmap.css'
import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'
import { Configuration } from '@/views/components/visual/Configuration'
import VisualPie from '@/views/components/visual/components/VisualPie.vue'
import { DateUtils } from '@/utils/date'

export default defineComponent({
  name: 'InfoHome',
  components: {
    VisualPie,
    CalendarHeatmap
  },
  data()
  {
    return {
      loading: false,
      heatmap: {
        data: [],
        endDate: null as string | null
      },
      radar: {
        configuration: null as Configuration | null
      }
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
      const axios = new HttpUtils().getAxios()
      axios.all([UserService.getUserContribution(), UserService.getUserContributionRadar()])
           .then(axios.spread((fetchContribution, fetchRadar) => {
             if (fetchContribution.status) {
               this.heatmap.data = fetchContribution.data
               if (fetchContribution.data.length > 0) {
                 if (this.heatmap.data.length > 0) {
                   const item = this.heatmap.data[this.heatmap.data.length - 1] as any
                   this.heatmap.endDate = item.date
                 }
               }
               else {
                 const now = new Date()
                 this.heatmap.endDate = DateUtils.formatTime(now, 'YYYY-MM-DD')
               }
             }
             if (fetchRadar.status) {
               const configuration = new Configuration()
               configuration.columns = fetchRadar.data
               configuration.chartConfigure = { yAxis: 'count', xAxis: 'label', outerRadius: [1.2] }
               this.radar.configuration = configuration
             }
           }))
           .finally(() => this.loading = false)
    }
  }
})
</script>
