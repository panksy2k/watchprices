<template>
  <div class="product-grid">
    <div class="container-fluid px-3">
      <!-- Header with Product Type Selector -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1 class="h2 text-primary mb-1">Product Collection</h1>
          <small class="text-muted">
            <i class="bi bi-info-circle me-1"></i>
            Double-click any row to edit
          </small>
        </div>
        <div class="d-flex gap-3 align-items-center">
          <button
            :class="{ active: filtersVisible }"
            class="btn btn-outline-secondary"
            @click="toggleFilters"
          >
            <i class="bi bi-funnel"></i> Filters
          </button>
          <router-link class="btn btn-primary" to="/add">
            <i class="bi bi-plus-circle"></i> Add New Product
          </router-link>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="mt-3 text-muted">Loading {{ selectedProductType.toLowerCase() }}s...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger" role="alert">
        <h4 class="alert-heading">Error Loading Data</h4>
        <p>{{ error }}</p>
        <button class="btn btn-outline-danger btn-sm" @click="fetchProducts(1)">
          <i class="bi bi-arrow-clockwise"></i> Retry
        </button>
      </div>

      <!-- Empty State -->
      <div v-else-if="!products.length" class="text-center py-5">
        <div class="mb-4">
          <i :class="getProductIcon(selectedProductType)" class="display-1 text-muted"></i>
        </div>
        <h3 class="text-muted">No {{ selectedProductType.toLowerCase() }}s Found</h3>
        <p class="text-muted">Start by adding your first {{ selectedProductType.toLowerCase() }} to the collection.</p>
        <router-link class="btn btn-primary" to="/add">
          <i class="bi bi-plus-circle"></i> Add {{ selectedProductType }}
        </router-link>
      </div>

      <!-- Filters Section (Top) -->
      <div v-if="filtersVisible" class="mb-4">
        <div class="card shadow-sm filter-top">
          <div class="card-header bg-light">
            <h6 class="card-title mb-0">
              <i class="bi bi-funnel me-2"></i>Filters
            </h6>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <!-- Water Resistance Filter -->
              <div class="col-lg-2 col-md-4 col-sm-6">
                <label class="form-label small fw-semibold">Water Resistance</label>
                <select
                  v-model="selectedWaterResistance"
                  :disabled="loadingFilters"
                  class="form-select form-select-sm"
                >
                  <option value="">{{ loadingFilters ? 'Loading...' : 'All' }}</option>
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
              <div class="col-lg-2 col-md-4 col-sm-6">
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
              <div class="col-lg-2 col-md-4 col-sm-6">
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
              <div class="col-lg-2 col-md-4 col-sm-6">
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
              <div class="col-lg-2 col-md-4 col-sm-6">
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
              <div class="col-lg-2 col-md-4 col-sm-6">
                <label class="form-label small fw-semibold text-transparent">Actions</label>
                <div>
                  <button
                    :disabled="!hasActiveFilters"
                    class="btn btn-outline-secondary btn-sm w-100"
                    @click="clearFilters"
                  >
                    <i class="bi bi-x-circle me-1"></i>Clear
                  </button>
                </div>
              </div>
            </div>

            <!-- Results Count -->
            <div class="mt-3 pt-2 border-top text-center">
              <small class="text-muted">
                <i class="bi bi-info-circle me-1"></i>
                <span v-if="hasActiveFilters">
                  Showing {{ products.length }} filtered product{{ products.length !== 1 ? 's' : '' }}
                  <span class="badge bg-primary ms-2">Filtered</span>
                </span>
                <span v-else>
                  Showing {{ products.length }} product{{ products.length !== 1 ? 's' : '' }}
                </span>
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- Main Content Area -->
      <div v-if="products.length">
        <!-- Results Count -->
        <div class="mb-3">
          <span class="text-muted">
            {{ products.length }} sports watch{{ products.length !== 1 ? 'es' : '' }} 
            {{ hasActiveFilters ? 'matching filters' : 'found' }}
          </span>
        </div>

        <!-- Full Width Table Container -->
        <div class="table-container">
          <table class="table table-hover table-striped product-table table-sm">
            <thead class="table-dark">
            <tr>
                <th class="text-nowrap" scope="col">
                  <i :class="getProductIcon(selectedProductType)" class="me-2"></i>Model
                </th>
                <th scope="col">Price</th>

              <!-- Sports Watch Headers -->
              <th scope="col">Size</th>
              <th scope="col">Display Px</th>
              <th scope="col">Battery</th>
              <th scope="col">Screen Type</th>
              <th scope="col">Connectivity</th>
              <th scope="col">Charge Time</th>
              <th scope="col">Memory</th>
              <th scope="col">Maps</th>
              <th scope="col">Water Resistance</th>

              <th scope="col">Weight</th>
              <th scope="col">Color</th>

              <!-- Detailed View Additional Columns -->
              <th scope="col">Activities</th>
              <th scope="col">Sensors</th>
              <th scope="col">Daily Features</th>
              <th scope="col">Third Party</th>

              <th scope="col">Purchase</th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="product in filteredProducts"
              :key="product.id"
              class="product-row clickable-row"
              title="Double-click to edit"
              @dblclick="editProduct(product)"
            >
              <!-- Model Name -->
              <td class="fw-semibold text-primary position-relative">
                <div class="d-flex align-items-center justify-content-between">
                  <div>
                    {{ product.modelName || 'Unknown Model' }}
                  </div>
                  <i class="bi bi-pencil-square text-muted edit-icon"></i>
                </div>
              </td>

                <!-- Price -->
                <td class="text-success fw-bold">
                  {{ formatPrice(product.price) }}
                  <div v-if="product.price?.currency" class="small text-muted">{{ product.price.currency }}</div>
                </td>

              <!-- Sports Watch Columns -->
                <!-- Display Size -->
                <td class="display-cell">
                  <span v-if="product.displaySize" class="spec-badge display-spec">{{ product.displaySize }}</span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Display Resolution -->
                <td class="display-cell">
                    <span v-if="product.displayResolution" class="spec-badge display-spec">{{
                        product.displayResolution
                      }}</span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Battery Life -->
                <td class="battery-cell">
                    <span v-if="product.batteryLifeDailyUse" class="spec-badge battery-spec">
                      <i class="bi bi-battery-charging me-1"></i>{{ product.batteryLifeDailyUse }}
                    </span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Screen Material -->
                <td class="material-cell">
                    <span v-if="product.screenMaterial" class="spec-badge material-spec">{{
                        product.screenMaterial
                      }}</span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Phone Connectivity -->
                <td class="connectivity-cell">
                    <span v-if="product.phoneConnectivity" class="spec-badge connectivity-spec">
                      <i class="bi bi-phone me-1"></i>{{ product.phoneConnectivity }}
                    </span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Charging Time -->
                <td class="charging-cell">
                    <span v-if="product.chargingTime" class="spec-badge charging-spec">
                      <i class="bi bi-lightning-charge me-1"></i>{{ product.chargingTime }}
                    </span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Internal Memory -->
                <td class="memory-cell">
                    <span v-if="product.internalMemory" class="spec-badge memory-spec">
                      <i class="bi bi-hdd me-1"></i>{{ product.internalMemory }}
                    </span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

                <!-- Global Maps -->
                <td class="maps-cell">
                    <span
                      v-if="product.hasDownloadableGlobalMaps !== null && product.hasDownloadableGlobalMaps !== undefined"
                      :class="product.hasDownloadableGlobalMaps ? 'maps-spec-yes' : 'maps-spec-no'"
                      class="spec-badge">
                      <i
                        :class="[product.hasDownloadableGlobalMaps ? 'bi-globe' : 'bi-globe-central-south-asia', 'bi', 'me-1']"></i>
                      {{ product.hasDownloadableGlobalMaps ? 'Yes' : 'No' }}
                    </span>
                  <span v-else class="text-muted fst-italic">
                      <i class="bi bi-question-circle me-1"></i>N/A
                    </span>
                </td>

                <!-- Water Resistance -->
                <td class="water-cell">
                    <span v-if="product.waterResistance" class="spec-badge water-spec">
                      <i class="bi bi-droplet me-1"></i>{{ product.waterResistance }}
                    </span>
                  <span v-else class="text-muted fst-italic">N/A</span>
                </td>

              <!-- Weight -->
              <td class="weight-cell">
                  <span v-if="product.weight" class="spec-badge weight-spec">
                    <i class="bi bi-speedometer me-1"></i>{{ product.weight }}
                  </span>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Color -->
              <td class="color-cell">
                  <span v-if="product.color" :class="getColorBadgeClass(product.color)" class="color-badge">
                    <i class="bi bi-palette me-1"></i>{{ product.color }}
                  </span>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Activities -->
              <td class="activities-cell">
                <div v-if="product.supportedActivities && product.supportedActivities.length"
                     class="d-flex flex-wrap gap-1">
                    <span
                      v-for="activity in product.supportedActivities.slice(0, 2)"
                      :key="activity"
                      class="activity-badge"
                    >
                      <i class="bi bi-activity me-1"></i>{{ activity }}
                    </span>
                  <span
                    v-if="product.supportedActivities.length > 2"
                    :title="product.supportedActivities.slice(2).join(', ')"
                    class="activity-badge more-badge"
                  >
                      +{{ product.supportedActivities.length - 2 }} more
                    </span>
                </div>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Tracking -->
              <td class="tracking-cell">
                <div v-if="product.dataTrackingSensors && product.dataTrackingSensors.length"
                     class="d-flex flex-wrap gap-1">
                    <span
                      v-for="trackingSensor in product.dataTrackingSensors.slice(0, 2)"
                      :key="trackingSensor"
                      class="tracking-badge"
                    >
                      <i class="bi bi-sensors me-1"></i>{{ trackingSensor }}
                    </span>
                  <span
                    v-if="product.dataTrackingSensors.length > 2"
                    :title="product.dataTrackingSensors.slice(2).join(', ')"
                    class="tracking-badge more-badge"
                  >
                      +{{ product.dataTrackingSensors.length - 2 }} more
                    </span>
                </div>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Daily Features -->
              <td class="features-cell">
                <div v-if="product.dailyFeatures && product.dailyFeatures.length" class="d-flex flex-wrap gap-1">
                    <span
                      v-for="dailyFeature in product.dailyFeatures.slice(0, 2)"
                      :key="dailyFeature"
                      class="feature-badge"
                    >
                      <i class="bi bi-star me-1"></i>{{ dailyFeature }}
                    </span>
                  <span
                    v-if="product.dailyFeatures.length > 2"
                    :title="product.dailyFeatures.slice(2).join(', ')"
                    class="feature-badge more-badge"
                  >
                      +{{ product.dailyFeatures.length - 2 }} more
                    </span>
                </div>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Third Party Integration -->
              <td class="integration-cell">
                <div v-if="product.thirdPartyIntegrationApps && product.thirdPartyIntegrationApps.length"
                     class="d-flex flex-wrap gap-1">
                    <span
                      v-for="thirdPartyIntegration in product.thirdPartyIntegrationApps.slice(0, 2)"
                      :key="thirdPartyIntegration"
                      class="integration-badge"
                    >
                      <i class="bi bi-link-45deg me-1"></i>{{ thirdPartyIntegration }}
                    </span>
                  <span
                    v-if="product.thirdPartyIntegrationApps.length > 2"
                    :title="product.thirdPartyIntegrationApps.slice(2).join(', ')"
                    class="integration-badge more-badge"
                  >
                      +{{ product.thirdPartyIntegrationApps.length - 2 }} more
                    </span>
                </div>
                <span v-else class="text-muted fst-italic">N/A</span>
              </td>

              <!-- Purchase Links -->
              <td class="purchase-cell">
                <div v-if="hasValidAffiliateLinks(product)" class="btn-group-vertical btn-group-sm">
                  <a
                    v-for="[partnerName, partnerUrl] in getAffiliateEntries(product).slice(0, 2)"
                    :key="partnerName"
                    :href="partnerUrl"
                    :title="`Buy from ${partnerName} - ${partnerUrl}`"
                    class="btn btn-outline-primary btn-sm purchase-link"
                    rel="noopener noreferrer"
                    target="_blank"
                    @click="logClick(partnerName, partnerUrl)"
                  >
                    <i class="bi bi-cart me-1"></i>{{ partnerName }}
                    <i class="bi bi-box-arrow-up-right ms-1"></i>
                  </a>

                  <!-- More Links Toggle -->
                  <button
                    v-if="getAffiliateEntries(product).length > 2"
                    :aria-controls="`moreLinks${product.id}`"
                    :aria-expanded="isExpanded(product.id)"
                    class="btn btn-outline-secondary btn-sm"
                    type="button"
                    @click="toggleMoreLinks(product.id)"
                  >
                    <i class="bi bi-three-dots me-1"></i>
                    +{{ getAffiliateEntries(product).length - 2 }} more
                  </button>

                  <!-- Collapsible More Links -->
                  <div
                    :id="`moreLinks${product.id}`"
                    :class="{ 'show': isExpanded(product.id) }"
                    class="more-links mt-1"
                  >
                    <a
                      v-for="[partnerName, partnerUrl] in getAffiliateEntries(product).slice(2)"
                      :key="partnerName"
                      :href="partnerUrl"
                      :title="`Buy from ${partnerName} - ${partnerUrl}`"
                      class="btn btn-outline-primary btn-sm d-block mb-1 purchase-link"
                      rel="noopener noreferrer"
                      target="_blank"
                      @click="logClick(partnerName, partnerUrl)"
                    >
                      <i class="bi bi-cart me-1"></i>{{ partnerName }}
                      <i class="bi bi-box-arrow-up-right ms-1"></i>
                    </a>
                  </div>
                </div>

                <!-- Debug Info (only in development) -->
                <div v-else-if="product.affiliateMarketingDeepURL" class="debug-info">
                  <small class="text-warning">
                    <i class="bi bi-exclamation-triangle me-1"></i>Invalid URLs
                  </small>
                  <details class="mt-1">
                    <summary class="text-muted" style="cursor: pointer; font-size: 0.7rem;">Debug</summary>
                    <pre class="text-muted" style="font-size: 0.6rem; margin: 0;">{{
                        JSON.stringify(product.affiliateMarketingDeepURL, null, 2)
                      }}</pre>
                  </details>
                </div>

                <!-- No Links Available -->
                <span v-else class="text-muted fst-italic">
                    <i class="bi bi-x-circle me-1"></i>No purchase links
                  </span>
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination Controls -->
        <div v-if="products.length" class="mt-4 mb-4">
          <nav aria-label="Product pagination">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <div class="d-flex align-items-center gap-2">
                <label for="itemsPerPage" class="form-label mb-0 small">Items per page:</label>
                <select
                  id="itemsPerPage"
                  v-model="itemsPerPage"
                  class="form-select form-select-sm"
                  style="width: auto;"
                  @change="changeItemsPerPage(itemsPerPage)"
                >
                  <option :value="5">5</option>
                  <option :value="10">10</option>
                  <option :value="25">25</option>
                  <option :value="50">50</option>
                </select>
              </div>
              <div class="text-center small text-muted" v-if="totalItems > 0">
                <span>Page {{ currentPage }} of {{ totalPages }} | Total: {{ totalItems }} items</span>
              </div>
            </div>

            <!-- Pagination Buttons -->
            <div class="d-flex justify-content-center gap-2">
              <button
                :disabled="currentPage === 1 || loading"
                class="btn btn-outline-primary btn-sm"
                @click="previousPage"
              >
                <i class="bi bi-chevron-left me-1"></i>Previous
              </button>

              <!-- Page Numbers -->
              <div class="d-flex gap-1">
                <button
                  v-for="pageNum in getVisiblePages"
                  :key="pageNum"
                  :class="{
                    'btn-primary': pageNum === currentPage,
                    'btn-outline-primary': pageNum !== currentPage
                  }"
                  class="btn btn-sm"
                  :disabled="loading"
                  @click="goToPage(pageNum)"
                >
                  {{ pageNum }}
                </button>
              </div>

              <button
                :disabled="currentPage === totalPages || loading"
                class="btn btn-outline-primary btn-sm"
                @click="nextPage"
              >
                Next<i class="bi bi-chevron-right ms-1"></i>
              </button>
            </div>
          </nav>
        </div>

        <!-- Affiliate Disclaimer -->
        <div v-if="products.length" class="mt-5 pt-4 border-top">
          <div class="card bg-light border-0">
            <div class="card-body">
              <div class="text-center">
                <h6 class="card-title text-muted mb-3">
                  <i class="bi bi-info-circle me-2"></i>Important Disclosure
                </h6>
                <p class="card-text text-muted small lh-base">
                  As an affiliate marketing initiative, I shall earn from qualifying purchases when it is done via the affiliate link. 
                  Product prices and availability are accurate as of the date/time indicated and are subject to change. 
                  Any price and availability information displayed at the time of purchase will apply to the purchase of the product. 
                  CERTAIN CONTENT THAT APPEARS ON THIS SITE COMES FROM AMAZON SERVICES LLC. 
                  THIS CONTENT IS PROVIDED 'AS IS' AND IS SUBJECT TO CHANGE OR REMOVAL AT ANY TIME. 
                  All rights reserved.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductGrid',
  data() {
    return {
      products: [],
      loading: false,
      error: null,
      selectedProductType: 'SPORTSWATCH', // Fixed to Sports Watches only
      tableView: 'detailed', // Fixed to detailed view
      expandedProducts: new Set(), // Track which products have expanded affiliate links
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
      filterDebounceTimer: null,
      // Pagination state
      currentPage: 1,
      itemsPerPage: 1,
      totalItems: 0,
      totalPages: 0
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
    getVisiblePages() {
      // Show up to 5 page buttons
      const maxVisible = 5
      const halfVisible = Math.floor(maxVisible / 2)
      let start = Math.max(1, this.currentPage - halfVisible)
      let end = Math.min(this.totalPages, start + maxVisible - 1)
      
      // Adjust start if we're near the end
      if (end - start < maxVisible - 1) {
        start = Math.max(1, end - maxVisible + 1)
      }
      
      const pages = []
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      return pages
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
    async fetchProducts(page = 1) {
      this.loading = true
      this.error = null
      this.currentPage = page

      try {
        const url = new URL(`/api/products/${this.selectedProductType}`, window.location.origin)
        url.searchParams.append('page', page)
        url.searchParams.append('limit', this.itemsPerPage)
        
        const response = await fetch(url.toString())
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()
        console.log('API Response:', data)

        // Handle the response structure - it should have productList
        if (data && data.productList) {
          this.products = [...data.productList]
          
          // Extract pagination info from headers
          const hasPagination = response.headers.get('X-APP-PAGINATION')
          if (hasPagination === 'true') {
            const totalPages = response.headers.get('X-APP-PAGINATION-TOTAL-PAGES')
            const totalEntities = response.headers.get('X-APP-PAGINATION-TOTAL-ENTITIES')
            
            if (totalPages) {
              this.totalPages = parseInt(totalPages, 10)
            }
            if (totalEntities) {
              this.totalItems = parseInt(totalEntities, 10)
            }
            
            console.log('Pagination info:', { totalPages: this.totalPages, totalItems: this.totalItems, currentPage: this.currentPage })
          } else {
            // No pagination headers, calculate based on response
            this.totalItems = this.products.length
            this.totalPages = 1
          }
        } else if (Array.isArray(data)) {
          this.products = [...data]
          this.totalItems = this.products.length
          this.totalPages = 1
        } else {
          this.products = []
          this.totalItems = 0
          this.totalPages = 0
        }

        console.log('Products loaded, count:', this.products.length)

      } catch (error) {
        console.error('Error fetching products:', error)
        this.error = error.message || `Failed to load ${this.selectedProductType.toLowerCase()}s`
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
        'GREY': 'bg-secondary',
        'GRAY': 'bg-secondary'
      }
      return colorClasses[color?.toUpperCase()] || 'bg-secondary'
    },
    getProductIcon(productType) {
      const icons = {
        'SPORTSWATCH': 'bi bi-watch',
        'SMARTPHONE': 'bi bi-phone',
        'LAPTOP': 'bi bi-laptop',
        'HEADPHONES': 'bi bi-headphones'
      }
      return icons[productType] || 'bi bi-box'
    },
    truncateText(text, maxLength) {
      if (!text || text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    },

    // Affiliate link handling methods
    hasValidAffiliateLinks(product) {
      if (!product.affiliateMarketingDeepURL) return false

      const entries = this.getAffiliateEntries(product)
      return entries.length > 0 && entries.some(([name, url]) => this.isValidUrl(url))
    },

    getAffiliateEntries(product) {
      if (!product.affiliateMarketingDeepURL) return []

      const entries = Object.entries(product.affiliateMarketingDeepURL)
        .filter(([name, url]) => name && url) // Filter out empty names or URLs
        .map(([name, url]) => [name.trim(), url.toString().trim()]) // Clean up strings

      console.log('Affiliate entries for product:', product.id, entries)
      return entries
    },

    isValidUrl(url) {
      if (!url) return false

      try {
        const urlString = url.toString().trim()
        // Check if it's a valid URL format
        const urlPattern = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/i
        return urlPattern.test(urlString) || urlString.startsWith('http://') || urlString.startsWith('https://')
      } catch (error) {
        console.error('URL validation error:', error)
        return false
      }
    },

    toggleMoreLinks(productId) {
      if (this.expandedProducts.has(productId)) {
        this.expandedProducts.delete(productId)
      } else {
        this.expandedProducts.add(productId)
      }
      // Force Vue to update the reactive set
      this.$forceUpdate()
    },

    isExpanded(productId) {
      return this.expandedProducts.has(productId)
    },

    editProduct(product) {
      // Navigate to edit page with product ID and type
      this.$router.push({
        name: 'EditSportsWatch',
        params: {
          productType: this.selectedProductType,
          id: product._id || product.id
        }
      })
    },
    logClick(partnerName, partnerUrl) {
      console.log(`Clicked on affiliate link:`, {
        partner: partnerName,
        url: partnerUrl,
        timestamp: new Date().toISOString()
      })

      // Here you could add analytics tracking if needed
      // Example: gtag('event', 'affiliate_click', { partner: partnerName })
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
      // Reset to all products and first page
      this.currentPage = 1
      this.fetchProducts(1)
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
          `/api/products/${this.selectedProductType}/find/criteria`,
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
        console.log('Filtered API Response:', data)

        // Handle the response structure
        // Backend returns: { success: true, data: { productList: [...] } }
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

        console.log('Products updated, count:', this.products.length)
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
    async fetchAllFilterOptions() {
      this.loadingFilters = true

      try {
        // Fetch all filter options in parallel
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
        const response = await fetch(`/api/products/${this.selectedProductType}/attribute/${attributeName}`)
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()
        console.log(`${attributeName} API Response:`, data)

        // Handle the response - it comes as {"waterResistance": ["value1", "value2"]}
        // Note: The API returns the attribute name as the key, so we need to extract it
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
          .filter(value => value && value.trim()) // Filter out null/empty values
          .map(value => ({value, label: value})) // Convert to options format

      } catch (error) {
        console.error(`Error fetching ${attributeName} options:`, error)
        // Fallback to empty array - no filter options will be shown
        this[optionsProperty] = []
      }
    },
    goToPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.fetchProducts(page)
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.goToPage(this.currentPage + 1)
      }
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.goToPage(this.currentPage - 1)
      }
    },
    changeItemsPerPage(newLimit) {
      this.itemsPerPage = newLimit
      this.currentPage = 1
      this.fetchProducts(1)
    }
  }
}
</script>

<style scoped>
.product-grid {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 2rem 0;
}

/* Full-width table container */
.table-container {
  width: 100%;
  margin: 0;
  padding: 0;
  overflow-x: auto;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background-color: white;
}

/* Table styling with optimized widths */
.product-table {
  width: 100%;
  min-width: 1100px; /* Minimum width to accommodate all columns */
  margin-bottom: 0;
  background-color: white;
  border: none;
  table-layout: fixed; /* Fixed layout for better control */
}

.product-table thead th {
  font-weight: 600;
  border-top: none;
  white-space: nowrap;
  padding: 8px 12px;
  background-color: #212529;
  font-size: 0.85rem;
}

.product-table th:first-child,
.product-table td:first-child {
  padding-left: 16px;
}

.product-table th:last-child,
.product-table td:last-child {
  padding-right: 16px;
}

.product-table tbody tr {
  transition: all 0.2s ease-in-out;
}

.clickable-row {
  cursor: pointer;
}

.product-table tbody tr:hover {
  background-color: rgba(13, 110, 253, 0.05);
}

.clickable-row:hover {
  background-color: rgba(13, 110, 253, 0.08) !important;
  transform: scale(1.001);
}

.edit-icon {
  opacity: 0.5;
  transition: opacity 0.2s ease-in-out;
  font-size: 0.9rem;
}

.clickable-row:hover .edit-icon {
  opacity: 1;
}

.product-row td {
  vertical-align: middle;
  padding: 8px 12px;
  border-color: #e9ecef;
  font-size: 0.85rem;
}

/* Optimized column widths */
.product-table th:nth-child(1), /* Model */
.product-table td:nth-child(1) {
  width: 180px;
}

.product-table th:nth-child(2), /* Price */
.product-table td:nth-child(2) {
  width: 100px;
}


/* Sports Watch specific columns - compact widths */
.product-table th:nth-child(n+3):nth-child(-n+11), /* Sports watch specs */
.product-table td:nth-child(n+3):nth-child(-n+11) {
  width: 90px;
  text-align: center;
}

/* Weight and Color columns */
.product-table th:nth-child(12), /* Weight */
.product-table td:nth-child(12),
.product-table th:nth-child(13), /* Color */
.product-table td:nth-child(13) {
  width: 80px;
  text-align: center;
}

/* Detailed view columns */
.product-table th:nth-child(n+14):nth-child(-n+17), /* Activities, Tracking, Features, Integration */
.product-table td:nth-child(n+14):nth-child(-n+17) {
  width: 150px;
}

/* Purchase column */
.product-table th:last-child,
.product-table td:last-child {
  width: 140px;
}


.activities-cell,
.tracking-cell,
.features-cell,
.integration-cell {
  max-width: 150px;
}

.purchase-cell .btn-group-vertical {
  width: 100%;
}

/* Purchase link styling */
.purchase-link {
  transition: all 0.2s ease;
  font-weight: 600;
}

.purchase-link:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3);
}

/* More links container */
.more-links {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease-in-out;
}

.more-links.show {
  max-height: 200px;
}

/* Debug info styling */
.debug-info {
  max-width: 150px;
  font-size: 0.7rem;
}

.debug-info details {
  margin-top: 0.25rem;
}

.debug-info pre {
  background-color: #f8f9fa;
  padding: 0.25rem;
  border-radius: 3px;
  max-height: 100px;
  overflow-y: auto;
  word-wrap: break-word;
  white-space: pre-wrap;
}

/* Cell-specific styling */
.display-cell,
.battery-cell,
.material-cell,
.connectivity-cell,
.charging-cell,
.memory-cell,
.maps-cell,
.water-cell,
.weight-cell,
.color-cell {
  text-align: center;
}

.activities-cell,
.tracking-cell,
.features-cell,
.integration-cell {
  max-width: 180px;
}

/* Badge styling */
.badge {
  font-size: 0.75rem;
  padding: 0.4em 0.7em;
  border-radius: 4px;
  display: inline-block;
  font-weight: 500;
}

/* Specification badges with enhanced styling */
.spec-badge {
  font-size: 0.75rem;
  padding: 0.5em 0.8em;
  border-radius: 6px;
  display: inline-block;
  font-weight: 600;
  text-align: center;
  min-width: 60px;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.spec-badge:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

/* Display specifications */
.display-spec,
.battery-spec,
.material-spec,
.connectivity-spec,
.charging-spec,
.memory-spec,
.maps-spec,
.maps-spec-yes,
.maps-spec-no,
.water-spec,
.weight-spec {
  background-color: #f8f9fa;
  color: #495057;
  border: 1px solid #dee2e6;
}

.maps-spec-yes,
.maps-spec-no {
  font-weight: 700;
}

/* Color badge styling */
.color-badge {
  font-size: 0.75rem;
  padding: 0.5em 0.8em;
  border-radius: 20px;
  display: inline-block;
  font-weight: 600;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.2s ease;
}

/* Activity badges */
.activity-badge,
.tracking-badge,
.feature-badge,
.integration-badge {
  font-size: 0.7rem;
  padding: 0.3em 0.6em;
  border-radius: 12px;
  background-color: #e9ecef;
  color: #495057;
  border: 1px solid #dee2e6;
  font-weight: 500;
  margin: 2px;
  display: inline-block;
  transition: all 0.2s ease;
}

.activity-badge:hover,
.tracking-badge:hover,
.feature-badge:hover,
.integration-badge:hover {
  transform: scale(1.05);
  background-color: #dee2e6;
}

/* More badges */
.more-badge {
  background: linear-gradient(135deg, #e0e0e0 0%, #f5f5f5 100%) !important;
  color: #666 !important;
  border: 1px solid #ddd;
  font-style: italic;
  cursor: help;
}

.more-badge:hover {
  background: linear-gradient(135deg, #d0d0d0 0%, #e5e5e5 100%) !important;
}

/* Button styling */
.btn {
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.2s ease-in-out;
}

.btn:hover {
  transform: translateY(-1px);
}

.btn-group .btn-check:checked + .btn {
  background-color: #0d6efd;
  border-color: #0d6efd;
  color: white;
}

.btn-group-vertical .btn {
  margin-bottom: 4px;
  text-align: left;
}

.btn-group-vertical .btn:last-child {
  margin-bottom: 0;
}

/* Form controls */
.form-select {
  border-radius: 6px;
  border: 2px solid #e9ecef;
  transition: border-color 0.2s ease-in-out;
}

.form-select:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}

/* Filter Section Styles */
.btn.active {
  background-color: #0d6efd;
  border-color: #0d6efd;
  color: white;
}

/* Top Filter Layout */
.filter-top {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

.filter-top .card-header {
  background: linear-gradient(135deg, #e9ecef 0%, #f8f9fa 100%);
  border-bottom: 1px solid #e9ecef;
  border-radius: 12px 12px 0 0;
  padding: 1rem;
}

.filter-top .card-title {
  color: #495057;
  font-weight: 600;
  margin: 0;
  font-size: 1.1rem;
}

.filter-top .card-body {
  padding: 1.5rem;
}

.filter-top .form-select-sm {
  font-size: 0.875rem;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  border: 2px solid #e9ecef;
  transition: all 0.3s ease;
}

.filter-top .form-select-sm:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
  transform: scale(1.02);
}

.filter-top .btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.filter-top .btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.filter-top .form-label {
  color: #495057;
  font-weight: 600;
  font-size: 0.85rem;
  margin-bottom: 0.5rem;
}

.text-transparent {
  color: transparent !important;
}

/* Legacy Sidebar Styles (kept for backwards compatibility) */
.filter-sidebar {
  position: sticky;
  top: 2rem;
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.filter-sidebar .card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
  border-radius: 12px 12px 0 0;
  padding: 1rem;
}

.filter-sidebar .card-title {
  color: #495057;
  font-weight: 600;
  margin: 0;
}

.filter-sidebar .card-body {
  padding: 1.25rem;
}

.filter-sidebar .form-select-sm {
  font-size: 0.875rem;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
}

.filter-sidebar .btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  border-radius: 6px;
}

.filter-sidebar .form-label {
  color: #495057;
  font-weight: 600;
}

/* Hover effects for filter elements */
.filter-sidebar .form-select:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.filter-sidebar .btn:hover {
  transform: translateY(-1px);
}

/* Responsive adjustments */
@media (min-width: 1920px) {
  /* Ultra-wide screens - increase table width */
  .product-table {
    min-width: 1500px;
  }

  .product-table th:nth-child(1),
  .product-table td:nth-child(1) {
    width: 220px;
  }

  .product-table th:nth-child(n+3):nth-child(-n+11),
  .product-table td:nth-child(n+3):nth-child(-n+11) {
    width: 110px;
  }

  .product-table th:nth-child(n+14):nth-child(-n+17),
  .product-table td:nth-child(n+14):nth-child(-n+17) {
    width: 180px;
  }
}

@media (max-width: 1400px) {
  /* Reduce table size for medium screens */
  .product-table {
    min-width: 900px;
  }

  .product-table th:nth-child(1),
  .product-table td:nth-child(1) {
    width: 160px;
  }

  .product-table th:nth-child(n+3):nth-child(-n+11),
  .product-table td:nth-child(n+3):nth-child(-n+11) {
    width: 80px;
  }

  .product-table th:nth-child(n+14):nth-child(-n+17),
  .product-table td:nth-child(n+14):nth-child(-n+17) {
    width: 130px;
  }
}

@media (max-width: 992px) {
  /* Tablet landscape */
  .product-table {
    min-width: 1000px;
  }

  .product-table thead th {
    padding: 6px 8px;
    font-size: 0.8rem;
  }

  .product-row td {
    padding: 6px 8px;
    font-size: 0.8rem;
  }

  .product-table th:nth-child(1),
  .product-table td:nth-child(1) {
    width: 140px;
  }

  .filter-sidebar {
    position: static;
  }

  /* Top filter responsive */
  .filter-top .card-body {
    padding: 1rem;
  }

  .filter-top .col-lg-2 {
    margin-bottom: 1rem;
  }
}

@media (max-width: 768px) {
  /* Mobile and tablet portrait */
  .product-grid {
    padding: 1rem 0;
  }

  .container-fluid {
    padding: 0 0.5rem;
  }

  .d-flex.justify-content-between {
    flex-direction: column;
    gap: 1rem;
  }

  .d-flex.gap-3 {
    width: 100%;
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .d-flex.gap-3 > * {
    margin-bottom: 0.5rem;
  }

  .filter-sidebar {
    position: static;
    margin-bottom: 1rem;
  }

  /* Top filter mobile styles */
  .filter-top {
    margin-bottom: 1rem;
  }

  .filter-top .card-body {
    padding: 0.75rem;
  }

  .filter-top .col-lg-2,
  .filter-top .col-md-4 {
    margin-bottom: 0.75rem;
  }

  .filter-top .form-label {
    font-size: 0.8rem;
  }

  .form-select {
    min-width: 150px;
  }

  .product-table {
    min-width: 800px;
  }

  .table-container {
    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;
  }
}

/* Pagination Controls Styling */
nav[aria-label="Product pagination"] {
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

nav[aria-label="Product pagination"] .btn {
  border-radius: 4px;
  font-size: 0.875rem;
  padding: 0.5rem 0.75rem;
  transition: all 0.2s ease;
}

nav[aria-label="Product pagination"] .btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

nav[aria-label="Product pagination"] .btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
  font-weight: 600;
}

nav[aria-label="Product pagination"] .btn-outline-primary:not(:disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(13, 110, 253, 0.3);
}

nav[aria-label="Product pagination"] .form-select-sm {
  border-radius: 4px;
  border: 2px solid #e9ecef;
  font-size: 0.875rem;
}

nav[aria-label="Product pagination"] .form-select-sm:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

@media (max-width: 576px) {
  /* Small mobile screens */
  .btn-group {
    display: none;
  }

  .container-fluid {
    padding: 0 0.25rem;
  }

  .product-table {
    min-width: 600px;
  }

  .product-table thead th,
  .product-row td {
    padding: 4px 6px;
    font-size: 0.75rem;
  }

  nav[aria-label="Product pagination"] .d-flex {
    flex-wrap: wrap;
  }

  nav[aria-label="Product pagination"] .btn {
    font-size: 0.75rem;
    padding: 0.375rem 0.5rem;
  }
}
</style>
