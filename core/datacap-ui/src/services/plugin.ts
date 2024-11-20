import { ResponseModel } from '@/model/response'
import { HttpUtils } from '@/utils/http'

const DEFAULT_PATH = '/api/v1/plugin'

class PluginService
{
    getPlugins(): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH }`)
    }

    filterByType(type: string): Promise<ResponseModel>
    {
        return new HttpUtils().get(`${ DEFAULT_PATH }/filter`, { type })
    }
}

export default new PluginService()
