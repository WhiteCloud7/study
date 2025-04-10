<template>
  <el-calendar  ref="calendar" class="calendar">
    <template #header="{ date }">
      <span class="date">{{ date }}</span>
      <el-button-group class="headerContainer" >
        <el-button class="header" @click="selectDate('prev-year')">
          上一年
        </el-button>
        <el-button class="header" @click="selectDate('prev-month')">
          上个月
        </el-button>
        <el-button class="header" @click="selectDate('today')">Today</el-button>
        <el-button class="header" @click="selectDate('next-month')">
          下个月
        </el-button>
        <el-button class="header" @click="selectDate('next-year')">
          下一年
        </el-button>
      </el-button-group>
    </template>
  </el-calendar>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { CalendarDateType, CalendarInstance } from 'element-plus'

const calendar = ref<CalendarInstance>()
const selectDate = (val: CalendarDateType) => {
  if (!calendar.value) return
  calendar.value.selectDate(val)
}
</script>

<style>
.calendar{
  color: black;
  font-size: 10px;
  width: 218px;
  height:100% ;
  padding: 0px;
}

.headerContainer{
  display: flex;
  flex-direction: row;
  width: 210px;
  height: 20px;
  position: relative;
  bottom: 6px ;
  left: 10px;
}
.el-calendar__header{
  height: 10px;
  background-color: #ecf5ff;
  padding: 0;
}
.date{
  color: #4a90e2;
  font-size: 8px;
  position: relative;
  right: 8px;
  bottom:8px;
  left: 0px;
}
.header{
  font-size: 8px !important;
  margin-right: 4px !important;
  height: 15px !important;
  width: 24px !important;
  padding: 0px !important;
}
.el-calendar__body{
  box-shadow: var(--el-box-shadow);
  background-color:#ecf5ff;
}
/*.el-calendar-table td {*/
/*  height: 20px !important;*/
/*  padding: 0 !important;*/
/*}*/

/* 或者修改内部的 .el-calendar-day */
.el-calendar-day {
  height: 24px !important;
  line-height: 24px !important;
  padding: 0 !important;
  font-size: 10px;
  background-color: #79bbff;
}
</style>