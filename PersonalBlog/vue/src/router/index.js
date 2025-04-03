import { createRouter, createWebHistory } from 'vue-router';
import MyTest from '../components/MyTest.vue';
import MyTest2 from '../components/MyTest2.vue';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: MyTest
    },
    {
        path: '/about',
        name: 'About',
        component: MyTest2
    },
    {
        path:'/form',
        component: ()=> import("@/components/myForm")
    },
    {
        path:'/updateForm',
        component: ()=> import("@/components/updateForm")
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;