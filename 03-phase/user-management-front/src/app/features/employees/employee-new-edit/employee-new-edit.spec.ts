import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeNewEdit } from './employee-new-edit';

describe('EmployeeNewEdit', () => {
  let component: EmployeeNewEdit;
  let fixture: ComponentFixture<EmployeeNewEdit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeNewEdit],
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeeNewEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
