import {defineStore} from 'pinia'

// 示例文章数据
const sampleArticles = [
    {
        id: '1',
        title: 'Vue 3 Composition API 完全指南',
        description: '深入理解Vue 3 Composition API的核心概念和最佳实践，帮助你更好地组织和复用组件逻辑。',
        excerpt: '探索Vue 3 Composition API的强大功能，学习如何使用它来构建更加灵活和可维护的Vue应用程序。',
        content: `# Vue 3 Composition API 完全指南

Vue 3 Composition API 是Vue.js 3.0版本引入的重要特性，它提供了一种更灵活的方式来组织组件逻辑。与Options API相比，Composition API具有更好的逻辑复用性和代码组织性。

## 什么是Composition API？

Composition API是一套新的API，允许我们使用函数的方式来组织组件逻辑。它的核心思想是将相关的逻辑组合在一起，而不是按照Options API的方式分散在不同的选项中。

## 基本用法

\`\`\`javascript
import { ref, reactive, computed, onMounted } from 'vue'

export default {
  setup() {
    // 响应式数据
    const count = ref(0)
    const user = reactive({
      name: 'Vue',
      age: 3
    })
    
    // 计算属性
    const doubleCount = computed(() => count.value * 2)
    
    // 方法
    const increment = () => {
      count.value++
    }
    
    // 生命周期
    onMounted(() => {
      console.log('组件已挂载')
    })
    
    return {
      count,
      user,
      doubleCount,
      increment
    }
  }
}
\`\`\`

## 优势

1. **更好的逻辑复用**：通过组合函数可以轻松复用逻辑
2. **更好的类型推导**：TypeScript支持更加完善
3. **更灵活的代码组织**：相关逻辑可以组织在一起
4. **更小的打包体积**：支持tree-shaking

## 总结

Composition API为Vue 3带来了更多的可能性，它不是Options API的替代品，而是一个补充。对于复杂的组件逻辑，Composition API提供了更好的解决方案。`,
        category: '技术',
        tags: ['Vue', 'JavaScript', '前端'],
        status: 'published',
        visibility: 'public',
        allowComments: true,
        featured: true,
        createdAt: '2023-12-15T10:30:00.000Z',
        updatedAt: '2023-12-20T14:45:00.000Z',
        author: 'SoulFrame',
        viewCount: 1250
    },
    {
        id: '2',
        title: '现代CSS布局技巧分享',
        description: '探索现代CSS布局技术，包括Flexbox、Grid和最新的布局特性，让你的网页设计更加灵活和美观。',
        excerpt: '掌握Flexbox和Grid布局，学会使用现代CSS技术创建响应式和灵活的网页布局。',
        content: `# 现代CSS布局技巧分享

CSS布局技术在过去几年中发生了巨大变化。从传统的浮动布局到现代的Flexbox和Grid，开发者有了更多选择。

## Flexbox布局

Flexbox是一维布局方法，用于在容器中排列项目。

\`\`\`css
.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.item {
  flex: 1;
}
\`\`\`

### 主要特性：
- 轻松实现垂直居中
- 灵活的空间分配
- 响应式设计友好

## Grid布局

Grid是二维布局系统，可以同时处理行和列。

\`\`\`css
.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}
\`\`\`

### 优势：
- 强大的二维布局能力
- 精确的网格控制
- 复杂布局的最佳选择

## 实用技巧

### 1. 完美居中
\`\`\`css
.center {
  display: grid;
  place-items: center;
}
\`\`\`

### 2. 响应式卡片布局
\`\`\`css
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1rem;
}
\`\`\`

## 总结

现代CSS布局技术为我们提供了强大的工具，合理使用这些技术可以创建出美观且响应式的网页布局。`,
        category: '教程',
        tags: ['CSS', '布局', '前端'],
        status: 'published',
        visibility: 'public',
        allowComments: true,
        featured: false,
        createdAt: '2023-12-10T09:15:00.000Z',
        updatedAt: '2023-12-12T16:20:00.000Z',
        author: 'SoulFrame',
        viewCount: 890
    },
    {
        id: '3',
        title: '响应式设计的未来趋势',
        description: '分析响应式设计的发展趋势，探讨容器查询、视口单位等新技术如何改变我们的设计方式。',
        excerpt: '了解响应式设计的最新发展，包括容器查询、新的视口单位和现代响应式设计模式。',
        content: `# 响应式设计的未来趋势

随着设备类型的不断增加，响应式设计变得越来越重要。传统的媒体查询虽然有效，但在某些场景下显得力不从心。

## 容器查询（Container Queries）

容器查询是响应式设计的一个重大突破，它允许我们基于容器的大小而不是视口的大小来应用样式。

\`\`\`css
.card-container {
  container-type: inline-size;
}

@container (min-width: 400px) {
  .card {
    display: flex;
    flex-direction: row;
  }
}
\`\`\`

## 新的视口单位

CSS引入了新的视口单位来解决移动端视口的问题：

- **dvh** (Dynamic Viewport Height)
- **lvh** (Large Viewport Height)  
- **svh** (Small Viewport Height)

\`\`\`css
.hero {
  height: 100dvh; /* 动态视口高度 */
}
\`\`\`

## 现代响应式模式

### 1. Intrinsic Web Design
基于内容的自适应设计，而不是固定的断点。

### 2. Component-Based Responsive
组件级别的响应式设计，每个组件都有自己的响应式逻辑。

### 3. Progressive Enhancement
从最基本的体验开始，逐步增强功能。

## 实践建议

1. **移动优先**：始终从移动端开始设计
2. **内容优先**：让内容决定断点，而不是设备
3. **性能考虑**：优化图片和资源加载
4. **可访问性**：确保所有用户都能良好使用

## 工具推荐

- **CSS Grid Generator**：快速生成Grid布局
- **Flexbox Playground**：学习和测试Flexbox
- **Responsive Design Checker**：测试响应式效果

## 总结

响应式设计正在向更加智能和灵活的方向发展。容器查询、新的视口单位等技术为我们提供了更强大的工具，让我们能够创建更好的用户体验。`,
        category: '设计',
        tags: ['响应式', '设计', '趋势'],
        status: 'published',
        visibility: 'public',
        allowComments: true,
        featured: true,
        createdAt: '2023-12-05T11:20:00.000Z',
        updatedAt: '2023-12-08T13:45:00.000Z',
        author: 'SoulFrame',
        viewCount: 654
    }
]

export const useArticleStore = defineStore('article', {
    state: () => {
        // 尝试从本地存储加载数据
        const savedArticles = localStorage.getItem('blog-articles')
        let articles = []

        if (savedArticles) {
            try {
                articles = JSON.parse(savedArticles)
            } catch (error) {
                articles = [...sampleArticles]
            }
        } else {
            articles = [...sampleArticles]
        }

        return {
            articles: articles,
            currentArticle: null
        }
    },

    actions: {
        // 初始化示例数据
        initSampleData() {
            // 如果没有数据，重新加载示例数据
            if (this.articles.length === 0) {
                this.articles = [...sampleArticles]
            }
            // 保存到本地存储
            localStorage.setItem('blog-articles', JSON.stringify(this.articles))
        },

        saveArticle(article) {
            if (!article.id) {
                article.id = Date.now().toString()
                article.createdAt = new Date().toISOString()
                this.articles.push(article)
            } else {
                const index = this.articles.findIndex(a => a.id === article.id)
                if (index !== -1) {
                    article.updatedAt = new Date().toISOString()
                    this.articles[index] = article
                }
            }

            // 保存到本地存储
            localStorage.setItem('blog-articles', JSON.stringify(this.articles))
            return article
        },

        deleteArticle(id) {
            const index = this.articles.findIndex(a => a.id === id)
            if (index !== -1) {
                this.articles.splice(index, 1)
                localStorage.setItem('blog-articles', JSON.stringify(this.articles))
                return true
            }
            return false
        },

        getArticle(id) {
            return this.articles.find(a => a.id === id) || null
        },

        getArticleById(id) {
            return this.getArticle(id)
        }
    }
}) 