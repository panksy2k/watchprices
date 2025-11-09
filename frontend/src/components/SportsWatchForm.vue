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
                            <label for="modelName" class="form-label fw-semibold">Model Name *</label>
                            <input type="text" id="modelName" v-model="sportsWatch.modelName" class="form-control" required />
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="cost" class="form-label fw-semibold">Cost *</label>
                                <input type="number" id="cost" v-model="sportsWatch.cost" class="form-control" step="0.01" required />
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="currency" class="form-label fw-semibold">Currency *</label>
                                <select id="currency" v-model="sportsWatch.currency" class="form-select" required>
                                    <option value="">Select Currency</option>
                                    <option v-for="currency in currencies" :key="currency" :value="currency">{{ currency }}</option>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="color" class="form-label fw-semibold">Color *</label>
                                <select id="color" v-model="sportsWatch.color" class="form-select" required>
                                    <option value="">Select Color</option>
                                    <option v-for="color in colors" :key="color" :value="color">{{ color }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label fw-semibold">Description</label>
                            <textarea id="description" v-model="sportsWatch.description" class="form-control" rows="3" placeholder="Enter product description..."></textarea>
                        </div>
                    </div>

                    <!-- Physical Specifications Section -->
                    <div class="form-section mb-4">
                        <h5 class="section-title text-primary mb-3">Physical Specifications</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dimension" class="form-label fw-semibold">Dimension</label>
                                <input type="text" id="dimension" v-model="sportsWatch.dimension" class="form-control" placeholder="e.g., 45.4 x 45.4 x 13.5 mm" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="weight" class="form-label fw-semibold">Weight</label>
                                <input type="text" id="weight" v-model="sportsWatch.weight" class="form-control" placeholder="e.g., 63g" />
                            </div>
                        </div>
                    </div>

                    <!-- Display Specifications Section -->
                    <div class="form-section mb-4">
                        <h5 class="section-title text-primary mb-3">Display Specifications</h5>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="displaySize" class="form-label fw-semibold">Display Size</label>
                                <input type="text" id="displaySize" v-model="sportsWatch.displaySize" class="form-control" placeholder="e.g., 1.4 inches" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="displayResolution" class="form-label fw-semibold">Display Resolution</label>
                                <input type="text" id="displayResolution" v-model="sportsWatch.displayResolution" class="form-control" placeholder="e.g., 454 x 454 pixels" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="displayType" class="form-label fw-semibold">Display Type</label>
                                <input type="text" id="displayType" v-model="sportsWatch.displayType" class="form-control" placeholder="e.g., AMOLED" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="screenMaterial" class="form-label fw-semibold">Screen Material</label>
                                <input type="text" id="screenMaterial" v-model="sportsWatch.screenMaterial" class="form-control" placeholder="e.g., Corning Gorilla Glass" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dialType" class="form-label fw-semibold">Dial Type</label>
                                <input type="text" id="dialType" v-model="sportsWatch.dialType" class="form-control" placeholder="e.g., Digital" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="phoneConnectivity" class="form-label fw-semibold">Phone Connectivity</label>
                                <input type="text" id="phoneConnectivity" v-model="sportsWatch.phoneConnectivity" class="form-control" placeholder="e.g., Bluetooth 5.2, Wi-Fi" />
                            </div>
                        </div>
                    </div>

                    <!-- Durability & Performance Section -->
                    <div class="form-section mb-4">
                        <h5 class="section-title text-primary mb-3">Durability & Performance</h5>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="waterResistance" class="form-label fw-semibold">Water Resistance</label>
                                <input type="text" id="waterResistance" v-model="sportsWatch.waterResistance" class="form-control" placeholder="e.g., 10 ATM" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="workingTemperature" class="form-label fw-semibold">Working Temperature</label>
                                <input type="text" id="workingTemperature" v-model="sportsWatch.workingTemperature" class="form-control" placeholder="e.g., -20°C to 60°C" />
                            </div>
                        </div>
                    </div>

                    <!-- Battery & Storage Section -->
                    <div class="form-section mb-4">
                        <h5 class="section-title text-primary mb-3">Battery & Storage</h5>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="batteryLifeDailyUse" class="form-label fw-semibold">Battery Life (Daily Use)</label>
                                <input type="text" id="batteryLifeDailyUse" v-model="sportsWatch.batteryLifeDailyUse" class="form-control" placeholder="e.g., 14 days" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="chargingTime" class="form-label fw-semibold">Charging Time</label>
                                <input type="text" id="chargingTime" v-model="sportsWatch.chargingTime" class="form-control" placeholder="e.g., 2 hours" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="internalMemory" class="form-label fw-semibold">Internal Memory</label>
                                <input type="text" id="internalMemory" v-model="sportsWatch.internalMemory" class="form-control" placeholder="e.g., 32GB" />
                            </div>
                            <div class="col-md-6 mb-3 d-flex align-items-center">
                                <div class="form-check mt-4">
                                    <input type="checkbox" id="hasDownloadableGlobalMaps" v-model="sportsWatch.hasDownloadableGlobalMaps" class="form-check-input" />
                                    <label for="hasDownloadableGlobalMaps" class="form-check-label fw-semibold">Has Downloadable Global Maps</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Features & Capabilities Section -->
                    <div class="form-section mb-4">
                        <h5 class="section-title text-primary mb-3">Features & Capabilities</h5>
                        <div class="mb-3">
                            <label for="dataTrackingSensors" class="form-label fw-semibold">Data Tracking Sensors</label>
                            <input type="text" id="dataTrackingSensors" v-model="sportsWatch.dataTrackingSensors" class="form-control" placeholder="e.g., GPS, Heart Rate, Accelerometer (comma-separated)" />
                            <div class="form-text">Enter sensors separated by commas</div>
                        </div>
                        <div class="mb-3">
                            <label for="dailyFeatures" class="form-label fw-semibold">Daily Features</label>
                            <input type="text" id="dailyFeatures" v-model="sportsWatch.dailyFeatures" class="form-control" placeholder="e.g., Sleep Tracking, Stress Monitoring, Weather (comma-separated)" />
                            <div class="form-text">Enter features separated by commas</div>
                        </div>
                        <div class="mb-3">
                            <label for="thirdPartyIntegrationApps" class="form-label fw-semibold">Third-Party Integration Apps</label>
                            <input type="text" id="thirdPartyIntegrationApps" v-model="sportsWatch.thirdPartyIntegrationApps" class="form-control" placeholder="e.g., Strava, Spotify, Google Pay (comma-separated)" />
                            <div class="form-text">Enter app names separated by commas</div>
                        </div>
                        <div class="mb-3">
                            <label for="supportedActivities" class="form-label fw-semibold">Supported Activities</label>
                            <input type="text" id="supportedActivities" v-model="sportsWatch.supportedActivities" class="form-control" placeholder="e.g., Running, Cycling, Swimming, Golf (comma-separated)" />
                            <div class="form-text">Enter activities separated by commas</div>
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
                                        type="text"
                                        v-model="urlEntry.partner"
                                        class="form-control"
                                        placeholder="Partner Name (e.g., Amazon)"
                                    />
                                </div>
                                <div class="col-md-7">
                                    <input
                                        type="url"
                                        v-model="urlEntry.url"
                                        class="form-control"
                                        placeholder="https://example.com/affiliate-link"
                                    />
                                </div>
                                <div class="col-md-1">
                                    <button
                                        type="button"
                                        @click="removeAffiliateURL(index)"
                                        class="btn btn-outline-danger btn-sm"
                                        :disabled="sportsWatch.affiliateMarketingDeepURLs.length === 1"
                                        title="Remove URL"
                                    >
                                        ×
                                    </button>
                                </div>
                            </div>
                            <button
                                type="button"
                                @click="addAffiliateURL"
                                class="btn btn-outline-secondary btn-sm mt-2">
                                <i class="bi bi-plus"></i> Add Affiliate URL
                            </button>
                        </div>
                    </div>

                    <div class="d-grid gap-2 mt-4">
                        <button type="submit" class="btn btn-primary btn-lg">Add Sports Watch</button>
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
                dailyFeatures: '',
                thirdPartyIntegrationApps: '',
                supportedActivities: '',
                color: '',
                affiliateMarketingDeepURLs: [
                    { partner: '', url: '' }
                ],
            },
            currencies: ['GBP', 'EUR', 'USD', 'CAD'],
            colors: ['BLACK', 'WHITE', 'RED', 'BLUE', 'GREEN', 'YELLOW', 'ORANGE', 'PURPLE', 'PINK', 'GREY'],
        };
    },
    methods: {
        addAffiliateURL() {
            this.sportsWatch.affiliateMarketingDeepURLs.push({ partner: '', url: '' });
        },
        removeAffiliateURL(index) {
            if (this.sportsWatch.affiliateMarketingDeepURLs.length > 1) {
                this.sportsWatch.affiliateMarketingDeepURLs.splice(index, 1);
            }
        },
        submitForm() {
            // Convert affiliateMarketingDeepURLs array to Map format for backend
            const formData = { ...this.sportsWatch };
            const affiliateMap = {};

            this.sportsWatch.affiliateMarketingDeepURLs.forEach(entry => {
                if (entry.partner && entry.url) {
                    affiliateMap[entry.partner] = entry.url;
                }
            });

            formData.affiliateMarketingDeepURL = affiliateMap;
            delete formData.affiliateMarketingDeepURLs;

            // Convert comma-separated strings to arrays
            const arrayFields = ['dataTrackingSensors', 'dailyFeatures', 'thirdPartyIntegrationApps', 'supportedActivities'];
            arrayFields.forEach(field => {
                if (formData[field] && typeof formData[field] === 'string') {
                    formData[field] = formData[field].split(',').map(item => item.trim());
                }
            });

            fetch('/api/products', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
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
                dailyFeatures: '',
                thirdPartyIntegrationApps: '',
                supportedActivities: '',
                color: '',
                affiliateMarketingDeepURLs: [
                    { partner: '', url: '' }
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

