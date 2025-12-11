<template>
  <div id="app">
    <header class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
      <div class="container-fluid">
        <router-link class="navbar-brand mb-0 h1 text-decoration-none" to="/">
          Sports Watch Manager
        </router-link>

        <button class="navbar-toggler" data-bs-target="#navbarNav" data-bs-toggle="collapse" type="button">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!--        <div class="collapse navbar-collapse" id="navbarNav">
                  <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                      <router-link to="/add" class="nav-link" active-class="active">
                        <i class="bi bi-plus-circle"></i> Add Watch
                      </router-link>
                    </li>
                    <li class="nav-item">
                      <router-link to="/list" class="nav-link" active-class="active">
                        <i class="bi bi-list"></i> View Collection
                      </router-link>
                    </li>
                    <li class="nav-item">
                      <router-link to="/grid" class="nav-link" active-class="active">
                        <i class="bi bi-grid-3x3"></i> Product Grid
                      </router-link>
                    </li>
                    <li class="nav-item" v-if="!isAuthenticated">
                      <router-link to="/login" class="nav-link" active-class="active">
                        <i class="bi bi-box-arrow-in-right"></i> Login
                      </router-link>
                    </li>
                    <li class="nav-item" v-if="!isAuthenticated">
                      <router-link to="/signup" class="nav-link" active-class="active">
                        <i class="bi bi-person-plus"></i> Sign Up
                      </router-link>
                    </li>
                    <li class="nav-item" v-if="isAuthenticated">
                      <a href="#" @click.prevent="logout" class="nav-link">
                        <i class="bi bi-box-arrow-right"></i> Logout
                      </a>
                    </li>
                  </ul>
                </div>-->
      </div>
    </header>

    <main>
      <router-view @login="checkAuth"/>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isAuthenticated: false
    };
  },
  mounted() {
    this.checkAuth();
    // Check auth status on route changes
    this.$watch(
      () => this.$route.path,
      () => {
        this.checkAuth();
      }
    );
  },
  methods: {
    checkAuth() {
      this.isAuthenticated = !!localStorage.getItem('authToken');
    },
    logout() {
      localStorage.removeItem('authToken');
      this.isAuthenticated = false;
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
#app {
  min-height: 100vh;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: 600;
}
</style>
