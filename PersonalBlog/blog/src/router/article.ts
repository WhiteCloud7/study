export default [
    {
        path: '/article/:articleId',
        name: 'articleDetail',
        component: () => import("@/components/article/articleDetail.vue"),
        meta: { showNav: false },
        props: true
    }
]