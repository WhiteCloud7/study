import myProjects from "@/components/project/MyProjects.vue"

export default [
    {
        path: '/project',
        name: 'project',
        component: myProjects
    },
    {
        path: '/project/:first?/:second?/:third?/:fourth?/:fifth?/:sixth?/:seventh?',
        name: 'projectPath',
        component: myProjects
    }
]
