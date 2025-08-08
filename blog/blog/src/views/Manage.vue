<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {useArticleStore} from '../store'
import {useUserStore} from '../store/user'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Delete, Edit, Plus, View} from '@element-plus/icons-vue'

const router = useRouter()
const articleStore = useArticleStore()
const userStore = useUserStore()
const loading = ref(false)
const searchQuery = ref('')

const articles = computed(() => {
  return articleStore.articles.slice().sort((a, b) => {
    const dateA = a.updatedAt || a.createdAt
    const dateB = b.updatedAt || b.createdAt
    return new Date(dateB) - new Date(dateA)
  })
})

const filteredArticles = computed(() => {
  if (!searchQuery.value) return articles.value

  const query = searchQuery.value.toLowerCase()
  return articles.value.filter(article =>
      article.title.toLowerCase().includes(query) ||
      (article.description && article.description.toLowerCase().includes(query)) ||
      (article.category && article.category.toLowerCase().includes(query)) ||
      article.content.toLowerCase().includes(query)
  )
})

const editArticle = (id) => {
  router.push(`/editor?id=${id}`)
}

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const deleteArticle = (id) => {
  ElMessageBox.confirm('确定要删除这篇文章吗？此操作不可逆', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const success = articleStore.deleteArticle(id)
    if (success) {
      ElMessage.success('文章已删除')
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
  })
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loading.value = true
  // 模拟异步加载
  setTimeout(() => {
    loading.value = false
  }, 300)
})
</script>

<template>
  <div class="manage-container">
    <div class="manage-header">
      <h2>文章管理</h2>
      <div class="manage-actions">
        <el-input
            v-model="searchQuery"
            clearable
            placeholder="搜索文章"
            prefix-icon="Search"
        >
        </el-input>
        <el-button type="primary" @click="router.push('/editor')">
          <el-icon>
            <Plus/>
          </el-icon>
          新建文章
        </el-button>
      </div>
    </div>

    <el-divider/>

    <el-skeleton v-if="loading" :rows="5" animated/>

    <el-table
        v-else-if="filteredArticles.length > 0"
        :data="filteredArticles"
        border
        style="width: 100%"
    >
      <el-table-column label="标题" min-width="200" prop="title">
        <template #default="scope">
          <div class="table-title" @click="viewArticle(scope.row.id)">
            {{ scope.row.title }}
          </div>
        </template>
      </el-table-column>

      <el-table-column label="分类" prop="category" width="120">
        <template #default="scope">
          <el-tag effect="light" size="small">{{ scope.row.category || '未分类' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>

      <el-table-column label="更新时间" width="180">
        <template #default="scope">
          {{ scope.row.updatedAt ? formatDate(scope.row.updatedAt) : '-' }}
        </template>
      </el-table-column>

      <el-table-column label="作者" prop="author" width="120">
        <template #default="scope">
          {{ scope.row.author }}
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" width="160">
        <template #default="scope">
          <div class="table-actions">
            <el-button
                :icon="Edit"
                plain
                size="small"
                title="编辑"
                type="primary"
                @click="editArticle(scope.row.id)"
            />
            <el-button
                :icon="View"
                plain
                size="small"
                title="查看"
                type="success"
                @click="viewArticle(scope.row.id)"
            />
            <el-button
                :icon="Delete"
                plain
                size="small"
                title="删除"
                type="danger"
                @click="deleteArticle(scope.row.id)"
            />
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-empty
        v-else-if="searchQuery"
        description="未找到符合条件的文章"
    />

    <el-empty
        v-else
        description="暂无文章"
    >
      <el-button type="primary" @click="router.push('/editor')">
        立即创建
      </el-button>
    </el-empty>
  </div>
</template>

<style scoped>
.manage-container {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.manage-header h2 {
  margin: 0;
}

.manage-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.table-title {
  cursor: pointer;
  color: #409EFF;
}

.table-title:hover {
  text-decoration: underline;
}

.table-actions {
  display: flex;
  gap: 5px;
}

:deep(.el-input__wrapper) {
  width: 250px;
}
</style>