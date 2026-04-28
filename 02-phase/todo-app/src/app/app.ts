import { Component, signal } from '@angular/core';
import { TodoComponent } from './todo/todo.component';

@Component({
  selector: 'app-root',
  imports: [TodoComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('todo-app');
}
