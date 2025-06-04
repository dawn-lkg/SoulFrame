<template>
  <div class="dashboard">
    <!-- 统计卡片区域 -->
    <a-row :gutter="[16, 16]">
      <a-col :xs="24" :sm="12" :md="12" :lg="6">
        <a-card class="stat-card">
          <div class="stat-header">
            <span class="stat-title">总访问量</span>
            <Icon icon="ant-design:line-chart-outlined" class="stat-icon" />
          </div>
          <div class="stat-content">
            <span class="stat-number">88,846</span>
            <span class="stat-percent success">
              <Icon icon="ant-design:arrow-up-outlined" />
              12%
            </span>
          </div>
          <div class="stat-footer">
            较上周增长
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="12" :lg="6">
        <a-card class="stat-card">
          <div class="stat-header">
            <span class="stat-title">新增用户</span>
            <Icon icon="ant-design:user-outlined" class="stat-icon" />
          </div>
          <div class="stat-content">
            <span class="stat-number">1,286</span>
            <span class="stat-percent warning">
              <Icon icon="ant-design:arrow-down-outlined" />
              5%
            </span>
          </div>
          <div class="stat-footer">
            较上周减少
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="12" :lg="6">
        <a-card class="stat-card">
          <div class="stat-header">
            <span class="stat-title">订单数量</span>
            <Icon icon="ant-design:shopping-outlined" class="stat-icon" />
          </div>
          <div class="stat-content">
            <span class="stat-number">3,568</span>
            <span class="stat-percent success">
              <Icon icon="ant-design:arrow-up-outlined" />
              8%
            </span>
          </div>
          <div class="stat-footer">
            较上周增长
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="12" :md="12" :lg="6">
        <a-card class="stat-card">
          <div class="stat-header">
            <span class="stat-title">总收入</span>
            <Icon icon="ant-design:wallet-outlined" class="stat-icon" />
          </div>
          <div class="stat-content">
            <span class="stat-number">¥25,666</span>
            <span class="stat-percent success">
              <Icon icon="ant-design:arrow-up-outlined" />
              15%
            </span>
          </div>
          <div class="stat-footer">
            较上周增长
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表区域 -->
    <a-row :gutter="[16, 16]" class="chart-row">
      <a-col :xs="24" :lg="16">
        <a-card title="访问趋势" :bordered="false">
          <template #extra>
            <a-radio-group v-model:value="timeRange" button-style="solid" size="small">
              <a-radio-button value="week">本周</a-radio-button>
              <a-radio-button value="month">本月</a-radio-button>
              <a-radio-button value="year">全年</a-radio-button>
            </a-radio-group>
          </template>
          <div class="chart-container">
            <line-chart :time-range="timeRange" />
          </div>
        </a-card>
      </a-col>
      <a-col :xs="24" :lg="8">
        <a-card title="用户分布" :bordered="false">
          <div class="chart-container">
            <pie-chart />
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 最近活动和待办事项 -->
    <a-row :gutter="[16, 16]" class="activity-row">
      <a-col :xs="24" :lg="12">
        <a-card title="最近活动" :bordered="false">
          <a-list :data-source="activities" :pagination="false">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: item.color }">
                      <Icon :icon="item.icon" />
                    </a-avatar>
                  </template>
                  <template #title>{{ item.title }}</template>
                  <template #description>{{ item.time }}</template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <a-col :xs="24" :lg="12">
        <a-card title="待办事项" :bordered="false" :tabList="todoTabs">
          <template #extra>
            <a-button type="primary" size="small">
              <template #icon>
                <Icon icon="ant-design:plus-outlined" />
              </template>
              添加
            </a-button>
          </template>
          <a-list :data-source="todos" :pagination="false">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-checkbox v-model:checked="item.done">{{ item.content }}</a-checkbox>
                <template #actions>
                  <a-tag :color="item.type.color">{{ item.type.text }}</a-tag>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Icon } from '@iconify/vue'
import LineChart from '@/components/charts/LineChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

const timeRange = ref('week')

// 模拟活动数据
const activities = ref([
  {
    title: '张三完成了系统升级',
    time: '2024-03-20 10:00',
    icon: 'ant-design:check-circle-outlined',
    color: '#52c41a'
  },
  {
    title: '李四添加了新产品',
    time: '2024-03-20 09:30',
    icon: 'ant-design:plus-circle-outlined',
    color: '#1890ff'
  },
  {
    title: '王五修改了用户权限',
    time: '2024-03-20 09:00',
    icon: 'ant-design:edit-outlined',
    color: '#faad14'
  },
  {
    title: '系统自动备份完成',
    time: '2024-03-20 08:30',
    icon: 'ant-design:cloud-outlined',
    color: '#722ed1'
  }
])

// 模拟待办事项数据
const todos = ref([
  {
    content: '完成首页设计',
    done: false,
    type: { text: '进行中', color: 'processing' }
  },
  {
    content: '优化系统性能',
    done: false,
    type: { text: '待处理', color: 'warning' }
  },
  {
    content: '更新用户文档',
    done: true,
    type: { text: '已完成', color: 'success' }
  }
])

const todoTabs = [
  { key: 'all', tab: '全部' },
  { key: 'todo', tab: '待办' },
  { key: 'done', tab: '已完成' }
]
</script>

<style lang="scss" scoped>
.dashboard {
  .stat-card {
    .stat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      .stat-title {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }

      .stat-icon {
        font-size: 24px;
        color: #1890ff;
      }
    }

    .stat-content {
      display: flex;
      align-items: baseline;
      margin-bottom: 8px;

      .stat-number {
        font-size: 24px;
        font-weight: 500;
        margin-right: 16px;
      }

      .stat-percent {
        display: flex;
        align-items: center;
        font-size: 14px;

        &.success {
          color: #52c41a;
        }

        &.warning {
          color: #faad14;
        }

        .anticon {
          margin-right: 4px;
        }
      }
    }

    .stat-footer {
      color: rgba(0, 0, 0, 0.45);
      font-size: 12px;
    }
  }

  .chart-row {
    margin-top: 16px;

    .chart-container {
      height: 300px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #fafafa;
    }
  }

  .activity-row {
    margin-top: 16px;

    :deep(.ant-list-item) {
      padding: 12px 0;
    }

    :deep(.ant-avatar) {
      color: #fff;
    }
  }
}

// 暗色主题适配
:deep(.dark-mode) {
  .stat-card {

    .stat-header .stat-title,
    .stat-footer {
      color: rgba(255, 255, 255, 0.45);
    }
  }

  .chart-container {
    background-color: #1f1f1f;
  }
}
</style>