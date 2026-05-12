
export interface ContractResponse {
  id?: number;
  customerId: number;
  title: string;
  startDate: string;
  endDate?: string;
  monthlyFee: number;
  currency: string;
  status: 'ACTIVE' | 'INACTIVE';

}

export interface ContractRequest {
  customerId: number;
  title: string;
  startDate: string;
  endDate?: string;
  monthlyFee: number;
  currency: string;
  
}