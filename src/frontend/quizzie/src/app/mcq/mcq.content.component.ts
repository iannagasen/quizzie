import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { AddChoiceDTO, Mcq, McqChoice, UpdateMcqDTO } from './types';
import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { INPUT_REQUIRED } from "./constants";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: 'app-mcq-content',
  standalone: true,
  imports: [ CommonModule, MatIconModule, ReactiveFormsModule ],
  template: `
    <div class="flex-1 flex justify-center items-center py-5">
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
                {{ currentIndex + 1 }}
              </div>
              <div class="text-right flex justify-end">
                <div *ngIf="isEditMode" class="pr-2 flex justify-end">
                  <div class="scale-[0.8]" (click)="willEditMcq()"><mat-icon>edit</mat-icon></div>
                  <div class="scale-[0.8]" (click)="willAddChoice()"><mat-icon>add</mat-icon></div>
                </div>
                <div>
                  <div class="scale-[0.8]" (click)="goEditMode()" *ngIf="!isEditMode">
                    <mat-icon>more_vert</mat-icon>
                  </div>
                  <div class="scale-[0.8]" (click)="saveChanges()" *ngIf="isEditMode">
                    <mat-icon>update</mat-icon>
                  </div>
                </div>
              </div>
              <div class="mt-3 py-2 font-semibold">
                {{ getMcqToRender().question }}
              </div>
              <div class="flex-auto">
                <div *ngFor="let choice of getMcqToRender().choices; let choice_i = index" class="my-1">
                  <div [ngClass]="{'text-amber-700 text-l font-extrabold': isAnswerShowing && choice.correct}">
                    <span>{{ loopIndexToLetters[choice_i] + '.'}}</span>
                    {{ choice.value }}
                  </div>
                  <div class="transform duration-500" [ngClass]="{'scale-y-0 h-0': !isAnswerShowing, 'scale-y-1 h-auto': isAnswerShowing}">
                    {{ choice.explanation }}
                  </div>
                </div>
                
                <div *ngIf="isAddingChoice">
                  <form [formGroup]="addChoiceForm" (ngSubmit)="addChoice()" class="max-w-sm mx-auto-4 mt-5 px-2 pt-2 rounded-md border-2 border-white shadow-sm shadow-slate-200">
                    <div class="mb-2">
                      <label class="block mb-2 text-sm text-gray-900">Value</label>
                      <input type="text" formControlName="value" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
                    </div>
                    <div class="mb-2">
                      <label class="block mb-2 text-sm text-gray-900">Explanation</label>
                      <input type="text" formControlName="explanation" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
                    </div>
                    <div class="flex justify-between mb-5">
                      <div class="flex items-center">
                        <label class="ms-2 text-sm font-medium">
                          <input type="checkbox" formControlName="correct" class="w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-3 focus:ring-blue-300">
                          Is Correct:
                        </label>
                      </div>
                      <button type="submit" class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md text-sm bg-gray-300 cursor-pointer">Add Choice</button>
                    </div>
                  </form>
                </div>
              </div>
                
              <div class="h-3 flex justify-between items-center text-xs">
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="goPrevious()">Previous</div>
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="isAnswerShowing = !isAnswerShowing">
                  {{ isAnswerShowing ? 'Hide Answers' : 'Show Answers' }}
                </div>
                <div class="shadow-sm shadow-slate-600 px-2 py-1 rounded-md bg-gray-300 cursor-pointer" (click)="goNext()">Next</div>
              </div>
            </div>
          </div>
        </ng-container>
      </div>
    </div>
  `
})
export class McqContentBar implements OnInit {
  
  @Input(INPUT_REQUIRED) mcqs: Mcq[] = [];
  
  @Output() onUpdateMcq = new EventEmitter<UpdateMcqDTO>();

  // do we even need this? this will be handled also by onUpdateMcq
  @Output() onAddChoice = new EventEmitter<AddChoiceDTO>();
  
  isRenderAsCards = true;
  isEditMode = false;
  isAddingChoice = false;
  isEditingMcq = true;
  isAnswerShowing = false;
  currentIndex = 0;

  addChoiceForm!: FormGroup;

  actionStore: ({ type: 'add', payload: AddChoiceDTO } | { type: 'none' })[] = [];

  /**
   * current Mcq that is being updated, think of a better name
   */
  currentMcq: Mcq = this.mcqs[0];

  constructor (
    private _formBuilder: FormBuilder
  ) { }
  
  ngOnInit(): void {
    this.initializeAddChoiceForm();
  }

  loopIndexToLetters: { [key: number]: string } = {
    0: 'a',
    1: 'b',
    2: 'c',
    3: 'd',
    4: 'e',
    5: 'f'
  }

  getMcqToRender() {
    return this.isEditMode 
        ? this.currentMcq
        : this.mcqs[this.currentIndex]
  }

  willAddChoice() {
    this.isAddingChoice = true;
  }

  willEditMcq() {
    this.isEditingMcq = true;
  }

  discardChanges() {
    this.currentMcq = this.mcqs[this.currentIndex]
  }

  saveChanges() {
    // make the menu slide left and right transition
    this.isEditMode = false;
    this.isAddingChoice = false;
    this.isEditingMcq = false;

    this.actionStore.forEach(action => {
      switch (action.type) {
        case "add":
          this.onAddChoice.emit(action.payload);       
          break;
        case "none":
        default:
          console.log("nothing here")
          break;
      }
    })
  }

  addChoice() {
    // store the data first, then a final save button should call the api
    const addedChoice = this.addChoiceForm.value as McqChoice;
    this.currentMcq.choices.push(addedChoice);

    const mcqId = this.mcqs[this.currentIndex].id as number
    this.actionStore.push({
      type: "add",
      payload: { addedChoice, mcqId }
    })

    // this.onAddChoice.emit({ addedChoice, mcqId });

    // this.isAddingChoice = false;
    this.addChoiceForm.reset();
  }

  goEditMode() {
    this.isEditMode = true;

    this.currentMcq = this.mcqs[this.currentIndex];
  }

  goNext() {
    if (this.currentIndex < this.mcqs.length - 1) {
      this.currentIndex++;
      this.isAnswerShowing = false;
      // this.currentMcq = this.mcqs[this.currentIndex]
    }
  }

  goPrevious() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.isAnswerShowing = false;
      // this.currentMcq = this.mcqs[this.currentIndex]
    }
  }

  initializeAddChoiceForm() {
    this.addChoiceForm = this._formBuilder.group<McqChoice>({
      id: 0,
      correct: false,
      value: '',
      explanation: ''
    })
  }

}