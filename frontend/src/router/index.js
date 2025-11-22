import { createRouter, createWebHistory } from 'vue-router'
import SportsWatchForm from '../components/SportsWatchForm.vue'
import SportsWatchEditForm from '../components/SportsWatchEditForm.vue'
import SportsWatchList from '../components/SportsWatchList.vue'
import ProductGrid from '../components/ProductGrid.vue'
import SignupForm from '../components/SignupForm.vue'
import LoginForm from '../components/LoginForm.vue'

const routes = [
  {
    path: '/',
    redirect: '/list'
  },
  {
    path: '/signup',
    name: 'Signup',
    component: SignupForm
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginForm
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

// Navigation guard to check authentication
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken');
  const requiresAuth = to.path === '/add' || to.path.startsWith('/edit');

  if (requiresAuth && !isAuthenticated) {
    // Redirect to login if trying to access protected routes without authentication
    next('/login');
  } else {
    next();
  }
});

export default router
