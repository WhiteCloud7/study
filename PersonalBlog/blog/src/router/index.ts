import index from '@/views/BlogIndex.vue'
import project from '@/views/MyProject.vue'
import article from '@/views/MyArticle.vue'
import resume from '@/views/PersonalResume.vue'
import contact from '@/views/ContactMe.vue'

export default[
  {
    path: '/index',
    name: 'index',
    component: index,
    meta:{showNav:true},
    children :[{
      path: "/myNotice/:noticeId",
      name: "notice",
      component: () => import("@/components/index/Main/noticeDetail.vue"),
      meta:{showNav:true},
      props: true,
    }]
  },
  {
    path: '/index/newNotice',
    name: 'newNotice',
    component: index,
    meta:{showNav:true},
  },
  {
    path: '/resume',
    name: 'resume',
    meta:{showNav:true},
    component: resume
  },
  {
    path: '/project',
    name: 'project',
    meta:{showNav:true},
    component: project
  },
  {
    path: '/article',
    name: 'article',
    meta:{showNav:true},
    component: article
  },
  {
    path: '/contactMe',
    name: 'contactMe',
    meta:{showNav:true},
    component: contact
  }
]