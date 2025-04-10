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
    children :[{
      path: "/myNotice/:noticeId",
      name: "notice",
      component: () => import("@/components/index/Main/noticeDetail.vue"),
      props: true
    }]
  },
  {
    path: '/resume',
    name: 'resume',
    component: resume
  },
  {
    path: '/project',
    name: 'project',
    component: project
  },
  {
    path: '/article',
    name: 'article',
    component: article
  },
  {
    path: '/contactMe',
    name: 'contactMe',
    component: contact
  }
]