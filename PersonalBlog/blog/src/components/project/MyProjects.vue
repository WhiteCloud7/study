<template>
  <!-- 面包屑导航 -->
  <div class="projects-breadcrumbNav">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click="() => navigateTo(-1)" style="cursor: pointer;">
        project
      </el-breadcrumb-item>
      <el-breadcrumb-item
          v-for="(dir, index) in filePath"
          :key="index"
          @click="() => navigateTo(index)"
          style="cursor: pointer;"
      >
        {{ dir }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>

  <!-- 工具栏 -->
  <div class="projects-utils">
    <el-icon class="projectUtils-add" @click="dialogVisible=true"><i class="fa-solid fa-plus-circle"></i><p>新建</p></el-icon>
    <div class="project-operationUtils">
      <el-tooltip content="复制" effect="light" placement="top"><div class="project-operationIcon" @click="copy"><i class="fa-sharp fa-regular fa-copy " :style="{color: utilsColor,cursor:utilsColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
      <el-tooltip content="剪贴" effect="light" placement="top"><div class="project-operationIcon" @click="cut"><i class="fa-sharp-duotone fa-solid fa-scissors " :style="{color: utilsColor,cursor:utilsColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
      <el-tooltip content="粘贴" effect="light" placement="top"><div v-loading.fullscreen.lock="isPaste" element-loading-text="正在粘贴..." class="project-operationIcon" @click="paste"><i class="fa-regular fa-paste " :style="{color: pasteColor,cursor:pasteColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
      <el-tooltip content="重命名" effect="light" placement="top"><div class="project-operationIcon" @click="rename"><i class="fa-solid fa-pen-to-square" :style="{color: utilsColor,cursor:utilsColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
      <el-tooltip content="删除" effect="light" placement="top"><div v-loading.fullscreen.lock="isDelete" element-loading-text="删除中..." class="project-operationIcon" @click="deleteFile"><i class="fa-regular fa-trash-can" :style="{color: utilsColor,cursor:utilsColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
      <el-tooltip content="下载" effect="light" placement="top"><div class="project-operationIcon" @click="download"><i class="fa-solid fa-download" :style="{color: utilsColor,cursor:utilsColor==='black'?'pointer':'default'}"></i></div></el-tooltip>
    </div>
    <div class="projectUtils-sort">
      <el-select class="projectUtils-sortSelect"
                 v-model="sortMethod"
                 filterable
                 placeholder="排序方式"
                 @change="handleSortChange"
                 @click="initRename"
      ><template #prefix>
          <el-icon><i class="fa-solid fa-sort"></i></el-icon>
        </template>
        <el-option
            v-for="method in sortMethods"
            :key="method.name"
            :label="method.value"
            :value="method.name"
        >{{ method.value }}
        </el-option>
      </el-select>
      <el-select class="projectUtils-sortSelect"
                 v-model="sortOrder"
                 filterable
                 placeholder="排序顺序"
                 @change  ="handleSortChange"
                 @click="initRename"
      ><template #prefix>
          <el-icon>
            <i class="fa-solid fa-sort-up" v-if="isAsc"></i>
            <i class="fa-solid fa-sort-down" v-else></i>
          </el-icon>
        </template>
        <el-option
            v-for="order in sortOrders"
            :key="order.name"
            :label="order.value"
            :value="order.name"
        >{{ order.value }}</el-option>
      </el-select>
    </div>
    <input type="file" style="margin-left: 10px" @change="handleFileChange" />
    <div v-loading.fullscreen.lock="isUploading" element-loading-text="正在上传..."></div>
  </div>
  <el-dialog v-model="dialogVisible" title="请输入新建文件名" width="500">
    <el-input type="text" @keydown.enter="addFile" maxlength="20" v-model="newFileName"></el-input>
      <el-button @click="addFile" style="margin-left: 150px;margin-top: 12px">确认</el-button>
      <el-button @click="dialogVisible=false" style="margin-left: 40px;margin-top: 12px">取消</el-button>
  </el-dialog>
  <!-- 多级目录容器 -->
  <div class="projects-directoryContainer">
    <the-directory
        v-for="(dir,index) in currentDirectors"
        :key="dir.fileId"
        :file-id="dir.fileId"
        :file-name="dir.fileName"
        :modify-time="dir.modifyTime"
        :type="dir.type"
        :dir-level="dir.dirLevel"
        @dblclick="goNextDir(dir.fileId,dir.fileName)"
        @is-check="handleCheck"
        :is-rename="isRename[index]"
        @rename="handleRename"
    ></the-directory>
  </div>
</template>

<script setup>
import {getCurrentInstance} from "vue";
import { useRoute, useRouter } from 'vue-router'
import {onMounted, onUnmounted, ref, watch} from 'vue'
import axios from 'axios'
import axiosToken from "@/axios"
import '@fortawesome/fontawesome-free/css/all.css'
import TheDirectory from "@/components/project/TheDirectory";
import {useHead} from "@vueuse/head";
import {cloneDeep} from "lodash";
import {ElMessage} from "element-plus";

const router = useRouter();
const route = useRoute();

const instance = getCurrentInstance();
const isLogin = instance.appContext.config.globalProperties.$isLogin;
const currentDirectors = ref([]);
const filePath = ref([]);
const newFileName = ref("");
const dialogVisible = ref(false);
const isRename = ref([]);
const utilsColor = ref('lightgray');
const pasteColor = ref('lightgray');
const checks = ref([]);
const isAsc = ref(true);
const shearPlate = ref([]);
const isCopy = ref(false);
let isProgrammaticChange = ref(false);  // 监控是否为程序内部变化
const uploadHeaders = { Authorization: `Bearer ${sessionStorage.getItem('token')}`};

const sortMethod = ref('');
const sortOrder = ref('');
const sortMethods = [{name:'name',value:'名称'},{name:'modifyTime',value:'修改日期'},{name:'type',value:'类型'}];
const sortOrders = [{name:'asc',value:'递增'},{name:'desc',value:'递减'}];
const shearPath = ref();
const isUploading = ref(false)
const isPaste = ref(false);
const isDelete = ref(false);
let cancelTokenSource = null

useHead({
  title:"个人项目"
})

const navigateTo = (index) => {
  initRename();
  const levels = filePath.value.slice(0, index + 1);
  let path = '';
  for (let level of levels) {
    path = path.concat(level).concat('/');
  }
  if (path != '') {
    sessionStorage.setItem('filePath', JSON.stringify(levels));
    axios.get(`http://localhost:8081/project/${path}`, {
      responseType: "json"
    }).then(res => {
      currentDirectors.value = res.data;
      console.log(currentDirectors.value);
      checks.value = [];
    }).catch(err => {
      console.log(err);
    });
  } else {
    initDirectory();
  }
  isProgrammaticChange.value = true;  // 标记为程序跳转
  router.push(`/project/${levels.join('/')}`);
  filePath.value = levels;
}

function formatDateToMySQL(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const handleFileChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)
  formData.append('filePath', filePath.value)

  isUploading.value = true
  cancelTokenSource = axios.CancelToken.source()

  try {
    await axiosToken.post('http://localhost:8081/uploadFile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      cancelToken: cancelTokenSource.token,
    })

    ElMessage.success("上传成功")
    initDirectory()
  } catch (err) {
    if (axios.isCancel(err)) {
      ElMessage.warning("上传已取消")
    } else {
      ElMessage.error("上传失败")
      console.error('上传失败', err)
    }
  } finally {
    isUploading.value = false
    cancelTokenSource = null
  }
}

const cancelUpload = () => {
  if (cancelTokenSource) {
    cancelTokenSource.cancel("用户取消上传")
    isUploading.value = false
  }
}

function getFileType(fileName){
  const fileNameArray = fileName.split('');
  let result;
  for(let i = fileNameArray.length-1;i>=0;i--){
    if(fileNameArray[i]=='.') {
      result = fileNameArray.slice(i+1,fileNameArray.length).join('');
      break;
    }else if(i==0){
      result = "文件夹";
    }  }
  return result;
}

function handleCheck(isClick, fileId) {
  const existing = checks.value.find(item => item.fileId === fileId);
  if (existing) {
    existing.isClick = isClick;
  } else {
    checks.value.push({ fileId, isClick });
  }
  const hasTrueClick = checks.value.some(item => item.isClick);
  utilsColor.value = hasTrueClick ? 'black' : 'lightgray';
}

const handleAvatarSuccess = (res) => {
  if(res.data==="上传文件为空")
    alert("上传文件为空!");
  else if(res.data==="上传失败")
    alert("上传失败!");
  else{
    initDirectory();
    const is = confirm('上传成功!');
    if(is)
      initDirectory();
  }
};

function addFile(){
  initRename();
  const createFileTime = new Date();
  const params = new URLSearchParams();
  params.append('fileName',newFileName.value);
  params.append('modifyTime',formatDateToMySQL(createFileTime))
  params.append('type',getFileType(newFileName.value))
  params.append('filePath',filePath.value)
  if(newFileName.value!=null&&newFileName.value!=''){
    axios.post("http://localhost:8081/addFile",params,{
    }).then(res =>{
      newFileName.value = '';
      const data = res.data;
      currentDirectors.value.push(data);
      dialogVisible.value = false;
    }).catch(err =>{
      console.log(err);
    })
  }else
    alert('文件名不能为空');
}

function dateToNum(str){
  str = parseInt(str.replaceAll('-','').replaceAll(':','').replace(' ',''));
  return str;
}

function sortByFileName(isAsc) {
  return currentDirectors.value.sort((a, b) => isAsc ? a.fileName.localeCompare(b.fileName) : b.fileName.localeCompare(a.fileName));
}

function sortByModifyTime(isAsc) {
  return currentDirectors.value.sort((a, b) => {
    const dateA = dateToNum(a.modifyTime);
    const dateB = dateToNum(b.modifyTime);
    return isAsc ? dateA - dateB : dateB - dateA;
  });
}

function sortByType(isAsc) {
  const typeMap = {};
  currentDirectors.value.forEach(item => {
    const type = item.type;
    if (!typeMap[type]) {
      typeMap[type] = [];
    }
    typeMap[type].push(item);
  });
  const sortedTypes = Object.keys(typeMap).sort((a, b) => isAsc ? a.localeCompare(b) : b.localeCompare(a));
  const result = [];
  sortedTypes.forEach(type => {
    result.push(...typeMap[type]);
  });
  currentDirectors.value = result;
  return currentDirectors.value;
}

function sort(sortMethod, isAsc) {
  switch (sortMethod) {
    case 'name':sortByFileName(isAsc);break;
    case 'modifyTime':sortByModifyTime(isAsc);break;
    case 'type':sortByType(isAsc);break;
    default:break;
  }
}
function handleSortChange() {
  const flag = sortOrder.value === 'asc';
  sort(sortMethod.value, flag);
  if(flag)
    isAsc.value = true;
  else
    isAsc.value = false;
}

function copy(e){
  if(utilsColor.value === 'lightgray')
    e.preventDefault();
  else{
    initRename();
    shearPath.value = cloneDeep(filePath.value);
    const isClick = checks.value.filter(map => map.isClick === true);
    shearPlate.value = isClick.map(map => map.fileId);
    isCopy.value = true;
  }
}

function cut(e){
  if(utilsColor.value === 'lightgray')
    e.preventDefault();
  else{
    initRename();
    shearPath.value = cloneDeep(filePath.value);
    const isClick = checks.value.filter(map => map.isClick === true);
    shearPlate.value = isClick.map(map => map.fileId);
    isCopy.value = false;
  }
}

function paste(e){
  if(pasteColor.value==='black'){
    initRename();
    const formData = new URLSearchParams();
    formData.append('filePath',filePath.value);
    shearPlate.value.forEach(fileId => {
      formData.append('fileIds[]', fileId);
    });
    formData.append("shearPath",shearPath.value);
    isPaste.value = true;
    if(isCopy.value){
      axios.post("http://localhost:8081/copyPaste",formData,{
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }).then(res=>{
        shearPlate.value = [];
        const datas = res.data;
        const currentDirData = datas.filter(d=>d.dirLevel===(filePath.value.length + 1));
        for(let data of currentDirData){
          currentDirectors.value.push(data);
        }
      }).catch(err =>{
        console.log(err);
      }).finally(()=>{
      isPaste.value = false;
    });
    }else {
      axios.post("http://localhost:8081/cutPaste",formData,{
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }).then(res=>{
        shearPlate.value = [];
        const datas = res.data;
        const currentDirData = datas.filter(d=>d.dirLevel===(filePath.value.length + 1));
        for(let data of currentDirData){
          currentDirectors.value.push(data);
        }
      }).catch(err =>{
        console.log(err);
      }).finally(()=>{
        isPaste.value = false;
      });
    }
  }else{
    e.preventDefault();
  }
}

function rename(){
  const checkCount = checks.value.reduce((acc,c) => {return c.isClick===true?acc+1:acc},0);
  if(checkCount===1){
    if(isRename.value.every(i=>i===false)){
      const clickId = checks.value.find(c=>c.isClick===true).fileId;
      const clickIndex = currentDirectors.value.findIndex(c=>c.fileId===clickId);
      isRename.value[clickIndex] = true;
    }else{
      initRename();
      rename();
    }
  }else{
    alert("请选择一个进行重命名！");
  }
}

function handleRename(newFileName){
  const clickId = checks.value.find(c=>c.isClick===true).fileId;
  const clickIndex = currentDirectors.value.findIndex(c=>c.fileId===clickId);
  isRename.value[clickIndex] = false;
  const oldName = currentDirectors.value.find(c=>c.fileId===clickId).fileName;
  currentDirectors.value.find(c=>c.fileId===clickId).fileName = newFileName;
  console.log(filePath.value.join("/"))
  axios.get("http://localhost:8081/rename",{
    params:{
      newFileName:newFileName,
      fileId:clickId,
      fPath:filePath.value.join("/")
    }
  }).catch(err=>{
    console.log(err);
  })
}

function deleteFile(e){
  if(utilsColor.value==='black'){
    const isClicks = checks.value.filter(c=>c.isClick===true)
    const deleteFiles = isClicks.map(d=>d.fileId);
    isDelete.value = true;
    axios.get("http://localhost:8081/deleteFile",{
      params:{
        deleteFiles:deleteFiles.join(','),
        filePath:filePath.value.join("/")
      }
    }).then(res=>{
      initRename();
      currentDirectors.value = currentDirectors.value.filter(c => !deleteFiles.includes(c.fileId));
    }).catch(err=>{
      console.log(err);
    }).finally(()=>{
      isDelete.value = false;
    });
  }else{
    e.preventDefault();
  }
}

function download() {
  const fileIdList = checks.value.filter(c => c.isClick === true);
  if(isLogin.value!=true){
    router.push('/login');
    return;
  }
  if (fileIdList.length !== 1) {
    alert("一次只支持下载一个文件！");
    return;
  }
  const fileId = fileIdList[0].fileId;
  axiosToken.get("http://localhost:8081/download", {
    params: {
      filePath: encodeURIComponent(filePath.value),
      fileId: fileId
    },
    responseType: 'blob'
  }).then(res => {
    const blob = new Blob([res.data]);
    const downloadUrl = window.URL.createObjectURL(blob);
    // 提取文件名（可选，依赖后端是否设置 Content-Disposition）
    const disposition = res.headers['content-disposition'];
    let fileName = '下载文件';
    if (disposition && disposition.includes('filename=')) {
      fileName = decodeURIComponent(disposition.split('filename=')[1].slice(1,disposition.split('filename=')[1].length-1));
    }
    // 创建 a 标签触发下载
    const a = document.createElement('a');
    a.href = downloadUrl;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    // 清理
    a.remove();
    window.URL.revokeObjectURL(downloadUrl);
  }).catch(err => {
    console.error("下载失败", err);
    ElMessage.error("下载失败！");
  });
}


function goNextDir(fileId,currentDirName) {
  if(getFileType(currentDirName)=='文件夹'){
    filePath.value.push(currentDirName);
    isProgrammaticChange.value = true;  // 标记为程序跳转
    axios.get("http://localhost:8081/getNextDirs", {
      params: {
        fileId:fileId
      },
      responseType: "json"
    }).then(res => {
      sessionStorage.setItem('filePath', JSON.stringify(filePath.value.join('/')));
      const nextDirs = res.data;
      currentDirectors.value = nextDirs;
      checks.value = [];
      utilsColor.value = 'lightgray';
      const newPath = `/project/${filePath.value.join('/')}`;
      router.push(newPath);
      initRename();
    }).catch(err => {
      console.log(err);
    });
  }else
    alert("目前暂不支持预览！");
}

function initDirectory() {
  axios.get("http://localhost:8081/getTheFirstDirectory", {
    responseType: "json"
  }).then(res => {
    currentDirectors.value = res.data;
    initRename();
  }).catch(err => {
    console.log(err);
  });
}

function initRename(){
  isRename.value = [];
  for(let i = 0;i<currentDirectors.value.length;i++){
    isRename.value.push(false)
  }
}

watch(() => shearPlate.value , (newParams) => {
  if (newParams.length!==0)
    pasteColor.value = 'black';
  else
    pasteColor.value = 'lightgray';
})

watch(() => route.params, (newParams) => {
  if (isProgrammaticChange.value) {
    isProgrammaticChange.value = false;  // 跳过程序跳转
    return;
  }
  const levels = Object.values(newParams);
  let path = '/project';
  for(let level of levels)
    if(level!=''){
      path = path.concat("/").concat(String(level));
    }
  // 请求相应的目录数据
  if (path !== '/project') {
    path=path.replace('/project','');
    axios.get(`http://localhost:8081/project${path}`, { responseType: 'json' })
        .then(res => {
          currentDirectors.value = res.data;
          filePath.value = [];
          for(let level of levels)
            if(level!='')
              filePath.value.push(level);
          initRename();
        }).catch(err => {
          console.log(err);
        });
  } else {
    filePath.value = [];
    initDirectory();
  }
}, { immediate: true });

onMounted(() => {
  const storedPath = sessionStorage.getItem('filePath');
  if (storedPath) {
    console.log('保持原页面');
  } else {
    initDirectory();
  }
})

function Click(e){
  const isDirectoryClick = e.target.closest(".projects-directoryContainer");
  const is = e.target.closest(".projects-utils");
  if (!isDirectoryClick&&!is) {
    initRename();
  }
}

onMounted(()=>{
  document.addEventListener("click",Click);
})
onUnmounted(()=>{
  document.removeEventListener("click",Click);
})
</script>
<style scoped>
.projects-breadcrumbNav {
  width: 100%;
  height: 25px;
  padding-left: 30px;
}
::v-deep(.el-breadcrumb__inner) {
  font-size: 16px;
  font-weight: bold;
  margin-top: 1px;
}

.projects-utils {
  display: flex;
  flex-direction: row;
  align-items: center;
  border: 1px solid black;
  width: 100%;
  height: 35px;
}
.projectUtils-add{
  font-style: normal;
  cursor: pointer;
  min-width: 100px;
  height: 90%;
}
.project-operationUtils{
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-left: 10px;
  height: 90%;
}
.project-operationIcon{
  padding-left: 5px;
  width: 26px;
  margin-right: 15px;
}
.projectUtils-sort{
  display: flex;
  flex-direction: row;
  width: 240px;
  height: 100%;
  margin-left: 550px;
}
.projectUtils-sortSelect{
  width: 120px;
}
.projectUtils-add:hover,
.project-operationIcon:hover,
.projectUtils-delete:hover,
.projectUtils-sort:hover{
  background-color: lightgray;
}

.projects-directoryContainer {
  display: flex;
  flex-direction: column;
  font-size: 20px;
  width: 100%;
  min-height: 90%;
  overflow-y: auto;
  border: 1px solid black;
}
.project-cancelUpload{
  padding: 0;
  font-size: 20px;
  width: 10%;
  height: 8%;
  position: absolute;
  left: 45%;
  right: 45%;
  top: 70%;
  bottom: 22%;
  z-index: 500;
}
</style>
