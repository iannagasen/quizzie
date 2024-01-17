import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MatIconModule } from "@angular/material/icon";
import { INPUT_REQUIRED } from './constants';

@Component({
  selector: 'app-mcq-menu',
  standalone: true,
  imports: [ MatIconModule, CommonModule ],
  template: `
    <div class="h-full bg-amber-700 mr-4 duration-300 relative pt-2 p-1" [ngClass]="{'w-20': isSideBarOpen, 'w-64': !isSideBarOpen}">
      <div class="absolute -right-3 top-5" (click)="onToggleSideBar()">
        <mat-icon class="bg-white rounded-full border border-amber-700 cursor-pointer" [ngClass]="{'rotate-180': isSideBarOpen}">arrow_back</mat-icon>
      </div>

      <div class="font-bold inline-flex pt-2 pl-2 text-green-100">
          <mat-icon class="float-left duration-300 w-full" [ngClass]="{'transform scale-[2] translate-x-4 translate-y-4 rotate-[360deg]': isSideBarOpen}">layers</mat-icon>
          <span class="duration-300" [ngClass]="{'scale-0': isSideBarOpen}">QUIZZIE</span>
      </div>

      <div class="flex items-center rounded-md bg-orange-100 mt-6 px-2.5 py-2 mx-2">
        <mat-icon [class.mr-2]="isSideBarOpen"  class="text-amber-500 text-lg block float-left cursor-pointer">search</mat-icon>
        <input [hidden]="isSideBarOpen" type="search" placeholder="Search" class="text-base bg-transparent w-full text-amber-800 focus:outline-none"/>
      </div>

      <div class="mt-5 pl-2 font-extrabold" (click)="getMcqs()">MCQ</div>

    </div>  
  `
})
export class McqMenuBar {

  isSideBarOpen = false;

  @Output() onGetMcqs = new EventEmitter<void>();

  onToggleSideBar() {
    this.isSideBarOpen = !this.isSideBarOpen
  }

  getMcqs() {
    this.onGetMcqs.emit();
  }

}