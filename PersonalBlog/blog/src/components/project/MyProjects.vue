<template>
  <!-- 面包屑导航 -->
  <div class="projects-breadcrumbNav">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click="() => router.push('/project')" style="cursor: pointer;">
        project
      </el-breadcrumb-item>
      <el-breadcrumb-item
          v-for="(dir, index) in directories"
          :key="index"
          @click="() => navigateTo(index)"
          style="cursor: pointer;"
      >
        {{ dir }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>

  <!-- 工具栏 -->
  <div class="projects-utils"></div>

  <!-- 多级目录容器 -->
  <div class="projects-directoryContainer">
    <component
        :is="currentComponent"
        :name="currentName"
        :key="currentComponent"
    />
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'

// 导入所有目录组件
import FirstLevelDirectory from "@/components/project/nLevelDirectory/FirstLevelDirectory";
import SecondLevelDirectory from "@/components/project/nLevelDirectory/SecondLevelDirectory";
import ThirdLevelDirectory from "@/components/project/nLevelDirectory/ThirdLevelDirectory";
import FourthLevelDirectory from "@/components/project/nLevelDirectory/FourthLevelDirectory";
import FifthLevelDirectory from "@/components/project/nLevelDirectory/FifthLevelDirectory";
import SixLevelDirectory from "@/components/project/nLevelDirectory/SixLevelDirectory";
import SevenLevelDirectory from "@/components/project/nLevelDirectory/SevenLevelDirectory";

const route = useRoute()
const router = useRouter()

const directories = computed(() => {
  const { first, second, third, fourth, fifth, sixth, seventh } = route.params
  return [first, second, third, fourth, fifth, sixth, seventh].filter(Boolean)
})

const currentLevel = computed(() => directories.value.length)

const componentMap = {
  0: FirstLevelDirectory,
  1: SecondLevelDirectory,
  2: ThirdLevelDirectory,
  3: FourthLevelDirectory,
  4: FifthLevelDirectory,
  5: SixLevelDirectory,
  6: SevenLevelDirectory
}

const currentComponent = computed(() => componentMap[currentLevel.value] || FirstLevelDirectory)
const currentName = computed(() => directories.value[currentLevel.value - 1] || null)

const navigateTo = (index) => {
  const levels = directories.value.slice(0, index + 1)
  router.push(`/project/${levels.join('/')}`)
}
</script>

<style scoped>
.projects-breadcrumbNav {
  border: 1px solid black;
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
  border: 1px solid black;
  width: 100%;
  height: 30px;
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
</style>
