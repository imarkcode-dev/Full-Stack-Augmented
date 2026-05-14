export interface PaymentResponse {
  id: number;
  invoiceId: number;
  paymentDate: string;
  amountPaid: number;
  paymentMethod: string;
  referenceNumber?: string;
  createdAt?: string;
  updatedAt?: string;
 
}


export interface PaymentRequest {
  invoiceId: number;
  paymentDate?: string;
  amountPaid: number;
  paymentMethod: string;
  referenceNumber?: string;
 
}