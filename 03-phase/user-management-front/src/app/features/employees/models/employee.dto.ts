/**
 * Data Transfer Object for employee data, representing the structure
 * of employee information including optional ID and required fields.
 */

export interface EmployeeDTO {
    id?: number;
    name: string;
    lastName: string;
    email: string;
    phone: string;

}