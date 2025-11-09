<template>
  <div id="app">
    <header class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand mb-0 h1 text-decoration-none">
          Sports Watch Manager
        </router-link>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
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
          </ul>
        </div>
      </div>
    </header>
    
    <main>
      <router-view />
    </main>
  </div>
</template>

<style scoped>
#app {
  min-height: 100vh;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: 600;
}
</style>
