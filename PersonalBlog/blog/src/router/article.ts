export default [
    {
        path: '/article/:articleId',
        name: 'articleDetail',
        component: () => import("@/components/article/articleDetail.vue"),
        meta: { showNav: false },
        props: true
    },
    {
        path: '/article/edit/:articleId',
        name: 'editArticle',
        component: () => import("@/components/article/editArticle.vue"),
        meta: { showNav: false },
        props: true
    },
    {
        path: '/article/add',
        name: 'newArticle',
        component: () => import("@/components/article/editArticle.vue"),
        meta: { showNav: false },
        props: true
    }
]