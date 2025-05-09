import myProjects from "@/components/project/MyProjects.vue"

export default [
    {
        path: '/project',
        name: 'project',
        meta:{showNav:true},
        component: myProjects
    },
    {
        path: '/project/:first?/:second?/:third?/:fourth?/:fifth?/:sixth?/:seventh?',
        name: 'projectPath',
        meta:{showNav:true},
        component: myProjects,
        props: true
    }
]
