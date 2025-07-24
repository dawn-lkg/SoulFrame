<template>
  <div class="dashboard-container">
    <!-- 顶部信息卡片 -->
    <a-row :gutter="[16, 16]" class="top-section">
      <!-- 欢迎和天气卡片 -->
      <a-col :lg="8" :md="12" :sm="24" :xs="24">
        <a-card class="welcome-card dashboard-card">
          <div class="weather-time-wrapper">
            <div class="welcome-info">
              <h2>{{ greeting }}，{{ userInfo.name }}</h2>
              <p class="date-info">{{ currentDate }}</p>
            </div>
            <div class="weather-info">
              <CloudOutlined v-if="weather.type === 'cloudy'" class="weather-icon"/>
              <FireOutlined v-else-if="weather.type === 'sunny'" class="weather-icon"/>
              <ThunderboltOutlined v-else-if="weather.type === 'storm'" class="weather-icon"/>
              <CloudDownloadOutlined v-else class="weather-icon"/>
              <div class="weather-detail">
                <div class="temperature">{{ weather.temperature }}°C</div>
                <div class="location">{{ weather.location }}</div>
              </div>
            </div>
          </div>
          <div class="time-display">{{ currentTime }}</div>
        </a-card>
      </a-col>

      <!-- 系统状态卡片 -->
      <a-col :lg="8" :md="12" :sm="24" :xs="24">
        <a-card :bordered="false" class="dashboard-card status-card" title="系统状态">
          <a-row :gutter="[0, 16]">
            <a-col :span="12">
              <a-progress :format="percent => `${percent}%`" :percent="systemStatus.cpu" :width="80" type="dashboard"/>
              <div class="status-label">CPU使用率</div>
            </a-col>
            <a-col :span="12">
              <a-progress :format="percent => `${percent}%`" :percent="systemStatus.memory" :width="80"
                          type="dashboard"/>
              <div class="status-label">内存使用率</div>
            </a-col>
          </a-row>
          <a-divider style="margin: 16px 0"/>
          <a-row>
            <a-col :span="12" class="status-item">
              <div class="status-value">{{ systemStatus.onlineUsers }}</div>
              <div class="status-label">在线用户</div>
            </a-col>
            <a-col :span="12" class="status-item">
              <div class="status-value">{{ systemStatus.uptime }}</div>
              <div class="status-label">运行时间(小时)</div>
            </a-col>
          </a-row>
        </a-card>
      </a-col>

      <!-- 快捷操作卡片 -->
      <a-col :lg="8" :md="24" :sm="24" :xs="24">
        <a-card :bordered="false" class="dashboard-card" title="快捷操作">
          <div class="quick-actions">
            <a-button class="action-button" type="primary" @click="handleAction('create')">
              <PlusOutlined/>
              创建任务
            </a-button>
            <a-button class="action-button" @click="handleAction('message')">
              <MessageOutlined/>
              发送消息
            </a-button>
            <a-button class="action-button" @click="handleAction('report')">
              <FileTextOutlined/>
              生成报表
            </a-button>
            <a-button class="action-button" @click="handleAction('settings')">
              <SettingOutlined/>
              系统设置
            </a-button>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 中间内容区 -->
    <a-row :gutter="[16, 16]" class="middle-section">
      <!-- 待办事项 -->
      <a-col :lg="8" :md="12" :xs="24">
        <a-card :bordered="false" class="dashboard-card todo-card" title="待办事项">
          <template #extra>
            <a-button size="small" type="link" @click="handleAddTodo">
              <PlusOutlined/>
              添加
            </a-button>
          </template>
          <a-list :data-source="todoList" class="todo-list" size="small">
            <template #renderItem="{ item }">
              <a-list-item class="todo-item">
                <a-checkbox
                    :checked="item.completed"
                    @change="(e) => toggleTodo(item.id, e.target.checked)"
                >
                  <span :class="{ 'completed': item.completed }">{{ item.content }}</span>
                </a-checkbox>
                <template #actions>
                  <a-tag :color="getPriorityColor(item.priority)">{{ item.priority }}</a-tag>
                  <a-button size="small" type="text" @click="deleteTodo(item.id)">
                    <DeleteOutlined/>
                  </a-button>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>

      <!-- 系统公告 -->
      <a-col :lg="8" :md="12" :xs="24">
        <a-card :bordered="false" class="dashboard-card announcement-card" title="系统公告">
          <template #extra>
            <a-dropdown>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="all">全部公告</a-menu-item>
                  <a-menu-item key="important">重要公告</a-menu-item>
                </a-menu>
              </template>
              <a-button size="small" type="link">
                筛选
                <DownOutlined/>
              </a-button>
            </a-dropdown>
          </template>
          <a-list :data-source="announcements" class="announcement-list" size="small">
            <template #renderItem="{ item }">
              <a-list-item class="announcement-item">
                <a-list-item-meta>
                  <template #title>
                    <div class="announcement-title">
                      <a-tag v-if="item.important" color="red">重要</a-tag>
                      {{ item.title }}
                    </div>
                  </template>
                  <template #description>
                    <div class="announcement-content">{{ item.content }}</div>
                    <div class="announcement-footer">
                      <span><CalendarOutlined/> {{ item.date }}</span>
                      <span><UserOutlined/> {{ item.publisher }}</span>
                    </div>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>

      <!-- 系统消息 -->
      <a-col :lg="8" :md="24" :xs="24">
        <a-card :bordered="false" class="dashboard-card message-card" title="系统消息">
          <template #extra>
            <a-button size="small" type="link" @click="markAllRead">全部已读</a-button>
          </template>
          <a-tabs default-active-key="unread">
            <a-tab-pane key="unread" tab="未读消息">
              <a-list :data-source="unreadMessages" class="message-list" size="small">
                <template #renderItem="{ item }">
                  <a-list-item class="message-item">
                    <a-list-item-meta>
                      <template #avatar>
                        <a-badge v-if="!item.read" dot>
                          <a-avatar :icon="getMessageIcon(item.type)"
                                    :style="{ backgroundColor: getMessageColor(item.type) }"/>
                        </a-badge>
                        <a-avatar v-else :icon="getMessageIcon(item.type)"
                                  :style="{ backgroundColor: getMessageColor(item.type) }"/>
                      </template>
                      <template #title>{{ item.title }}</template>
                      <template #description>
                        <div class="message-content">{{ item.content }}</div>
                        <div class="message-time">
                          <ClockCircleOutlined/>
                          {{ item.time }}
                        </div>
                      </template>
                    </a-list-item-meta>
                    <template #actions>
                      <a-button size="small" type="link" @click="markAsRead(item.id)">标为已读</a-button>
                    </template>
                  </a-list-item>
                </template>
              </a-list>
            </a-tab-pane>
            <a-tab-pane key="all" tab="全部消息">
              <a-list :data-source="allMessages" class="message-list" size="small">
                <template #renderItem="{ item }">
                  <a-list-item class="message-item">
                    <a-list-item-meta>
                      <template #avatar>
                        <a-badge v-if="!item.read" dot>
                          <a-avatar :icon="getMessageIcon(item.type)"
                                    :style="{ backgroundColor: getMessageColor(item.type) }"/>
                        </a-badge>
                        <a-avatar v-else :icon="getMessageIcon(item.type)"
                                  :style="{ backgroundColor: getMessageColor(item.type) }"/>
                      </template>
                      <template #title>{{ item.title }}</template>
                      <template #description>
                        <div class="message-content">{{ item.content }}</div>
                        <div class="message-time">
                          <ClockCircleOutlined/>
                          {{ item.time }}
                        </div>
                      </template>
                    </a-list-item-meta>
                  </a-list-item>
                </template>
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import {computed, onMounted, onUnmounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {message} from 'ant-design-vue';
import {
  BellOutlined,
  CalendarOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined,
  CloudDownloadOutlined,
  CloudOutlined,
  DeleteOutlined,
  DownOutlined,
  FileTextOutlined,
  FireOutlined,
  InfoCircleOutlined,
  MessageOutlined,
  PlusOutlined,
  SettingOutlined,
  ThunderboltOutlined,
  UserOutlined,
  WarningOutlined
} from '@ant-design/icons-vue';

const router = useRouter();

// 用户信息
const userInfo = ref({
  name: '管理员',
  lastLogin: '2024-01-15 09:30:00'
});

// 问候语
const greeting = computed(() => {
  const hour = new Date().getHours();
  if (hour < 6) return '凌晨好';
  if (hour < 9) return '早上好';
  if (hour < 12) return '上午好';
  if (hour < 14) return '中午好';
  if (hour < 17) return '下午好';
  if (hour < 19) return '傍晚好';
  return '晚上好';
});

// 当前时间和日期
const currentTime = ref('');
const currentDate = ref('');
let timeInterval = null;

// 更新时间
const updateTime = () => {
  const now = new Date();
  const pad = (n) => n.toString().padStart(2, '0');

  // 更新时间
  currentTime.value = `${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;

  // 更新日期
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
  currentDate.value = `${now.getFullYear()}年${pad(now.getMonth() + 1)}月${pad(now.getDate())}日 ${weekdays[now.getDay()]}`;
};

// 天气信息
const weather = ref({
  type: 'sunny',
  temperature: 26,
  location: '北京市'
});

// 系统状态
const systemStatus = ref({
  cpu: 32.5,
  memory: 45.8,
  onlineUsers: 128,
  uptime: 72
});

// 待办事项
const todoList = ref([
  {id: 1, content: '完成系统更新', completed: false, priority: '高'},
  {id: 2, content: '审核新用户申请', completed: false, priority: '中'},
  {id: 3, content: '准备周报', completed: true, priority: '中'},
  {id: 4, content: '备份数据库', completed: false, priority: '高'},
  {id: 5, content: '更新文档', completed: false, priority: '低'}
]);

// 系统公告
const announcements = ref([
  {
    id: 1,
    title: '系统升级通知',
    content: '系统将于本周六凌晨2:00-4:00进行升级维护，请提前做好准备。',
    date: '2024-06-10',
    publisher: '系统管理员',
    important: true
  },
  {
    id: 2,
    title: '新功能上线',
    content: '数据分析模块已更新，新增多维度图表展示功能。',
    date: '2024-06-08',
    publisher: '产品部',
    important: false
  },
  {
    id: 3,
    title: '安全更新提醒',
    content: '请所有用户及时修改密码，确保账号安全。',
    date: '2024-06-05',
    publisher: '安全部',
    important: true
  }
]);

// 系统消息
const allMessages = ref([
  {
    id: 1,
    title: '登录提醒',
    content: '您的账号刚刚在新设备上登录',
    time: '10分钟前',
    read: false,
    type: 'warning'
  },
  {
    id: 2,
    title: '任务完成',
    content: '数据库备份任务已完成',
    time: '30分钟前',
    read: true,
    type: 'success'
  },
  {
    id: 3,
    title: '系统通知',
    content: '您有3个待处理的工单',
    time: '1小时前',
    read: false,
    type: 'info'
  },
  {
    id: 4,
    title: '安全警告',
    content: '检测到异常登录尝试',
    time: '2小时前',
    read: false,
    type: 'warning'
  }
]);

// 未读消息
const unreadMessages = computed(() => {
  return allMessages.value.filter(msg => !msg.read);
});

// 获取优先级颜色
const getPriorityColor = (priority) => {
  switch (priority) {
    case '高':
      return 'red';
    case '中':
      return 'orange';
    case '低':
      return 'blue';
    default:
      return 'blue';
  }
};

// 获取消息图标
const getMessageIcon = (type) => {
  switch (type) {
    case 'warning':
      return WarningOutlined;
    case 'success':
      return CheckCircleOutlined;
    case 'info':
      return InfoCircleOutlined;
    default:
      return BellOutlined;
  }
};

// 获取消息颜色
const getMessageColor = (type) => {
  switch (type) {
    case 'warning':
      return '#faad14';
    case 'success':
      return '#52c41a';
    case 'info':
      return '#1890ff';
    default:
      return '#1890ff';
  }
};

// 切换待办事项状态
const toggleTodo = (id, checked) => {
  const todo = todoList.value.find(item => item.id === id);
  if (todo) {
    todo.completed = checked;
  }
};

// 删除待办事项
const deleteTodo = (id) => {
  todoList.value = todoList.value.filter(item => item.id !== id);
  message.success('删除成功');
};

// 添加待办事项
const handleAddTodo = () => {
  message.info('添加待办功能开发中...');
};

// 标记消息为已读
const markAsRead = (id) => {
  const msg = allMessages.value.find(item => item.id === id);
  if (msg) {
    msg.read = true;
    message.success('已标记为已读');
  }
};

// 标记所有消息为已读
const markAllRead = () => {
  allMessages.value.forEach(msg => {
    msg.read = true;
  });
  message.success('全部标记为已读');
};

// 处理快捷操作
const handleAction = (action) => {
  switch (action) {
    case 'create':
      message.info('创建任务功能开发中...');
      break;
    case 'message':
      message.info('发送消息功能开发中...');
      break;
    case 'report':
      message.info('生成报表功能开发中...');
      break;
    case 'settings':
      router.push('/settings');
      break;
  }
};

// 生命周期钩子
onMounted(() => {
  // 初始化时间
  updateTime();
  timeInterval = setInterval(updateTime, 1000);

  // 这里可以添加获取天气、系统状态等API调用
});

onUnmounted(() => {
  // 清除定时器
  if (timeInterval) clearInterval(timeInterval);
});
</script>

<style scoped>
.dashboard-container {
  padding: 24px;
  background-color: var(--bg-color, #f0f2f5);
  min-height: 100vh;
}

.top-section,
.middle-section {
  margin-bottom: 24px;
}

/* 卡片通用样式 */
.dashboard-card {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.dashboard-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 欢迎卡片样式 - 多种颜色选项 */
/* 选项1: 紫色渐变 */
.welcome-card {
  background: linear-gradient(135deg, #8e2de2, #4a00e0);
  color: white;
}

/* 选项2: 绿色渐变 */
/*.welcome-card {
  background: linear-gradient(135deg, #11998e, #38ef7d);
  color: white;
}*/

/* 选项3: 橙色渐变 (温暖色调) */
/* .welcome-card {
  background: linear-gradient(135deg, #f2994a, #f2c94c);
  color: white;
} */

/* 选项4: 粉色渐变 */
/*.welcome-card {
  background: linear-gradient(135deg, #ee9ca7, #ffdde1);
  color: white;
}*/


.weather-time-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.welcome-info h2 {
  font-size: 24px;
  margin-bottom: 8px;
  color: white;
}

.date-info {
  color: rgba(255, 255, 255, 0.85);
  font-size: 14px;
}

.weather-info {
  display: flex;
  align-items: center;
}

.weather-icon {
  font-size: 36px;
  color: white;
  margin-right: 10px;
}

.temperature {
  font-size: 24px;
  font-weight: 500;
  color: white;
}

.location {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
}

.time-display {
  font-size: 36px;
  font-weight: 300;
  text-align: center;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 系统状态卡片 */
.status-card :deep(.ant-card-head) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.status-item {
  text-align: center;
}

.status-value {
  font-size: 24px;
  font-weight: 500;
  color: var(--primary-color, #1890ff);
}

.status-label {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  text-align: center;
  margin-top: 8px;
}

/* 快捷操作样式 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.action-button {
  height: 40px;
  border-radius: 4px;
  transition: all 0.3s;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

/* 待办事项样式 */
.todo-list {
  max-height: 300px;
  overflow-y: auto;
}

.todo-item {
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.todo-item:last-child {
  border-bottom: none;
}

.completed {
  text-decoration: line-through;
  color: rgba(0, 0, 0, 0.45);
}

/* 公告样式 */
.announcement-list {
  max-height: 300px;
  overflow-y: auto;
}

.announcement-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  font-weight: 500;
  margin-bottom: 4px;
}

.announcement-content {
  color: rgba(0, 0, 0, 0.65);
  margin-bottom: 8px;
  line-height: 1.5;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  color: rgba(0, 0, 0, 0.45);
  font-size: 12px;
}

/* 消息样式 */
.message-list {
  max-height: 300px;
  overflow-y: auto;
}

.message-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.message-item:last-child {
  border-bottom: none;
}

.message-content {
  color: rgba(0, 0, 0, 0.65);
  line-height: 1.5;
}

.message-time {
  color: rgba(0, 0, 0, 0.45);
  font-size: 12px;
  margin-top: 4px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .dashboard-container {
    padding: 16px;
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .time-display {
    font-size: 28px;
  }

  .weather-icon {
    font-size: 28px;
  }

  .temperature {
    font-size: 20px;
  }

  .status-value {
    font-size: 20px;
  }
}

/* 暗色模式适配 */
.dark-mode :deep(.dashboard-card) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.dark-mode :deep(.status-label) {
  color: rgba(255, 255, 255, 0.45);
}

.dark-mode :deep(.todo-item),
.dark-mode :deep(.announcement-item),
.dark-mode :deep(.message-item) {
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

.dark-mode :deep(.announcement-content),
.dark-mode :deep(.message-content) {
  color: rgba(255, 255, 255, 0.65);
}

.dark-mode :deep(.announcement-footer),
.dark-mode :deep(.message-time) {
  color: rgba(255, 255, 255, 0.45);
}

.dark-mode :deep(.completed) {
  color: rgba(255, 255, 255, 0.45);
}
</style>
