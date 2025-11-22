<template>
    <div class="auth-form">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-md-6 col-lg-5">
            <div class="card shadow">
              <div class="card-header bg-primary text-white">
                <h2 class="card-title mb-0">Login</h2>
              </div>
              <div class="card-body">
                <form @submit.prevent="submitForm">
                  <div class="mb-3">
                    <label for="email" class="form-label fw-semibold">Email Address *</label>
                    <input 
                      type="email" 
                      id="email" 
                      v-model="email" 
                      class="form-control" 
                      placeholder="Enter your email"
                      required 
                    />
                  </div>
                  
                  <div class="mb-3">
                    <label for="password" class="form-label fw-semibold">Password *</label>
                    <input 
                      type="password" 
                      id="password" 
                      v-model="password" 
                      class="form-control" 
                      placeholder="Enter your password"
                      required 
                    />
                  </div>

                  <div v-if="errorMessage" class="alert alert-danger" role="alert">
                    {{ errorMessage }}
                  </div>

                  <div v-if="successMessage" class="alert alert-success" role="alert">
                    {{ successMessage }}
                  </div>
                  
                  <div class="d-grid gap-2 mt-4">
                    <button type="submit" class="btn btn-primary btn-lg" :disabled="isSubmitting">
                      <span v-if="isSubmitting">Logging in...</span>
                      <span v-else>Login</span>
                    </button>
                  </div>
                </form>

                <div class="text-center mt-3">
                  <p class="mb-0">
                    Don't have an account? 
                    <router-link to="/signup" class="text-primary fw-semibold">Sign up</router-link>
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
    data() {
        return {
            email: '',
            password: '',
            errorMessage: '',
            successMessage: '',
            isSubmitting: false
        };
    },
    methods: {
        submitForm() {
            this.errorMessage = '';
            this.successMessage = '';
            this.isSubmitting = true;

            fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: this.email,
                    password: this.password
                }),
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Login failed');
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Login successful:', data);
                
                // Store the authentication token
                if (data.token) {
                    localStorage.setItem('authToken', data.token);
                    this.successMessage = 'Login successful! Redirecting...';
                    
                    // Reset form
                    this.email = '';
                    this.password = '';
                    
                    // Redirect to home/list page after 1 second
                    setTimeout(() => {
                        this.$router.push('/list');
                    }, 1000);
                } else {
                    throw new Error('No token received from server');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                this.errorMessage = error.message || 'Invalid email or password. Please try again.';
            })
            .finally(() => {
                this.isSubmitting = false;
            });
        }
    }
};
</script>

<style scoped>
.auth-form {
    min-height: 100vh;
    background-color: #f8f9fa;
    padding: 2rem 0;
    display: flex;
    align-items: center;
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
    text-align: center;
}

.card-body {
    padding: 2rem;
}

.form-label {
    color: #495057;
    margin-bottom: 0.5rem;
}

.form-control {
    border-radius: 6px;
    border: 1px solid #ced4da;
    transition: all 0.2s ease-in-out;
}

.form-control:focus {
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

.btn-primary:hover:not(:disabled) {
    background: linear-gradient(135deg, #0a58ca 0%, #084298 100%);
    transform: translateY(-1px);
    box-shadow: 0 4px 8px rgba(13, 110, 253, 0.3);
}

.btn-primary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.alert {
    border-radius: 6px;
    font-size: 0.9rem;
}

.shadow {
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

@media (max-width: 768px) {
    .auth-form {
        padding: 1rem 0;
    }

    .card-body {
        padding: 1.5rem;
    }
}
</style>
