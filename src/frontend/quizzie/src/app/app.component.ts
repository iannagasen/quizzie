import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="h-screen">
      <app-mcq></app-mcq>
    </div>
  `
})
export class AppComponent {
  title = 'quizzie';
}
