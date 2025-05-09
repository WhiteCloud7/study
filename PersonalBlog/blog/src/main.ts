import { createApp,ref } from 'vue'
import App from './App.vue'
import router from "./router/mianRouter"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {PictureFilled} from "@element-plus/icons-vue";
import GlobalMask from "@/components/public/GlobalMask.vue"
import { createHead } from '@vueuse/head';

const app = createApp(App);
const head = createHead();
const isLogin = ref(localStorage.getItem('token') !== null);
const currentChatObject = ref("");
const currentUserName = ref("");

app.component("PictureFilled",PictureFilled)
app.component("GlobalMask",GlobalMask);

app.config.globalProperties.$isLogin = isLogin;
app.config.globalProperties.$currentChatObject = currentChatObject;

app.use(head);
app.use(ElementPlus);
app.use(router)
app.mount('#app');

const observerErrorHandler = (e: ErrorEvent) => {
    if (
        e.message ===
        'ResizeObserver loop completed with undelivered notifications.'
    ) {
        e.stopImmediatePropagation();
    }
};

window.addEventListener('error', observerErrorHandler);