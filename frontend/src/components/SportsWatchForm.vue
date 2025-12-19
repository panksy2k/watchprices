<template>
  <div class="sports-watch-form">
    <div class="container">
      <div class="card shadow">
        <div class="card-header bg-primary text-white">
          <h2 class="card-title mb-0">Add Sports Watch</h2>
        </div>
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <!-- Basic Information Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Basic Information</h5>
              <div class="mb-3">
                <label class="form-label fw-semibold" for="modelName">Model Name *</label>
                <input id="modelName" v-model="sportsWatch.modelName" class="form-control" required type="text"/>
              </div>
              <div class="row">
                <div class="col-md-4 mb-3">
                  <label class="form-label fw-semibold" for="cost">Cost *</label>
                  <input id="cost" v-model="sportsWatch.cost" class="form-control" required step="0.01" type="number"/>
                </div>
                <div class="col-md-4 mb-3">
                  <label class="form-label fw-semibold" for="currency">Currency *</label>
                  <select id="currency" v-model="sportsWatch.currency" class="form-select" required>
                    <option value="">Select Currency</option>
                    <option v-for="currency in currencies" :key="currency" :value="currency">{{ currency }}</option>
                  </select>
                </div>
                <div class="col-md-4 mb-3">
                  <label class="form-label fw-semibold" for="color">Color *</label>
                  <select id="color" v-model="sportsWatch.color" class="form-select" required>
                    <option value="">Select Color</option>
                    <option v-for="color in colors" :key="color" :value="color">{{ color }}</option>
                  </select>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold" for="description">Description</label>
                <textarea id="description" v-model="sportsWatch.description" class="form-control"
                          placeholder="Enter product description..."
                          rows="3"></textarea>
              </div>
            </div>

            <!-- Physical Specifications Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Physical Specifications</h5>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="dimension">Dimension</label>
                  <input id="dimension" v-model="sportsWatch.dimension" class="form-control"
                         placeholder="e.g., 45.4 x 45.4 x 13.5 mm"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="weight">Weight</label>
                  <input id="weight" v-model="sportsWatch.weight" class="form-control" placeholder="e.g., 63g"
                         type="text"/>
                </div>
              </div>
            </div>

            <!-- Display Specifications Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Display Specifications</h5>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="displaySize">Display Size</label>
                  <input id="displaySize" v-model="sportsWatch.displaySize" class="form-control"
                         placeholder="e.g., 1.4 inches"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="displayResolution">Display Resolution</label>
                  <input id="displayResolution" v-model="sportsWatch.displayResolution" class="form-control"
                         placeholder="e.g., 454 x 454 pixels"
                         type="text"/>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="displayType">Display Type</label>
                  <input id="displayType" v-model="sportsWatch.displayType" class="form-control"
                         placeholder="e.g., AMOLED"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="screenMaterial">Screen Material</label>
                  <input id="screenMaterial" v-model="sportsWatch.screenMaterial" class="form-control"
                         placeholder="e.g., Corning Gorilla Glass"
                         type="text"/>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="dialType">Dial Type</label>
                  <input id="dialType" v-model="sportsWatch.dialType" class="form-control" placeholder="e.g., Digital"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="phoneConnectivity">Phone Connectivity</label>
                  <input id="phoneConnectivity" v-model="sportsWatch.phoneConnectivity" class="form-control"
                         placeholder="e.g., Bluetooth 5.2, Wi-Fi"
                         type="text"/>
                </div>
              </div>
            </div>

            <!-- Durability & Performance Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Durability & Performance</h5>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="waterResistance">Water Resistance</label>
                  <input id="waterResistance" v-model="sportsWatch.waterResistance" class="form-control"
                         placeholder="e.g., 10 ATM"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="workingTemperature">Working Temperature</label>
                  <input id="workingTemperature" v-model="sportsWatch.workingTemperature" class="form-control"
                         placeholder="e.g., -20°C to 60°C" type="text"/>
                </div>
              </div>
            </div>

            <!-- Battery & Storage Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Battery & Storage</h5>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="batteryLifeDailyUse">Battery Life (Daily Use)</label>
                  <input id="batteryLifeDailyUse" v-model="sportsWatch.batteryLifeDailyUse" class="form-control"
                         placeholder="e.g., 14 days" type="text"/>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="chargingTime">Charging Time</label>
                  <input id="chargingTime" v-model="sportsWatch.chargingTime" class="form-control"
                         placeholder="e.g., 2 hours"
                         type="text"/>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold" for="internalMemory">Internal Memory</label>
                  <input id="internalMemory" v-model="sportsWatch.internalMemory" class="form-control"
                         placeholder="e.g., 32GB"
                         type="text"/>
                </div>
                <div class="col-md-6 mb-3 d-flex align-items-center">
                  <div class="form-check mt-4">
                    <input id="hasDownloadableGlobalMaps" v-model="sportsWatch.hasDownloadableGlobalMaps"
                           class="form-check-input" type="checkbox"/>
                    <label class="form-check-label fw-semibold" for="hasDownloadableGlobalMaps">Has Downloadable Global
                      Maps</label>
                  </div>
                </div>
              </div>
            </div>

            <!-- Features & Capabilities Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Features & Capabilities</h5>
              <div class="mb-3">
                <label class="form-label fw-semibold" for="dataTrackingSensors">Data Tracking Sensors</label>
                <input id="dataTrackingSensors" v-model="sportsWatch.dataTrackingSensors" class="form-control"
                       placeholder="e.g., GPS, Heart Rate, Accelerometer (comma-separated)" type="text"/>
                <div class="form-text">Enter sensors separated by commas</div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Daily Features</label>
                <div v-if="loadingDailyFeatures" class="text-muted">
                  <span aria-hidden="true" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  Loading daily features...
                </div>
                <div v-else-if="dailyFeaturesError" class="alert alert-warning" role="alert">
                  {{ dailyFeaturesError }}
                </div>
                <div v-else class="row">
                  <div v-for="feature in availableDailyFeatures" :key="feature" class="col-md-6 mb-2">
                    <div class="form-check">
                      <input
                        :id="'dailyFeature-' + feature"
                        v-model="sportsWatch.dailyFeatures"
                       :value="feature"                      
                        class="form-check-input"
                        type="checkbox"
                      />
                      <label :for="'dailyFeature-' + feature" class="form-check-label">
                        {{ feature }}
                      </label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Third-Party Integration Apps</label>
                <div v-if="loadingThirdPartyApps" class="text-muted">
                  <span aria-hidden="true" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  Loading third-party apps...
                </div>
                <div v-else-if="thirdPartyAppsError" class="alert alert-warning" role="alert">
                  {{ thirdPartyAppsError }}
                </div>
                <div v-else class="row">
                  <div v-for="app in availableThirdPartyApps" :key="app" class="col-md-6 mb-2">
                    <div class="form-check">
                      <input
                        :id="'thirdPartyApp-' + app"
                        v-model="sportsWatch.thirdPartyIntegrationApps"
                        :value="app"
                        class="form-check-input"
                        type="checkbox"
                      />
                      <label :for="'thirdPartyApp-' + app" class="form-check-label">
                        {{ app }}
                      </label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Supported Activities</label>
                <div v-if="loadingSupportedActivities" class="text-muted">
                  <span aria-hidden="true" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  Loading supported activities...
                </div>
                <div v-else-if="supportedActivitiesError" class="alert alert-warning" role="alert">
                  {{ supportedActivitiesError }}
                </div>
                <div v-else class="row">
                  <div v-for="activity in availableSupportedActivities" :key="activity" class="col-md-6 mb-2">
                    <div class="form-check">
                      <input
                        :id="'supportedActivity-' + activity"
                        v-model="sportsWatch.supportedActivities"
                        :value="activity"
                        class="form-check-input"
                        type="checkbox"
                      />
                      <label :for="'supportedActivity-' + activity" class="form-check-label">
                        {{ activity }}
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Affiliate Marketing Section -->
            <div class="form-section mb-4">
              <h5 class="section-title text-primary mb-3">Affiliate Marketing URLs</h5>
              <div class="mb-3">
                <label class="form-label fw-semibold">Partner Links</label>
                <div v-for="(urlEntry, index) in sportsWatch.affiliateMarketingDeepURLs" :key="index" class="row mb-2">
                  <div class="col-md-4">
                    <input
                      v-model="urlEntry.partner"
                      class="form-control"
                      placeholder="Partner Name (e.g., Amazon)"
                      type="text"
                    />
                  </div>
                  <div class="col-md-7">
                    <input
                      v-model="urlEntry.url"
                      class="form-control"
                      placeholder="https://example.com/affiliate-link"
                      type="url"
                    />
                  </div>
                  <div class="col-md-1">
                    <button
                      :disabled="sportsWatch.affiliateMarketingDeepURLs.length === 1"
                      class="btn btn-outline-danger btn-sm"
                      title="Remove URL"
                      type="button"
                      @click="removeAffiliateURL(index)"
                    >
                      ×
                    </button>
                  </div>
                </div>
                <button
                  class="btn btn-outline-secondary btn-sm mt-2"
                  type="button"
                  @click="addAffiliateURL">
                  <i class="bi bi-plus"></i> Add Affiliate URL
                </button>
              </div>
            </div>

            <div class="d-grid gap-2 mt-4">
              <button class="btn btn-primary btn-lg" type="submit">Add Sports Watch</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      sportsWatch: {
        productType: 'SPORTSWATCH',
        modelName: '',
        cost: '',
        currency: '',
        description: '',
        dimension: '',
        weight: '',
        displaySize: '',
        displayResolution: '',
        displayType: '',
        screenMaterial: '',
        dialType: '',
        phoneConnectivity: '',
        waterResistance: '',
        workingTemperature: '',
        batteryLifeDailyUse: '',
        chargingTime: '',
        internalMemory: '',
        hasDownloadableGlobalMaps: false,
        dataTrackingSensors: '',
        dailyFeatures: [],
        thirdPartyIntegrationApps: [],
        supportedActivities: [],
        color: '',
        affiliateMarketingDeepURLs: [
          {partner: '', url: ''}
        ],
      },
      currencies: ['GBP', 'EUR', 'USD', 'CAD'],
      colors: ['BLACK', 'WHITE', 'RED', 'BLUE', 'GREEN', 'YELLOW', 'ORANGE', 'PURPLE', 'PINK', 'GREY'],
      availableDailyFeatures: [],
      loadingDailyFeatures: false,
      dailyFeaturesError: null,
      availableThirdPartyApps: [],
      loadingThirdPartyApps: false,
      thirdPartyAppsError: null,
      availableSupportedActivities: [],
      loadingSupportedActivities: false,
      supportedActivitiesError: null,
    };
  },
  mounted() {
    this.fetchDailyFeatures();
    this.fetchThirdPartyApps();
    this.fetchSupportedActivities();
  },
  methods: {
    fetchDailyFeatures() {
      this.loadingDailyFeatures = true;
      this.dailyFeaturesError = null;

      fetch('/api/products/SPORTSWATCH/watchFeatures/dailyFeatures', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch daily features');
          }
          return response.json();
        })
        .then(data => {
          // Assuming the response is {"dailyFeatures": ["feature1", "feature2", ...]}
          this.availableDailyFeatures = [...new Set(data.dailyFeatures || [])];
          this.loadingDailyFeatures = false;
        })
        .catch(error => {
          console.error('Error fetching daily features:', error);
          this.dailyFeaturesError = 'Failed to load daily features. Please try again later.';
          this.loadingDailyFeatures = false;
        });
    },
    fetchThirdPartyApps() {
      this.loadingThirdPartyApps = true;
      this.thirdPartyAppsError = null;

      fetch('/api/products/SPORTSWATCH/watchFeatures/thirdPartyIntegration', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch third-party apps');
          }
          return response.json();
        })
        .then(data => {
          this.availableThirdPartyApps = [...new Set(data.thirdPartyIntegration || [])];
          this.loadingThirdPartyApps = false;
        })
        .catch(error => {
          console.error('Error fetching third-party apps:', error);
          this.thirdPartyAppsError = 'Failed to load third-party apps. Please try again later.';
          this.loadingThirdPartyApps = false;
        });
    },
    fetchSupportedActivities() {
      this.loadingSupportedActivities = true;
      this.supportedActivitiesError = null;

      fetch('/api/products/SPORTSWATCH/watchFeatures/supportedActivities', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Failed to fetch supported activities');
          }
          return response.json();
        })
        .then(data => {
          this.availableSupportedActivities = [...new Set(data.supportedActivities || [])];
          this.loadingSupportedActivities = false;
        })
        .catch(error => {
          console.error('Error fetching supported activities:', error);
          this.supportedActivitiesError = 'Failed to load supported activities. Please try again later.';
          this.loadingSupportedActivities = false;
        });
    },
    addAffiliateURL() {
      this.sportsWatch.affiliateMarketingDeepURLs.push({partner: '', url: ''});
    },
    removeAffiliateURL(index) {
      if (this.sportsWatch.affiliateMarketingDeepURLs.length > 1) {
        this.sportsWatch.affiliateMarketingDeepURLs.splice(index, 1);
      }
    },
    submitForm() {
      // Convert affiliateMarketingDeepURLs array to Map format for backend
      const formData = {...this.sportsWatch};
      const affiliateMap = {};

      this.sportsWatch.affiliateMarketingDeepURLs.forEach(entry => {
        if (entry.partner && entry.url) {
          affiliateMap[entry.partner] = entry.url;
        }
      });

      formData.affiliateMarketingDeepURL = affiliateMap;
      delete formData.affiliateMarketingDeepURLs;

      // Convert comma-separated strings to arrays (dailyFeatures, thirdPartyIntegrationApps, and supportedActivities are already arrays from checkboxes)
      const arrayFields = ['dataTrackingSensors'];
      arrayFields.forEach(field => {
        if (formData[field] && typeof formData[field] === 'string') {
          formData[field] = formData[field].split(',').map(item => item.trim());
        }
      });

      const token = localStorage.getItem('authToken');
      if (!token) {
        alert('Please login first to add a sports watch.');
        return;
      }

      fetch('/api/secured/products', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
          alert('Sports watch added successfully!');
          // Reset form
          this.resetForm();
        })
        .catch((error) => {
          console.error('Error:', error);
          alert('Failed to add sports watch.');
        });
    },
    resetForm() {
      this.sportsWatch = {
        productType: 'SPORTSWATCH',
        modelName: '',
        cost: '',
        currency: '',
        description: '',
        dimension: '',
        weight: '',
        displaySize: '',
        displayResolution: '',
        displayType: '',
        screenMaterial: '',
        dialType: '',
        phoneConnectivity: '',
        waterResistance: '',
        workingTemperature: '',
        batteryLifeDailyUse: '',
        chargingTime: '',
        internalMemory: '',
        hasDownloadableGlobalMaps: false,
        dataTrackingSensors: '',
        dailyFeatures: [],
        thirdPartyIntegrationApps: [],
        supportedActivities: [],
        color: '',
        affiliateMarketingDeepURLs: [
          {partner: '', url: ''}
        ],
      };
    },
  },
};
</script>

<style scoped>
.sports-watch-form {
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 2rem 0;
}

.sports-watch-form .container {
  max-width: 900px;
}

.card {
  border: none;
  border-radius: 12px;
}

.card-header {
  border-radius: 12px 12px 0 0 !important;
  padding: 1.5rem;
  background: linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%) !important;
}

.card-title {
  font-size: 1.75rem;
  font-weight: 600;
}

.card-body {
  padding: 2rem;
}

.form-section {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  border-left: 4px solid #0d6efd;
}

.section-title {
  font-weight: 600;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e9ecef;
}

.form-label {
  color: #495057;
  margin-bottom: 0.5rem;
}

.form-control, .form-select {
  border-radius: 6px;
  border: 1px solid #ced4da;
  transition: all 0.2s ease-in-out;
}

.form-control:focus, .form-select:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.form-text {
  font-size: 0.875rem;
  color: #6c757d;
  margin-top: 0.25rem;
}

.btn-primary {
  background: linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%);
  border: none;
  border-radius: 6px;
  font-weight: 500;
  padding: 0.75rem 1.5rem;
  transition: all 0.2s ease-in-out;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #0a58ca 0%, #084298 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(13, 110, 253, 0.3);
}

.btn-outline-secondary {
  border-radius: 6px;
  font-weight: 500;
}

.btn-outline-danger {
  border-radius: 4px;
}

.form-check-input {
  border-radius: 4px;
}

.form-check-input:checked {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.shadow {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

@media (max-width: 768px) {
  .sports-watch-form {
    padding: 1rem 0;
  }

  .card-body {
    padding: 1rem;
  }

  .form-section {
    padding: 1rem;
  }
}
</style>

