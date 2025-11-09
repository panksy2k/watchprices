import { createRouter, createWebHistory } from 'vue-router'
import SportsWatchForm from '../components/SportsWatchForm.vue'
import SportsWatchEditForm from '../components/SportsWatchEditForm.vue'
import SportsWatchList from '../components/SportsWatchList.vue'
import ProductGrid from '../components/ProductGrid.vue'

const routes = [
  {
    path: '/',
    redirect: '/list'
  },
  {
    path: '/add',
    name: 'AddSportsWatch',
    component: SportsWatchForm
  },
  {
    path: '/edit/:productType/:id',
    name: 'EditSportsWatch',
    component: SportsWatchEditForm,
    props: true
  },
  {
    path: '/list',
    name: 'ListSportsWatch',
    component: SportsWatchList
  },
  {
    path: '/grid',
    name: 'ProductGrid',
    component: ProductGrid
  },
  {
    path: '/grid/:productType',
    name: 'ProductGridWithType',
    component: ProductGrid
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router