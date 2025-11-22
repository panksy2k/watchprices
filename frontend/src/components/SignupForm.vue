<template>
    <div class="auth-form">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-md-6 col-lg-5">
            <div class="card shadow">
              <div class="card-header bg-primary text-white">
                <h2 class="card-title mb-0">Sign Up</h2>
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
                      minlength="6"
                      required 
                    />
                    <div class="form-text">Password must be at least 6 characters</div>
                  </div>
                  
                  <div class="mb-3">
                    <label for="confirmPassword" class="form-label fw-semibold">Confirm Password *</label>
                    <input 
                      type="password" 
                      id="confirmPassword" 
                      v-model="confirmPassword" 
                      class="form-control" 
                      placeholder="Confirm your password"
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
                      <span v-if="isSubmitting">Signing up...</span>
                      <span v-else>Sign Up</span>
                    </button>
                  </div>
                </form>

                <div class="text-center mt-3">
                  <p class="mb-0">
                    Already have an account? 
                    <router-link to="/login" class="text-primary fw-semibold">Login</router-link>
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
            confirmPassword: '',
            errorMessage: '',
            successMessage: '',
            isSubmitting: false
        };
    },
    methods: {
        submitForm() {
            this.errorMessage = '';
            this.successMessage = '';

            // Validate passwords match
            if (this.password !== this.confirmPassword) {
                this.errorMessage = 'Passwords do not match';
                return;
            }

            // Validate password length
            if (this.password.length < 6) {
                this.errorMessage = 'Password must be at least 6 characters';
                return;
            }

            this.isSubmitting = true;

            fetch('/api/auth/signup', {
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
                        throw new Error(err.message || 'Signup failed');
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Signup successful:', data);
                this.successMessage = 'Account created successfully! Redirecting to login...';
                
                // Reset form
                this.email = '';
                this.password = '';
                this.confirmPassword = '';
                
                // Redirect to login after 2 seconds
                setTimeout(() => {
                    this.$router.push('/login');
                }, 2000);
            })
            .catch((error) => {
                console.error('Error:', error);
                this.errorMessage = error.message || 'Failed to create account. Please try again.';
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
