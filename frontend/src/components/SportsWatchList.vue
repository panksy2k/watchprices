<template>
  <div class="sports-watch-list">
    <div class="container">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1 class="h2 text-primary mb-1">Sports Watch Collection</h1>
          <small class="text-muted">
            <i class="bi bi-info-circle me-1"></i>
            Double-click on any watch card to edit
          </small>
        </div>
        <div class="d-flex gap-2">
          <button
            :class="{ active: filtersVisible }"
            class="btn btn-outline-secondary"
            @click="toggleFilters"
          >
            <i class="bi bi-funnel"></i> Filters
          </button>
          <router-link class="btn btn-primary" to="/add">
            <i class="bi bi-plus-circle"></i> Add New Watch
          </router-link>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-3 text-muted">Loading sports watches...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger" role="alert">
        <h4 class="alert-heading">Error Loading Data</h4>
        <p>{{ error }}</p>
        <button class="btn btn-outline-danger btn-sm" @click="fetchProducts">
          <i class="bi bi-arrow-clockwise"></i> Retry
        </button>
      </div>

      <!-- Empty State -->
      <div v-else-if="!products.length" class="text-center py-5">
        <div class="mb-4">
          <i class="bi bi-watch display-1 text-muted"></i>
        </div>
        <h3 class="text-muted">No Sports Watches Found</h3>
        <p class="text-muted">Start by adding your first sports watch to the collection.</p>
        <router-link class="btn btn-primary" to="/add">
          <i class="bi bi-plus-circle"></i> Add Sports Watch
        </router-link>
      </div>

      <!-- Main Content Area -->
      <div v-else class="row">
        <!-- Filter Sidebar -->
        <div v-if="filtersVisible" class="col-lg-3 col-md-4 mb-4">
          <div class="card shadow-sm filter-sidebar">
            <div class="card-header bg-light">
              <h6 class="card-title mb-0">
                <i class="bi bi-funnel me-2"></i>Filters
              </h6>
            </div>
            <div class="card-body">
              <!-- Water Resistance Filter -->
              <div class="mb-3">
                <label class="form-label small fw-semibold">Water Resistance</label>
                <select
                  v-model="selectedWaterResistance"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All Water Resistance' }}</option>
                  <option
                    v-for="option in waterResistanceOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <!-- Charging Time Filter -->
              <div class="mb-3">
                <label class="form-label small fw-semibold">Charging Time</label>
                <select
                  v-model="selectedChargingTime"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All' }}</option>
                  <option
                    v-for="option in chargingTimeOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <!-- Internal Memory Filter -->
              <div class="mb-3">
                <label class="form-label small fw-semibold">Memory</label>
                <select
                  v-model="selectedInternalMemory"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All' }}</option>
                  <option
                    v-for="option in internalMemoryOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <!-- Screen Material Filter -->
              <div class="mb-3">
                <label class="form-label small fw-semibold">Screen Material</label>
                <select
                  v-model="selectedScreenMaterial"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All' }}</option>
                  <option
                    v-for="option in screenMaterialOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <!-- Battery Life Daily Use Filter -->
              <div class="mb-3">
                <label class="form-label small fw-semibold">Battery Life</label>
                <select
                  v-model="selectedBatteryLifeDailyUse"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All' }}</option>
                  <option
                    v-for="option in batteryLifeDailyUseOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <!-- Filter Actions -->
              <div class="d-grid gap-2">
                <button
                  :disabled="!hasActiveFilters"
                  class="btn btn-outline-secondary btn-sm"
                  @click="clearFilters"
                >
                  <i class="bi bi-x-circle me-1"></i>Clear Filters
                </button>
              </div>

              <!-- Results Count -->
              <div class="mt-3 pt-2 border-top">
                <small class="text-muted">
                  Showing {{ filteredProducts.length }} of {{ products.length }} watches
                </small>
              </div>
            </div>
          </div>
        </div>

        <!-- Products Grid -->
        <div :class="filtersVisible ? 'col-lg-9 col-md-8' : 'col-12'">
          <div class="row g-4">
            <div v-for="product in filteredProducts" :key="product.id" class="col-xl-4 col-lg-6">
              <div
                class="card h-100 shadow-sm product-card"
                title="Double-click to edit"
                @dblclick="editProduct(product)"
              >
                <div class="card-header bg-light position-relative">
                  <div class="d-flex justify-content-between align-items-start">
                    <h5 class="card-title text-primary mb-1">{{ product.modelName }}</h5>
                    <div class="d-flex align-items-center gap-2">
                  <span :class="getColorBadgeClass(product.color)" class="badge">
                    {{ product.color }}
                  </span>
                    </div>
                  </div>
                  <div class="edit-indicator">
                    <i class="bi bi-pencil-square text-muted"></i>
                  </div>
                  <div class="d-flex align-items-center">
                <span class="h5 text-success mb-0">
                  {{ formatPrice(product.price) }}
                </span>
                    <small class="text-muted ms-2">{{ product.price?.currency }}</small>
                  </div>
                </div>

                <div class="card-body">
                  <p v-if="product.description" class="card-text text-muted small">
                    {{ truncateText(product.description, 100) }}
                  </p>

                  <!-- Key Specifications -->
                  <div class="row g-2 mt-2">
                    <div v-if="product.displaySize" class="col-6">
                      <small class="text-muted d-block">Display</small>
                      <span class="fw-semibold">{{ product.displaySize }}</span>
                    </div>
                    <div v-if="product.waterResistance" class="col-6">
                      <small class="text-muted d-block">Water Resistance</small>
                      <span class="fw-semibold">{{ product.waterResistance }}</span>
                    </div>
                    <div v-if="product.batteryLifeDailyUse" class="col-6">
                      <small class="text-muted d-block">Battery Life</small>
                      <span class="fw-semibold">{{ product.batteryLifeDailyUse }}</span>
                    </div>
                    <div v-if="product.weight" class="col-6">
                      <small class="text-muted d-block">Weight</small>
                      <span class="fw-semibold">{{ product.weight }}</span>
                    </div>
                    <div v-if="product.internalMemory" class="col-6">
                      <small class="text-muted d-block">Memory</small>
                      <span class="fw-semibold">{{ product.internalMemory }}</span>
                    </div>
                    <div v-if="product.chargingTime" class="col-6">
                      <small class="text-muted d-block">Charging Time</small>
                      <span class="fw-semibold">{{ product.chargingTime }}</span>
                    </div>
                    <div v-if="product.screenMaterial" class="col-6">
                      <small class="text-muted d-block">Screen Material</small>
                      <span class="fw-semibold">{{ product.screenMaterial }}</span>
                    </div>
                  </div>

                  <!-- Features -->
                  <div v-if="product.supportedActivities && product.supportedActivities.length" class="mt-3">
                    <small class="text-muted d-block mb-2">Supported Activities</small>
                    <div class="d-flex flex-wrap gap-1">
                  <span
                    v-for="activity in product.supportedActivities.slice(0, 3)"
                    :key="activity"
                    class="badge bg-secondary"
                  >
                    {{ activity }}
                  </span>
                      <span
                        v-if="product.supportedActivities.length > 3"
                        class="badge bg-light text-dark"
                      >
                    +{{ product.supportedActivities.length - 3 }} more
                  </span>
                    </div>
                  </div>

                  <!-- Daily Features -->
                  <div v-if="product.dailyFeatures && product.dailyFeatures.length" class="mt-3">
                    <small class="text-muted d-block mb-2">Daily Features</small>
                    <div class="d-flex flex-wrap gap-1">
                      <span
                        v-for="feature in product.dailyFeatures"
                        :key="feature"
                        class="badge bg-info"
                      >
                        {{ feature }}
                      </span>
                    </div>
                  </div>

                  <!-- Third-Party Integration -->
                  <div v-if="product.thirdPartyIntegrationApps && product.thirdPartyIntegrationApps.length"
                       class="mt-3">
                    <small class="text-muted d-block mb-2">Integrations</small>
                    <div class="d-flex flex-wrap gap-1">
                      <span
                        v-for="app in product.thirdPartyIntegrationApps"
                        :key="app"
                        class="badge bg-warning"
                      >
                        {{ app }}
                      </span>
                    </div>
                  </div>
                </div>

                <!-- Affiliate Links -->
                <div v-if="product.affiliateMarketingDeepURL && Object.keys(product.affiliateMarketingDeepURL).length"
                     class="card-footer bg-white">
                  <small class="text-muted d-block mb-2">Buy from:</small>
                  <div class="d-flex flex-wrap gap-2">
                    <a
                      v-for="(url, partner) in product.affiliateMarketingDeepURL"
                      :key="partner"
                      :href="url"
                      class="btn btn-outline-primary btn-sm"
                      rel="noopener noreferrer"
                      target="_blank"
                    >
                      {{ partner }}
                      <i class="bi bi-box-arrow-up-right ms-1"></i>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Stats Footer -->
        <div v-if="products.length" class="mt-5 pt-4 border-top">
          <div class="row text-center">
            <div class="col-md-3">
              <div class="h4 text-primary">{{ filteredProducts.length }}</div>
              <small class="text-muted">{{ hasActiveFilters ? 'Filtered' : 'Total' }} Watches</small>
            </div>
            <div class="col-md-3">
              <div class="h4 text-success">${{ averagePrice.toFixed(0) }}</div>
              <small class="text-muted">Avg. Price</small>
            </div>
            <div class="col-md-3">
              <div class="h4 text-info">{{ uniqueBrands }}</div>
              <small class="text-muted">Brands</small>
            </div>
            <div class="col-md-3">
              <div class="h4 text-warning">{{ uniqueColors }}</div>
              <small class="text-muted">Colors</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SportsWatchList',
  data() {
    return {
      products: [],
      loading: false,
      error: null,
      productType: 'SPORTSWATCH',
      filtersVisible: false,
      selectedWaterResistance: '',
      waterResistanceOptions: [],
      selectedChargingTime: '',
      chargingTimeOptions: [],
      selectedInternalMemory: '',
      internalMemoryOptions: [],
      selectedScreenMaterial: '',
      screenMaterialOptions: [],
      selectedBatteryLifeDailyUse: '',
      batteryLifeDailyUseOptions: [],
      loadingFilters: false,
      filterDebounceTimer: null
    }
  },
  computed: {
    filteredProducts() {
      // Return products fetched from backend based on criteria
      return this.products
    },
    hasActiveFilters() {
      return this.selectedWaterResistance !== '' ||
        this.selectedChargingTime !== '' ||
        this.selectedInternalMemory !== '' ||
        this.selectedScreenMaterial !== '' ||
        this.selectedBatteryLifeDailyUse !== ''
    },
    averagePrice() {
      if (!this.filteredProducts.length) return 0
      const total = this.filteredProducts.reduce((sum, product) => {
        return sum + (product.price?.cost || 0)
      }, 0)
      return total / this.filteredProducts.length
    },
    uniqueBrands() {
      const brands = new Set(this.filteredProducts.map(p => p.modelName?.split(' ')[0]))
      return brands.size
    },
    uniqueColors() {
      const colors = new Set(this.filteredProducts.map(p => p.color))
      return colors.size
    }
  },
  watch: {
    selectedWaterResistance() {
      this.applyFilters()
    },
    selectedChargingTime() {
      this.applyFilters()
    },
    selectedInternalMemory() {
      this.applyFilters()
    },
    selectedScreenMaterial() {
      this.applyFilters()
    },
    selectedBatteryLifeDailyUse() {
      this.applyFilters()
    }
  },
  mounted() {
    this.fetchProducts()
    this.fetchAllFilterOptions()
  },
  methods: {
    async fetchProducts() {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(`/api/products/${this.productType}`)
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()
        console.log('API Response:', data)

        // Handle the response structure - it should have productList
        if (data && data.productList) {
          this.products = data.productList
        } else if (Array.isArray(data)) {
          this.products = data
        } else {
          this.products = []
        }

      } catch (error) {
        console.error('Error fetching products:', error)
        this.error = error.message || 'Failed to load sports watches'
        this.products = []
      } finally {
        this.loading = false
      }
    },
    async fetchAllFilterOptions() {
      this.loadingFilters = true
      try {
        await Promise.all([
          this.fetchAttributeOptions('waterResistance', 'waterResistanceOptions'),
          this.fetchAttributeOptions('chargingTime', 'chargingTimeOptions'),
          this.fetchAttributeOptions('internalMemory', 'internalMemoryOptions'),
          this.fetchAttributeOptions('screenMaterial', 'screenMaterialOptions'),
          this.fetchAttributeOptions('batteryLifeDailyUse', 'batteryLifeDailyUseOptions')
        ])
      } catch (error) {
        console.error('Error fetching filter options:', error)
      } finally {
        this.loadingFilters = false
      }
    },
    async fetchAttributeOptions(attributeName, optionsProperty) {
      try {
        const response = await fetch(`/api/products/${this.productType}/attribute/${attributeName}`)
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        const data = await response.json()
        let values = []
        if (data && data[attributeName] && Array.isArray(data[attributeName])) {
          values = data[attributeName]
        } else if (Array.isArray(data)) {
          values = data
        } else {
          console.warn(`Unexpected response format for ${attributeName} options:`, data)
          values = []
        }
        this[optionsProperty] = values
          .filter(value => value && value.trim())
          .map(value => ({ value, label: value }))
      } catch (error) {
        console.error(`Error fetching ${attributeName} options:`, error)
        this[optionsProperty] = []
      }
    },
    async applyFilters() {
      // Debounce to avoid too many API calls
      if (this.filterDebounceTimer) {
        clearTimeout(this.filterDebounceTimer)
      }

      this.filterDebounceTimer = setTimeout(async () => {
        await this.executeFilterQuery()
      }, 300)
    },
    async executeFilterQuery() {
      // Build criteria array based on selected filters
      const criterias = []

      if (this.selectedWaterResistance) {
        criterias.push({
          col: 'waterResistance',
          val: [this.selectedWaterResistance],
          op: 'IN'
        })
      }

      if (this.selectedChargingTime) {
        criterias.push({
          col: 'chargingTime',
          val: [this.selectedChargingTime],
          op: 'IN'
        })
      }

      if (this.selectedInternalMemory) {
        criterias.push({
          col: 'internalMemory',
          val: [this.selectedInternalMemory],
          op: 'IN'
        })
      }

      if (this.selectedScreenMaterial) {
        criterias.push({
          col: 'screenMaterial',
          val: [this.selectedScreenMaterial],
          op: 'IN'
        })
      }

      if (this.selectedBatteryLifeDailyUse) {
        criterias.push({
          col: 'batteryLifeDailyUse',
          val: [this.selectedBatteryLifeDailyUse],
          op: 'IN'
        })
      }

      // If no filters selected, fetch all products
      if (criterias.length === 0) {
        await this.fetchProducts()
        return
      }

      // Make backend call with criteria
      await this.fetchProductsByCriteria(criterias)
    },
    async fetchProductsByCriteria(criterias) {
      this.loading = true
      this.error = null

      try {
        const response = await fetch(
          `/api/products/${this.productType}/find/criteria`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({ criterias })
          }
        )

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()

        if (data && data.success && data.data) {
          if (data.data.productList) {
            this.products = [...data.data.productList]
          } else if (Array.isArray(data.data)) {
            this.products = [...data.data]
          } else {
            this.products = []
          }
        } else if (data && data.productList) {
          this.products = [...data.productList]
        } else if (Array.isArray(data)) {
          this.products = [...data]
        } else {
          this.products = []
        }

        // Force Vue to update
        this.$forceUpdate()
      } catch (error) {
        console.error('Error fetching filtered products:', error)
        this.error = error.message || 'Failed to apply filters'
        this.products = []
      } finally {
        this.loading = false
      }
    },
    formatPrice(price) {
      if (!price || typeof price.cost !== 'number') return 'N/A'
      return price.cost.toLocaleString('en-US', {
        style: 'currency',
        currency: price.currency || 'USD',
        minimumFractionDigits: 0
      })
    },
    getColorBadgeClass(color) {
      const colorClasses = {
        'BLACK': 'bg-dark text-white',
        'WHITE': 'bg-light text-dark',
        'RED': 'bg-danger',
        'BLUE': 'bg-primary',
        'GREEN': 'bg-success',
        'YELLOW': 'bg-warning text-dark',
        'ORANGE': 'bg-warning',
        'PURPLE': 'bg-info',
        'PINK': 'bg-danger',
        'GREY': 'bg-secondary'
      }
      return colorClasses[color] || 'bg-secondary'
    },
    truncateText(text, maxLength) {
      if (!text || text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    },
    editProduct(product) {
      // Navigate to edit page with product ID and type
      this.$router.push({
        name: 'EditSportsWatch',
        params: {
          productType: this.productType,
          id: product._id || product.id
        }
      })
    },
    toggleFilters() {
      this.filtersVisible = !this.filtersVisible
    },
    clearFilters() {
      this.selectedWaterResistance = ''
      this.selectedChargingTime = ''
      this.selectedInternalMemory = ''
      this.selectedScreenMaterial = ''
      this.selectedBatteryLifeDailyUse = ''
      this.fetchProducts()
    }
  }
}
</script>

<style scoped>
.sports-watch-list {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 2rem 0;
}

.card {
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  border: none;
  border-radius: 12px;
}

.product-card {
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

.edit-indicator {
  position: absolute;
  top: 10px;
  right: 10px;
  opacity: 0.6;
  transition: opacity 0.2s ease-in-out;
}

.product-card:hover .edit-indicator {
  opacity: 1;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

.card-header {
  border-bottom: 1px solid #e9ecef;
  border-radius: 12px 12px 0 0 !important;
  padding: 1.25rem;
}

.card-body {
  padding: 1.25rem;
}

.card-footer {
  border-top: 1px solid #e9ecef;
  border-radius: 0 0 12px 12px !important;
  padding: 1rem 1.25rem;
}

.badge {
  font-size: 0.75rem;
  padding: 0.35em 0.65em;
}

.btn {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.2s ease-in-out;
}

.btn:hover {
  transform: translateY(-1px);
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}

.bi-watch {
  font-size: 4rem;
}

/* Filter Section Styles */
.btn.active {
  background-color: #0d6efd;
  border-color: #0d6efd;
  color: white;
}

.form-select-sm {
  font-size: 0.875rem;
}

.card-title {
  color: #495057;
  font-weight: 600;
}

.filter-sidebar {
  position: sticky;
  top: 2rem;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .sports-watch-list {
    padding: 1rem 0;
  }

  .container {
    padding: 0 1rem;
  }

  .filter-sidebar {
    position: static;
    margin-bottom: 1rem;
  }

  .d-flex.gap-2 {
    flex-direction: column;
    gap: 0.5rem !important;
  }

  .d-flex.gap-2 .btn {
    width: 100%;
  }
}

@media (max-width: 992px) {
  .filter-sidebar {
    position: static;
  }
}
</style>
