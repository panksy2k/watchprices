import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {createHtmlPlugin} from 'vite-plugin-html'

// https://vite.dev/config/
export default defineConfig({
  appType: 'spa',
  plugins: [
    vue(),
    createHtmlPlugin({
      minify: true,
      inject: {
        data: {
          title: 'Sports Watch Manager | Track, View Your Sports Watch Collection',
        },
      },
    }),
  ],
  server: {
    port: 8080,
  },
  build: {
    rollupOptions: {
      output: {
        // Better code splitting for faster loading
        manualChunks: {
          vendor: ['vue', 'vue-router'],
        },
      },
    },
  },
})
