
export interface CashFlowForecast {
  [month: string]: number;
}


export interface DashboardResponse {
  totalInvoiced: number; 
  totalCollected: number;
  overdueAmount: number;
  cashFlowForecast: CashFlowForecast;
}