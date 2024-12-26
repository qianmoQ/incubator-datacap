import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { createAuthRoute } from '@/router/auth'
import { createDefaultRouter } from '@/router/default'
import { createHttpRoute } from '@/router/http'
// @ts-ignore
import { LoadingBar } from 'view-shadcn-ui'

LoadingBar.enabledNetwork()

const routes: Array<RouteRecordRaw> = []

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

createHttpRoute(router)
createAuthRoute(router)
createDefaultRouter(router)

router.beforeEach((_to, _from, _next) => {
    LoadingBar.start()
    if (_to.matched.length === 0) {
        _next('/common/404')
    }
    else {
        _next()
    }
})

router.afterEach(() => LoadingBar.done())

export default router
