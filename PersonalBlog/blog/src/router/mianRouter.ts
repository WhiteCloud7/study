import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import index from '@/views/BlogIndex.vue'

import indexRouter from '@/router/index'
import projectRouter from '@/router/project'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: index
    },
    ...indexRouter,
    ...projectRouter
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
