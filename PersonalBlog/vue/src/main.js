import {createApp, ref} from 'vue'
import App from './App.vue'
import router from './router';

const app = createApp(App);
const clickCount = ref(0);
app.config.globalProperties.$clickCount=clickCount;
app.use(router);
app.mount('#app');
