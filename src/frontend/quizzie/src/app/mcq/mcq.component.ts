import { Component } from '@angular/core';
import { McqService } from './mcq.service';
import { Mcq } from './types';

@Component({
  selector: 'app-mcq',
  template: `
   <div class="flex h-full p-5">
    <!-- Menu Bar -->
    <div class="bg-amber-700 mr-4 duration-300 relative pt-2 p-1" [ngClass]="{'w-20': isSideBarOpen, 'w-64': !isSideBarOpen}">
      <div class="absolute -right-3 top-5" (click)="toggleSideBar()">
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


    <!-- Content -->
    <div class="flex-1 flex justify-center items-center py-5">
      <!-- <div *ngFor="let mcq of mcqs; let i = index" class="m-2">
        <div class="bg-green-200 rounded-md p-3">{{ mcq.question }}</div>
        <div *ngFor="let choice of mcq.choices; let j = index">
          <div [ngClass]="{'pt-1': (j === 0)}">{{ choice.value }}</div>
          <div>{{ choice.explanation }}</div>
        </div>
      </div> -->
      <div class="w-[600px] h-full">
        <!-- Options -->
        <div class="flex items-center h-8">
          <div class="flex-auto rounded-sm border border-blue-200 mr-3">MCQ</div>
          <div class="w-[150px]">Cards/Page</div>
        </div>
        
        <ng-container *ngIf="isRenderAsCards">
          <div *ngIf="mcqs && mcqs.length > 0" class="flex items-center justify-center h-full">
            <div class="rounded-lg bg-green-200 w-[350px] h-[500px] shadow-md shadow-slate-300 p-4 flex flex-col items-stretch relative">
              <div class="absolute bg-amber-400 rounded-t-sm w-7 h-8 -top-1 left-5 font-extrabold text-xl text-right px-1">
                {{ currentIndex }}
              </div>
              <div class="mt-3 py-2 font-semibold">
                {{ mcqs[currentIndex].question }}
              </div>
              <div class="flex-auto">
                <div *ngFor="let choice of mcqs[currentIndex].choices; let choice_i = index" class="my-1">
                  <div [ngClass]="{'text-amber-700 text-l font-extrabold': willShowAnswer && choice.correct}">
                    <span>{{ loopIndexToLetters[choice_i] + '.'}}</span>
                    {{ choice.value }}
                  </div>
                  <div class="transform duration-500" [ngClass]="{'scale-y-0 h-0': !willShowAnswer, 'scale-y-1 h-auto': willShowAnswer}">
                    {{ choice.explanation }}
                  </div>
                </div>
              </div>
              <div class="h-3 flex justify-between items-center text-xs">
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="goPrevious()">Previous</div>
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="willShowAnswer ? hideAnswers() : showAnswers()">
                  {{ willShowAnswer ? 'Hide Answers' : 'Show Answers' }}
                </div>
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="goNext()">Next</div>
              </div>
            </div>
          </div>
        </ng-container>
      </div>
    </div>

    <!-- Left Bar -->
    <div class="bg-gray-400 w-52 ml-4">3rd</div>
  </div>
  `,
})
export class McqComponent {

  isRenderAsCards = true;
  currentIndex = 0;
  willShowAnswer = false;

  loopIndexToLetters: { [key: number]: string } = {
    0: 'a',
    1: 'b',
    2: 'c',
    3: 'd',
    4: 'e',
    5: 'f'
  }

  isSideBarOpen = false;
  mcqs: Mcq[] = [];


  constructor(
    private _mcqService: McqService
  ) { }

  toggleSideBar() {
    this.isSideBarOpen = !this.isSideBarOpen;
  }

  showMcqs() {
    
  }

  goNext() {
    if (this.currentIndex < this.mcqs.length - 1) {
      this.currentIndex++;
      this.willShowAnswer = false;
    }
  }

  goPrevious() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.willShowAnswer = false;
    }
  }

  showAnswers() {
    this.willShowAnswer = true;
  }

  hideAnswers() {
    this.willShowAnswer = false;
  }

  getMcqs(){
    const topic = 'AWS';
    this._mcqService.getQuestions(topic)
      .subscribe({
        next: (data: Mcq[]) =>  this.mcqs = data,
        error: (err) => console.log(err),
        complete: () =>  console.log(this.mcqs)
      })
  }

}