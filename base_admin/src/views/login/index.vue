<template>
  <div class="login-container">
    <div class="login-background">
      <div class="wave"></div>
      <div class="wave"></div>
      <div class="wave"></div>
    </div>
    <div class="login-content">
      <div class="login-box">
        <div class="login-header">
          <h1>Base Admin</h1>
          <p>基于 Vue3 + Vite + Ant Design Vue 的后台管理系统</p>
        </div>
        <a-form :model="formState" :rules="formRules" name="loginForm" @finish="handleSubmit" autocomplete="off">
          <a-form-item name="username">
            <a-input v-model:value="formState.username" size="large" placeholder="用户名" :maxLength="20">
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password">
            <a-input-password v-model:value="formState.password" size="large" placeholder="密码" :maxLength="20">
              <template #prefix>
                <LockOutlined />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item name="captcha">
            <a-row :gutter="16">
              <a-col :span="16">
                <a-input v-model:value="formState.captcha" size="large" placeholder="验证码" :maxLength="4">
                  <template #prefix>
                    <SafetyOutlined />
                  </template>
                </a-input>
              </a-col>
              <a-col :span="8">
                <div class="captcha-img" @click="refreshCaptcha">
                  <img :src="captchaInfo.captchaSvg" alt="验证码" />
                </div>
              </a-col>
            </a-row>
          </a-form-item>

          <a-form-item>
            <a-row :gutter="16">
              <a-col :span="12">
                <a-checkbox v-model:checked="formState.remember">
                  记住密码
                </a-checkbox>
              </a-col>
              <a-col :span="12" style="text-align: right">
                <a class="forgot-link" @click="handleForgotPassword">忘记密码？</a>
              </a-col>
            </a-row>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" html-type="submit" size="large" :loading="loading" block>
              登录
            </a-button>
          </a-form-item>

          <div class="other-login">
            <div class="divider">
              <span>其他登录方式</span>
            </div>
            <div class="icons">
              <a-tooltip title="Github">
                <GithubOutlined class="icon" @click="handleThirdPartyLogin('Github')" />
              </a-tooltip>
              <a-tooltip title="微信">
                <WechatOutlined class="icon" @click="handleThirdPartyLogin('微信')" />
              </a-tooltip>
              <a-tooltip title="钉钉">
                <DingdingOutlined class="icon" @click="handleThirdPartyLogin('钉钉')" />
              </a-tooltip>
            </div>
          </div>
        </a-form>
      </div>
    </div>
    <div class="footer">
      <p>Copyright © {{ new Date().getFullYear() }} Base Admin</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import {
  DingdingOutlined,
  GithubOutlined,
  LockOutlined,
  SafetyOutlined,
  UserOutlined,
  WechatOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useRoute, useRouter } from 'vue-router'
// import { getCaptcha } from '@/api/modules/auth'
// import { useAuthStore } from '@/store/modules/auth'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
// const authStore = useAuthStore()

// 验证码相关状态
const captchaInfo = ref({
  captchaSvg: '',
  captchaCode: ''
})

// 表单状态
const formState = reactive({
  username: 'admin',
  password: '123456',
  captcha: 'AHAH',
  remember: true
})

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在 3-20 个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在 6-20 个字符之间', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码长度应为 4 位', trigger: 'blur' }
  ]
}

// 获取验证码
const refreshCaptcha = async () => {
  try {
    // const res = await getCaptcha()
    // captchaInfo.value = res.data
  } catch (error) {
    message.error('获取验证码失败，请重试')
  }
}

// 检查是否有记住的密码
const checkRemembered = () => {
  try {
    const remembered = localStorage.getItem('remembered')
    if (remembered) {
      const { username, password } = JSON.parse(remembered)
      formState.username = username
      formState.password = password
      formState.remember = true
    }
  } catch (error) {
    console.error('读取记住的密码失败:', error)
    localStorage.removeItem('remembered')
  }
}

// 记住密码处理
const handleRememberPassword = () => {
  if (formState.remember) {
    localStorage.setItem('remembered', JSON.stringify({
      username: formState.username,
      password: formState.password
    }))
  } else {
    localStorage.removeItem('remembered')
  }
}

// 处理第三方登录
const handleThirdPartyLogin = (type) => {
  message.info(`${type}登录功能开发中...`)
}

// 处理忘记密码
const handleForgotPassword = () => {
  message.info('忘记密码功能开发中...')
}

// 提交表单
const handleSubmit = async () => {
  try {
    loading.value = true
    // await authStore.login({
    //   username: formState.username,
    //   password: formState.password,
    //   code: formState.captcha,
    //   uuid: captchaInfo.value.captchaCode
    // })

    handleRememberPassword()
    message.success('登录成功')

    // 跳转到来源页面或首页
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (error) {
    message.error(error.message || '登录失败')

    console.error('登录失败:', error)
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

// 按键处理
const handleKeyPress = (e) => {
  if (e.key === 'Enter') {
    handleSubmit()
  }
}

onMounted(() => {
  refreshCaptcha()
  checkRemembered()
  // 添加键盘事件监听
  window.addEventListener('keypress', handleKeyPress)
})

onUnmounted(() => {
  // 移除键盘事件监听
  window.removeEventListener('keypress', handleKeyPress)
})
</script>

<style lang="scss" scoped>
// 变量定义
$primary-color: #1890ff;
$secondary-color: #722ed1;
$white: #ffffff;
$text-color: rgba(0, 0, 0, 0.65);
$text-secondary: rgba(0, 0, 0, 0.45);
$border-color: #d9d9d9;
$shadow-color: rgba(0, 0, 0, 0.15);

// Mixins
@mixin gradient-bg {
  background: linear-gradient(135deg, $primary-color 0%, $secondary-color 100%);
}

@mixin text-gradient {
  background: linear-gradient(135deg, $primary-color 0%, $secondary-color 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

@mixin hover-transform {
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-1px);
  }
  
  &:active {
    transform: translateY(0);
  }
}

@mixin glass-effect {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 8px 24px $shadow-color;
}

.login-container {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  @include gradient-bg;

  .login-background {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 0;

    .wave {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 100px;
      background: url('@/assets/login-bg.svg') repeat-x;
      animation: wave 10s linear infinite;

      &:nth-child(2) {
        bottom: 10px;
        opacity: 0.5;
        animation: wave 7s linear infinite;
      }

      &:nth-child(3) {
        bottom: 20px;
        opacity: 0.2;
        animation: wave 4s linear infinite;
      }
    }
  }

  .login-content {
    position: relative;
    z-index: 1;
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px 0;
  }

  .login-box {
    width: 400px;
    padding: 40px;
    @include glass-effect;
    animation: fadeIn 0.5s ease-out;
  }

  .login-header {
    text-align: center;
    margin-bottom: 40px;

    h1 {
      margin: 0;
      font-size: 28px;
      font-weight: 600;
      @include text-gradient;
    }

    p {
      margin: 12px 0 0;
      font-size: 14px;
      color: $text-secondary;
    }
  }

  // Ant Design 组件样式覆盖
  :deep(.ant-input-affix-wrapper) {
    background: transparent;
    border-radius: 4px;
    transition: all 0.3s;

    &:hover,
    &:focus {
      border-color: $primary-color;
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
    }

    .anticon {
      color: #bfbfbf;
      transition: color 0.3s;
    }

    &:hover .anticon {
      color: $primary-color;
    }

    input.ant-input {
      background: transparent;
    }
  }

  .captcha-img {
    height: 40px;
    border: 1px solid $border-color;
    border-radius: 4px;
    cursor: pointer;
    overflow: hidden;
    transition: all 0.3s;

    &:hover {
      border-color: $primary-color;
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
    }

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .forgot-link {
    color: $primary-color;
    transition: all 0.3s;
    cursor: pointer;

    &:hover {
      color: #40a9ff;
      text-decoration: underline;
    }
  }

  :deep(.ant-btn) {
    height: 40px;
    font-size: 16px;
    border-radius: 4px;
    transition: all 0.3s;

    &.ant-btn-primary {
      @include gradient-bg;
      border: none;
      @include hover-transform;

      &:hover {
        box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
      }
    }
  }

  :deep(.ant-checkbox-wrapper) {
    color: $text-color;
  }

  .other-login {
    margin-top: 24px;
    text-align: center;

    .divider {
      position: relative;
      margin: 16px 0;
      color: $text-secondary;

      &::before,
      &::after {
        content: '';
        position: absolute;
        top: 50%;
        width: 30%;
        height: 1px;
        background: rgba(0, 0, 0, 0.06);
      }

      &::before {
        left: 0;
      }

      &::after {
        right: 0;
      }

      span {
        padding: 0 24px;
        background: rgba(255, 255, 255, 0.95);
      }
    }

    .icons {
      display: flex;
      justify-content: center;
      gap: 24px;

      .icon {
        font-size: 24px;
        color: $text-secondary;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          color: $primary-color;
          transform: scale(1.2);
        }
      }
    }
  }

  .footer {
    position: relative;
    z-index: 1;
    text-align: center;
    padding: 24px;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 动画定义
@keyframes wave {
  0% {
    background-position-x: 0;
  }

  100% {
    background-position-x: 1440px;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .login-container {
    .login-box {
      width: 90%;
      padding: 30px 20px;
      margin: 0 20px;
    }

    .login-header {
      h1 {
        font-size: 24px;
      }
    }

    .other-login {
      .icons {
        gap: 16px;

        .icon {
          font-size: 20px;
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .login-container {
    .login-content {
      padding: 20px 0;
    }

    .login-box {
      padding: 20px;
    }

    .login-header {
      margin-bottom: 30px;

      h1 {
        font-size: 20px;
      }

      p {
        font-size: 12px;
      }
    }
  }
}
</style>
