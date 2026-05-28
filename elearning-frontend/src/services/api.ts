
import axios from 'axios';

// Importações de tipos (apenas para TypeScript)
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import type { AuthResponse } from '../types';


class ApiService {
  private api: AxiosInstance;
  private refreshTokenPromise: Promise<string> | null = null;

  constructor() {
    this.api = axios.create({
      baseURL: 'http://localhost:8080/api',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  

  private setupInterceptors() {
    // Request interceptor
    this.api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('access_token');
  console.log('Interceptor - Token:', token);
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log('Interceptor - Header set:', config.headers.Authorization);
  }
  return config;
});


    // Response interceptor
    this.api.interceptors.response.use(
      (response: AxiosResponse) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };
        
        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          
          try {
            const newToken = await this.refreshToken();
            if (originalRequest.headers) {
              originalRequest.headers.Authorization = `Bearer ${newToken}`;
            }
            return this.api(originalRequest);
          } catch (refreshError) {
            this.logout();
            window.location.href = '/login';
            return Promise.reject(refreshError);
          }
        }
        
        return Promise.reject(error);
      }
    );
  }

  private async refreshToken(): Promise<string> {
    if (this.refreshTokenPromise) {
      return this.refreshTokenPromise;
    }

    this.refreshTokenPromise = new Promise(async (resolve, reject) => {
      try {
        const refreshToken = localStorage.getItem('refresh_token');
        if (!refreshToken) {
          throw new Error('No refresh token');
        }

        
        const response = await this.api.post<AuthResponse>('/auth/refresh', {
            refreshToken: refreshToken,
        });

        localStorage.setItem('access_token', response.data.accessToken); 
        localStorage.setItem('refresh_token', response.data.refreshToken); 
        
        resolve(response.data.accessToken);
      } catch (error) {
        reject(error);
      } finally {
        this.refreshTokenPromise = null;
      }
    });

    return this.refreshTokenPromise;
  }

  public logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
  }

  public getApi() {
    return this.api;
  }
}

export const apiService = new ApiService();
export const api = apiService.getApi();