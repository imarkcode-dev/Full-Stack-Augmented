/**
 * Data Transfer Objects for authentication operations.
 * LoginRequest defines the structure for login credentials.
 * AuthResponse defines the structure for authentication response containing the token.
 */

export interface LoginRequest {
    username: string;
    password: string;
}

export interface AuthResponse {
    token: string;
}

