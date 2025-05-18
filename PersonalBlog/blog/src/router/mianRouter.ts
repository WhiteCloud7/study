import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import index from '@/views/BlogIndex.vue'
import indexRouter from '@/router/index'
import projectRouter from '@/router/project'
import articleRouter from "@/router/article";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: index,
        meta:{showNav:true}
    },
    {
        path:'/login',
        name:'login',
        component: () => import('@/views/userLogin.vue'),
        meta:{showNav:false}
    },
    {
        path:'/register',
        name:'register',
        component: () => import('@/views/userRegister.vue'),
        meta:{showNav:false}
    },
    {
        path:'/profile',
        name:'profile',
        component: () => import('@/views/userProfile.vue'),
        meta:{showNav:true},
        children:[{
            path:'/profile',
            component: () => import('@/components/profile/personalInfo.vue'),
        },{
            path:'/profile/star',
            component: () => import('@/components/profile/personalStar.vue'),
        },{
            path:'/profile/setting',
            component: () => import('@/components/profile/profileSetting.vue'),
        }]
    },
    {
        path:'/friendProfile/:username',
        name:'friendProfile',
        component: () => import('@/components/profile/personalInfo.vue'),
        meta:{showNav:true},
        props:true
    },
    {
        path:'/userProfile/:username',
        name:'userProfile',
        component: () => import('@/components/profile/personalInfo.vue'),
        meta:{showNav:true},
        props:true
    },
    ...indexRouter,
    ...projectRouter,
    ...articleRouter
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
