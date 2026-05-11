export interface CustomerResponse {
  id: number;
  nameCustomer: string;
  email: string;
  phone: string;
  address: string;
  status: 'ACTIVE' | 'INACTIVE';
}

export interface CustomerRequest {
  nameCustomer: string;
  email: string;
  phone: string;
  address: string;
}