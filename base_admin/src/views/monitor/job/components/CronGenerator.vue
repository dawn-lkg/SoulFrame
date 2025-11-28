<template>
  <a-modal
      :open="visible"
      :title="'Cron表达式生成器'"
      width="700px"
      @cancel="handleCancel"
      @ok="handleConfirm"
  >
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="second" tab="秒">
        <a-radio-group v-model:value="second.type" @change="generateExpression">
          <a-radio value="*">每秒</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
        </a-radio-group>
        <div v-if="second.type === 'cycle'" class="mt-2">
          <a-space>
            从第
            <a-input-number v-model:value="second.start" :max="59" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            到第
            <a-input-number v-model:value="second.end" :max="59" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            秒
          </a-space>
        </div>
        <div v-if="second.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="second.specificList"
              mode="multiple"
              placeholder="请选择具体秒数"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 60" :key="i - 1" :value="i - 1">{{ i - 1 }}</a-select-option>
          </a-select>
        </div>
      </a-tab-pane>

      <a-tab-pane key="minute" tab="分钟">
        <a-radio-group v-model:value="minute.type" @change="generateExpression">
          <a-radio value="*">每分钟</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
        </a-radio-group>
        <div v-if="minute.type === 'cycle'" class="mt-2">
          <a-space>
            从第
            <a-input-number v-model:value="minute.start" :max="59" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            到第
            <a-input-number v-model:value="minute.end" :max="59" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            分钟
          </a-space>
        </div>
        <div v-if="minute.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="minute.specificList"
              mode="multiple"
              placeholder="请选择具体分钟"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 60" :key="i - 1" :value="i - 1">{{ i - 1 }}</a-select-option>
          </a-select>
        </div>
      </a-tab-pane>

      <a-tab-pane key="hour" tab="小时">
        <a-radio-group v-model:value="hour.type" @change="generateExpression">
          <a-radio value="*">每小时</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
        </a-radio-group>
        <div v-if="hour.type === 'cycle'" class="mt-2">
          <a-space>
            从第
            <a-input-number v-model:value="hour.start" :max="23" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            到第
            <a-input-number v-model:value="hour.end" :max="23" :min="0" style="width: 80px"
                            @change="generateExpression"/>
            小时
          </a-space>
        </div>
        <div v-if="hour.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="hour.specificList"
              mode="multiple"
              placeholder="请选择具体小时"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 24" :key="i - 1" :value="i - 1">{{ i - 1 }}</a-select-option>
          </a-select>
        </div>
      </a-tab-pane>

      <a-tab-pane key="day" tab="日">
        <a-radio-group v-model:value="day.type" @change="generateExpression">
          <a-radio value="*">每天</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
          <a-radio value="last">最后一天</a-radio>
        </a-radio-group>
        <div v-if="day.type === 'cycle'" class="mt-2">
          <a-space>
            从第
            <a-input-number v-model:value="day.start" :max="31" :min="1" style="width: 80px"
                            @change="generateExpression"/>
            到第
            <a-input-number v-model:value="day.end" :max="31" :min="1" style="width: 80px"
                            @change="generateExpression"/>
            天
          </a-space>
        </div>
        <div v-if="day.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="day.specificList"
              mode="multiple"
              placeholder="请选择具体天数"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 31" :key="i" :value="i">{{ i }}</a-select-option>
          </a-select>
        </div>
      </a-tab-pane>

      <a-tab-pane key="month" tab="月">
        <a-radio-group v-model:value="month.type" @change="generateExpression">
          <a-radio value="*">每月</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
        </a-radio-group>
        <div v-if="month.type === 'cycle'" class="mt-2">
          <a-space>
            从第
            <a-input-number v-model:value="month.start" :max="12" :min="1" style="width: 80px"
                            @change="generateExpression"/>
            到第
            <a-input-number v-model:value="month.end" :max="12" :min="1" style="width: 80px"
                            @change="generateExpression"/>
            月
          </a-space>
        </div>
        <div v-if="month.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="month.specificList"
              mode="multiple"
              placeholder="请选择具体月份"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 12" :key="i" :value="i">{{ i }}月</a-select-option>
          </a-select>
        </div>
      </a-tab-pane>

      <a-tab-pane key="week" tab="周">
        <a-radio-group v-model:value="week.type" @change="generateExpression">
          <a-radio value="*">每周</a-radio>
          <a-radio value="?">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
          <a-radio value="last">本月最后一个</a-radio>
        </a-radio-group>
        <div v-if="week.type === 'cycle'" class="mt-2">
          <a-space>
            从周
            <a-input-number v-model:value="week.start" :max="7" :min="1" style="width: 80px"
                            @change="generateExpression"/>
            到周
            <a-input-number v-model:value="week.end" :max="7" :min="1" style="width: 80px"
                            @change="generateExpression"/>
          </a-space>
        </div>
        <div v-if="week.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="week.specificList"
              mode="multiple"
              placeholder="请选择具体星期"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option :value="1">周日</a-select-option>
            <a-select-option :value="2">周一</a-select-option>
            <a-select-option :value="3">周二</a-select-option>
            <a-select-option :value="4">周三</a-select-option>
            <a-select-option :value="5">周四</a-select-option>
            <a-select-option :value="6">周五</a-select-option>
            <a-select-option :value="7">周六</a-select-option>
          </a-select>
        </div>
        <div v-if="week.type === 'last'" class="mt-2">
          <a-space>
            本月最后一个
            <a-select v-model:value="week.last" style="width: 100px" @change="generateExpression">
              <a-select-option :value="1">周日</a-select-option>
              <a-select-option :value="2">周一</a-select-option>
              <a-select-option :value="3">周二</a-select-option>
              <a-select-option :value="4">周三</a-select-option>
              <a-select-option :value="5">周四</a-select-option>
              <a-select-option :value="6">周五</a-select-option>
              <a-select-option :value="7">周六</a-select-option>
            </a-select>
          </a-space>
        </div>
      </a-tab-pane>

      <a-tab-pane key="year" tab="年">
        <a-radio-group v-model:value="year.type" @change="generateExpression">
          <a-radio value="*">每年</a-radio>
          <a-radio value="">不指定</a-radio>
          <a-radio value="cycle">周期</a-radio>
          <a-radio value="specific">指定</a-radio>
        </a-radio-group>
        <div v-if="year.type === 'cycle'" class="mt-2">
          <a-space>
            从
            <a-input-number v-model:value="year.start" :max="2100" :min="2000" style="width: 100px"
                            @change="generateExpression"/>
            到
            <a-input-number v-model:value="year.end" :max="2100" :min="2000" style="width: 100px"
                            @change="generateExpression"/>
            年
          </a-space>
        </div>
        <div v-if="year.type === 'specific'" class="mt-2">
          <a-select
              v-model:value="year.specificList"
              mode="multiple"
              placeholder="请选择具体年份"
              style="width: 100%"
              @change="generateExpression"
          >
            <a-select-option v-for="i in 10" :key="2023 + i - 1" :value="2023 + i - 1">{{
                2023 + i - 1
              }}
            </a-select-option>
          </a-select>
        </div>
      </a-tab-pane>
    </a-tabs>

    <div class="cron-preview">
      <a-divider>表达式预览</a-divider>
      <a-row>
        <a-col :span="24">
          <a-input v-model:value="cronExpression" readonly/>
        </a-col>
      </a-row>
      <a-divider>常用示例</a-divider>
      <a-row :gutter="[16, 16]">
        <a-col v-for="(example, index) in examples" :key="index" :span="8">
          <a-button type="link" @click="applyExample(example.value)">
            {{ example.label }}：{{ example.value }}
          </a-button>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script setup>
import {ref, reactive, watch} from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  value: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:visible', 'confirm', 'cancel'])

// 当前活动的标签页
const activeTab = ref('second')

// 表达式各部分的配置
const second = reactive({
  type: '*',
  start: 0,
  end: 59,
  specificList: []
})

const minute = reactive({
  type: '*',
  start: 0,
  end: 59,
  specificList: []
})

const hour = reactive({
  type: '*',
  start: 0,
  end: 23,
  specificList: []
})

const day = reactive({
  type: '*',
  start: 1,
  end: 31,
  specificList: []
})

const month = reactive({
  type: '*',
  start: 1,
  end: 12,
  specificList: []
})

const week = reactive({
  type: '?',
  start: 1,
  end: 7,
  specificList: [],
  last: 1
})

const year = reactive({
  type: '',
  start: new Date().getFullYear(),
  end: new Date().getFullYear() + 5,
  specificList: []
})

// 生成的cron表达式
const cronExpression = ref('')

// 常用cron表达式示例
const examples = [
  {label: '每5秒执行', value: '0/5 * * * * ?'},
  {label: '每小时执行', value: '0 0 * * * ?'},
  {label: '每天凌晨2点执行', value: '0 0 2 * * ?'},
  {label: '每周一凌晨1点执行', value: '0 0 1 ? * 2'},
  {label: '每月1日凌晨1点执行', value: '0 0 1 1 * ?'},
  {label: '每季度第一个月第一天执行', value: '0 0 0 1 1,4,7,10 ?'}
]

// 初始化
watch(() => props.visible, (val) => {
  if (val && props.value) {
    parseCronExpression(props.value)
  }
})

// 解析cron表达式
const parseCronExpression = (expression) => {
  if (!expression) return

  const parts = expression.split(' ')
  if (parts.length < 6) return

  // 秒
  parsePartValue(parts[0], second)
  // 分
  parsePartValue(parts[1], minute)
  // 时
  parsePartValue(parts[2], hour)
  // 日
  parsePartValue(parts[3], day)
  // 月
  parsePartValue(parts[4], month)
  // 周
  parsePartValue(parts[5], week)
  // 年
  if (parts.length > 6) {
    parsePartValue(parts[6], year)
  } else {
    year.type = ''
  }

  // 更新表达式
  cronExpression.value = expression
}

// 解析cron表达式的各个部分
const parsePartValue = (part, config) => {
  if (part === '*') {
    config.type = '*'
  } else if (part === '?') {
    config.type = '?'
  } else if (part.includes('-')) {
    config.type = 'cycle'
    const [start, end] = part.split('-')
    config.start = parseInt(start)
    config.end = parseInt(end)
  } else if (part.includes(',')) {
    config.type = 'specific'
    config.specificList = part.split(',').map(item => parseInt(item))
  } else if (part.includes('L')) {
    config.type = 'last'
    const value = part.replace('L', '')
    if (value) {
      config.last = parseInt(value)
    }
  } else if (part.includes('/')) {
    // 处理间隔，暂不实现
    config.type = '*'
  } else {
    // 单个值
    config.type = 'specific'
    config.specificList = [parseInt(part)]
  }
}

// 生成cron表达式
const generateExpression = () => {
  const secondVal = getPartValue(second)
  const minuteVal = getPartValue(minute)
  const hourVal = getPartValue(hour)
  const dayVal = getPartValue(day)
  const monthVal = getPartValue(month)
  const weekVal = getPartValue(week)
  const yearVal = year.type ? getPartValue(year) : ''

  cronExpression.value = [secondVal, minuteVal, hourVal, dayVal, monthVal, weekVal, yearVal]
      .filter(val => val !== '')
      .join(' ')
}

// 获取各部分的值
const getPartValue = (config) => {
  switch (config.type) {
    case '*':
      return '*'
    case '?':
      return '?'
    case '':
      return ''
    case 'cycle':
      return `${config.start}-${config.end}`
    case 'specific':
      return config.specificList.length > 0 ? config.specificList.join(',') : '*'
    case 'last':
      if (config === week) {
        return `${config.last}L`
      }
      return 'L'
    default:
      return '*'
  }
}

// 应用示例
const applyExample = (example) => {
  parseCronExpression(example)
}

// 确认
const handleConfirm = () => {
  emit('confirm', cronExpression.value)
  emit('update:visible', false)
}

// 取消
const handleCancel = () => {
  emit('cancel')
  emit('update:visible', false)
}

// 初始化生成表达式
generateExpression()
</script>

<style scoped>
.mt-2 {
  margin-top: 8px;
}

.cron-preview {
  margin-top: 16px;
}
</style> 