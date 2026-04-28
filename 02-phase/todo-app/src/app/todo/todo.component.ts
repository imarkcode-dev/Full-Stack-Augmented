import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Task } from '../models/task.model';

@Component({
  selector: 'app-todo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './todo.component.html',
  styleUrl: './todo.component.css'
})
export class TodoComponent {
  tasks: Task[] = [];
  newTaskTitle = '';

  addTask() {
    if (this.newTaskTitle.trim()) {
      const newTask: Task = {
        id: Date.now(),
        title: this.newTaskTitle.trim(),
        completed: false
      };
      this.tasks.push(newTask);
      this.newTaskTitle = '';
    }
  }

  toggleTask(task: Task) {
    task.completed = !task.completed;
  }

  deleteTask(task: Task) {
    this.tasks = this.tasks.filter(t => t !== task);
  }
}