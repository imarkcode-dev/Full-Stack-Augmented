import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ContractService } from '../../../core/services/contract.service';
import { ContractResponse } from '../../../models/contract.model';
import { ConfirmDialog } from '../../../shared/confirm-dialog/confirm-dialog/confirm-dialog';
import { Contract } from '../contract/contract'; 


@Component({
  selector: 'app-contract-list',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatTooltipModule],
  templateUrl: './contract-list.html',
  styleUrl: './contract-list.scss',
})
export class ContractList implements OnInit {

  private contractService = inject(ContractService);
  private dialog = inject(MatDialog);

  contracts = signal<ContractResponse[]>([]);
  
  displayedColumns: string[] = ['title', 'customer', 'fee', 'status', 'actions'];

  ngOnInit() { 
    this.loadContracts(); 
  }

  loadContracts() {
    this.contractService.getAll().subscribe({
      next: (data) => this.contracts.set(data),
      error: (err) => console.error('Error loading contracts:', err)
    });
  }

  onAdd() {
    const dialogRef = this.dialog.open(Contract, { 
      width: '600px',
      disableClose: true 
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.loadContracts();
    });
  }

  onEdit(contract: ContractResponse) {
    const dialogRef = this.dialog.open(Contract, { 
      width: '600px', 
      data: contract,
      disableClose: true 
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.loadContracts();
    });
  }

  onDelete(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      data: { message: 'Delete this contract?.' }
    });
    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.contractService.delete(id).subscribe(() => this.loadContracts());
      }
    });
  }

  


}
