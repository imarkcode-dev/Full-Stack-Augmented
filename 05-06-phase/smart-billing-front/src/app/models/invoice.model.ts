export interface InvoiceResponse {
  id: number;
  contractId: number;
  invoiceNumber: string;
  customerName: string;
  issueDate: string;
  dueDate: string;
  totalAmount: number;
  penaltyAmount?: number;
  status: 'PENDING' | 'PAID' | 'OVERDUE' | 'CANCELLED';
  createdAt: string;
  updatedAt: string;
}

export interface InvoiceRequest {
  contractId: number;
  invoiceNumber: string;
  issueDate: string;
  dueDate: string;
  totalAmount: number;
  penaltyAmount?: number;
  status: string;
}