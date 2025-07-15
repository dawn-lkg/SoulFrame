<template>
  <div class="portal-container">
    <!-- 顶部搜索区域 -->
    <div class="search-section">
      <a-card class="search-card">
        <div class="search-content">
          <h1 class="search-title">企业资源管理平台</h1>
          <p class="search-subtitle">快速查找并管理您的企业资源</p>
          <div class="search-box">
            <a-input-search
                v-model:value="searchValue"
                enter-button
                placeholder="搜索应用、文档、资源..."
                size="large"
                @search="onSearch"
            />
          </div>
        </div>
      </a-card>
    </div>

    <!-- 快捷入口区域 -->
    <div class="shortcuts-section">
      <a-row :gutter="[16, 16]">
        <a-col :span="24">
          <a-card class="shortcuts-card" title="快捷入口">
            <div class="shortcuts-grid">
              <div v-for="(item, index) in shortcuts" :key="index" class="shortcut-item" @click="navigateTo(item.path)">
                <div :style="{ backgroundColor: item.bgColor }" class="shortcut-icon">
                  <Icon :icon="item.icon"/>
                </div>
                <div class="shortcut-name">{{ item.name }}</div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 中间内容区域 -->
    <div class="main-content">
      <a-row :gutter="[16, 16]">
        <!-- 左侧：最新动态 -->
        <a-col :lg="16" :xs="24">
          <a-card class="news-card" title="最新动态">
            <template #extra>
              <a-radio-group v-model:value="newsType" button-style="solid" size="small">
                <a-radio-button value="all">全部</a-radio-button>
                <a-radio-button value="system">系统</a-radio-button>
                <a-radio-button value="business">业务</a-radio-button>
              </a-radio-group>
            </template>
            <a-timeline>
              <a-timeline-item v-for="(item, index) in filteredNews" :key="index" :color="getNewsColor(item.type)">
                <div class="news-item">
                  <div class="news-header">
                    <span class="news-title">{{ item.title }}</span>
                    <a-tag :color="getNewsTagColor(item.type)">{{ item.type }}</a-tag>
                  </div>
                  <div class="news-content">{{ item.content }}</div>
                  <div class="news-footer">
                    <span class="news-time">{{ item.time }}</span>
                    <span class="news-author">{{ item.author }}</span>
                  </div>
                </div>
              </a-timeline-item>
            </a-timeline>
            <div class="news-more">
              <a-button type="link">查看更多</a-button>
            </div>
          </a-card>
        </a-col>

        <!-- 右侧：数据统计和待办 -->
        <a-col :lg="8" :xs="24">
          <a-row :gutter="[0, 16]">
            <!-- 统计数据 -->
            <a-col :span="24">
              <a-card class="stats-card" title="数据统计">
                <a-row :gutter="[8, 16]">
                  <a-col v-for="(stat, index) in statistics" :key="index" :span="12">
                    <div class="stat-item">
                      <div :style="{ backgroundColor: stat.bgColor }" class="stat-icon">
                        <Icon :icon="stat.icon"/>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stat.value }}</div>
                        <div class="stat-label">{{ stat.label }}</div>
                      </div>
                    </div>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>

            <!-- 待办事项 -->
            <a-col :span="24">
              <a-card class="todo-card" title="待办事项">
                <template #extra>
                  <a-button size="small" type="link">全部</a-button>
                </template>
                <a-list :data-source="todoList" size="small">
                  <template #renderItem="{ item }">
                    <a-list-item>
                      <div class="todo-item">
                        <div class="todo-content">
                          <a-badge :status="getTodoStatus(item.status)"/>
                          <span :class="{ 'todo-completed': item.status === 'done' }">{{ item.content }}</span>
                        </div>
                        <div class="todo-deadline">{{ item.deadline }}</div>
                      </div>
                    </a-list-item>
                  </template>
                </a-list>
              </a-card>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
    </div>

    <!-- 底部区域：常用应用 -->
    <div class="apps-section">
      <a-card class="apps-card" title="常用应用">
        <a-row :gutter="[16, 16]">
          <a-col v-for="(app, index) in apps" :key="index" :lg="4" :md="6" :sm="8" :xs="12">
            <div class="app-item" @click="openApp(app.url)">
              <div class="app-icon">
                <img :alt="app.name" :src="app.icon"/>
              </div>
              <div class="app-info">
                <div class="app-name">{{ app.name }}</div>
                <div class="app-desc">{{ app.description }}</div>
              </div>
            </div>
          </a-col>
        </a-row>
      </a-card>
    </div>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import {Icon} from '@iconify/vue'
import {message} from 'ant-design-vue'

// 搜索
const searchValue = ref('')
const onSearch = (value) => {
  message.info(`搜索: ${value}`)
}

// 快捷入口
const shortcuts = ref([
  {name: '人事管理', icon: 'ant-design:team-outlined', path: '/hr', bgColor: '#1890ff'},
  {name: '财务系统', icon: 'ant-design:account-book-outlined', path: '/finance', bgColor: '#52c41a'},
  {name: '项目管理', icon: 'ant-design:project-outlined', path: '/projects', bgColor: '#722ed1'},
  {name: '客户关系', icon: 'ant-design:contacts-outlined', path: '/crm', bgColor: '#fa8c16'},
  {name: '数据分析', icon: 'ant-design:bar-chart-outlined', path: '/analytics', bgColor: '#13c2c2'},
  {name: '知识库', icon: 'ant-design:book-outlined', path: '/knowledge', bgColor: '#eb2f96'},
  {name: '日程安排', icon: 'ant-design:calendar-outlined', path: '/schedule', bgColor: '#faad14'},
  {name: '系统设置', icon: 'ant-design:setting-outlined', path: '/settings', bgColor: '#8c8c8c'}
])

const navigateTo = (path) => {
  message.info(`导航到: ${path}`)
}

// 最新动态
const newsType = ref('all')
const news = ref([
  {
    title: '系统更新通知',
    content: '系统将于2024年6月15日晚上22:00-次日凌晨2:00进行版本升级，请提前做好相关工作安排。',
    time: '2024-06-10 10:30',
    author: '系统管理员',
    type: '系统'
  },
  {
    title: '新客户签约成功',
    content: '我司与ABC科技有限公司签订了为期三年的战略合作协议，将为其提供全面的技术支持服务。',
    time: '2024-06-09 16:45',
    author: '销售部 王经理',
    type: '业务'
  },
  {
    title: '人事变动公告',
    content: '张三同志自2024年7月1日起担任技术部经理一职，请各部门配合工作。',
    time: '2024-06-08 09:15',
    author: '人事部',
    type: '业务'
  },
  {
    title: '安全漏洞修复',
    content: '系统发现一个安全漏洞，已在最新补丁中修复，请各位尽快更新系统。',
    time: '2024-06-07 14:20',
    author: '技术支持',
    type: '系统'
  },
  {
    title: '财务报表提交通知',
    content: '请各部门于本月25日前提交6月份财务报表，逾期将影响绩效考核。',
    time: '2024-06-05 11:00',
    author: '财务部',
    type: '业务'
  }
])

const filteredNews = computed(() => {
  if (newsType.value === 'all') {
    return news.value
  } else if (newsType.value === 'system') {
    return news.value.filter(item => item.type === '系统')
  } else {
    return news.value.filter(item => item.type === '业务')
  }
})

const getNewsColor = (type) => {
  return type === '系统' ? 'blue' : 'green'
}

const getNewsTagColor = (type) => {
  return type === '系统' ? 'blue' : 'green'
}

// 数据统计
const statistics = ref([
  {label: '项目总数', value: '42', icon: 'ant-design:project-outlined', bgColor: 'rgba(24, 144, 255, 0.1)'},
  {label: '在线用户', value: '128', icon: 'ant-design:user-outlined', bgColor: 'rgba(82, 196, 26, 0.1)'},
  {label: '待处理', value: '23', icon: 'ant-design:file-exclamation-outlined', bgColor: 'rgba(250, 173, 20, 0.1)'},
  {label: '已完成', value: '198', icon: 'ant-design:check-circle-outlined', bgColor: 'rgba(82, 196, 26, 0.1)'}
])

// 待办事项
const todoList = ref([
  {content: '完成项目方案审核', deadline: '今天 18:00', status: 'processing'},
  {content: '参加部门周会', deadline: '明天 10:00', status: 'waiting'},
  {content: '提交季度工作总结', deadline: '本周五', status: 'processing'},
  {content: '更新客户资料', deadline: '已逾期', status: 'error'},
  {content: '完成系统测试', deadline: '已完成', status: 'done'}
])

const getTodoStatus = (status) => {
  const statusMap = {
    'processing': 'processing',
    'waiting': 'default',
    'error': 'error',
    'done': 'success'
  }
  return statusMap[status] || 'default'
}

// 常用应用
const apps = ref([
  {
    name: 'OA系统',
    description: '办公自动化',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png',
    url: '/oa'
  },
  {
    name: 'ERP系统',
    description: '企业资源计划',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png',
    url: '/erp'
  },
  {
    name: 'CRM系统',
    description: '客户关系管理',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/dURIMkkrRFpPgTuzkwnB.png',
    url: '/crm'
  },
  {
    name: '财务系统',
    description: '财务管理',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/sfjbOqnsXXJgNCjCzDBL.png',
    url: '/finance'
  },
  {
    name: '人事系统',
    description: '人力资源管理',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/siCrBXXhmvTQGWPNLBow.png',
    url: '/hr'
  },
  {
    name: '知识库',
    description: '企业知识管理',
    icon: 'https://gw.alipayobjects.com/zos/rmsportal/kZzEzemZyKLKFsojXItE.png',
    url: '/knowledge'
  }
])

const openApp = (url) => {
  message.info(`打开应用: ${url}`)
}
</script>

<style lang="scss" scoped>
.portal-container {
  padding: 16px;

  .search-section {
    margin-bottom: 16px;

    .search-card {
      background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
      color: white;

      .search-content {
        text-align: center;
        padding: 20px 0;

        .search-title {
          font-size: 28px;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .search-subtitle {
          font-size: 16px;
          margin-bottom: 24px;
          opacity: 0.8;
        }

        .search-box {
          max-width: 600px;
          margin: 0 auto;

          :deep(.ant-input) {
            height: 46px;
            border-radius: 4px 0 0 4px;
          }

          :deep(.ant-btn) {
            height: 46px;
            border-radius: 0 4px 4px 0;
          }
        }
      }
    }
  }

  .shortcuts-section {
    margin-bottom: 16px;

    .shortcuts-card {
      .shortcuts-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
        gap: 16px;

        .shortcut-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            transform: translateY(-3px);
          }

          .shortcut-icon {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 60px;
            height: 60px;
            border-radius: 12px;
            margin-bottom: 8px;
            color: white;
            font-size: 24px;
          }

          .shortcut-name {
            font-size: 14px;
            text-align: center;
          }
        }
      }
    }
  }

  .main-content {
    margin-bottom: 16px;

    .news-card {
      height: 100%;

      .news-item {
        margin-bottom: 8px;

        .news-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 4px;

          .news-title {
            font-weight: 500;
            font-size: 16px;
          }
        }

        .news-content {
          color: rgba(0, 0, 0, 0.65);
          margin-bottom: 4px;
        }

        .news-footer {
          display: flex;
          justify-content: space-between;
          font-size: 12px;
          color: rgba(0, 0, 0, 0.45);
        }
      }

      .news-more {
        text-align: center;
        margin-top: 16px;
      }
    }

    .stats-card {
      margin-bottom: 16px;

      .stat-item {
        display: flex;
        align-items: center;

        .stat-icon {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 40px;
          height: 40px;
          border-radius: 8px;
          margin-right: 12px;
          font-size: 20px;
          color: #1890ff;
        }

        .stat-info {
          .stat-value {
            font-size: 20px;
            font-weight: 500;
            line-height: 1;
            margin-bottom: 4px;
          }

          .stat-label {
            font-size: 12px;
            color: rgba(0, 0, 0, 0.45);
          }
        }
      }
    }

    .todo-card {
      .todo-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;

        .todo-content {
          display: flex;
          align-items: center;
          gap: 8px;
        }

        .todo-completed {
          text-decoration: line-through;
          color: rgba(0, 0, 0, 0.45);
        }

        .todo-deadline {
          font-size: 12px;
          color: rgba(0, 0, 0, 0.45);
        }
      }
    }
  }

  .apps-section {
    .apps-card {
      .app-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 16px;
        border-radius: 8px;
        border: 1px solid #f0f0f0;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          transform: translateY(-3px);
        }

        .app-icon {
          width: 48px;
          height: 48px;
          margin-bottom: 12px;

          img {
            width: 100%;
            height: 100%;
            object-fit: contain;
          }
        }

        .app-info {
          text-align: center;

          .app-name {
            font-weight: 500;
            margin-bottom: 4px;
          }

          .app-desc {
            font-size: 12px;
            color: rgba(0, 0, 0, 0.45);
          }
        }
      }
    }
  }
}

// 响应式调整
@media (max-width: 768px) {
  .portal-container {
    .shortcuts-section {
      .shortcuts-card {
        .shortcuts-grid {
          grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));

          .shortcut-icon {
            width: 50px;
            height: 50px;
            font-size: 20px;
          }

          .shortcut-name {
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>