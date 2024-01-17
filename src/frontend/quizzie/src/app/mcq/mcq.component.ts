import { Component, OnInit } from '@angular/core';
import { McqService } from './mcq.service';
import { AddChoiceDTO, Mcq, McqChoice } from './types';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { McqMenuBar } from './mcq.menu.component';
import { McqContentBar } from './mcq.content.component';

@Component({
  selector: 'app-mcq',
  standalone: true,
  imports: [ ReactiveFormsModule, MatIconModule, CommonModule, McqMenuBar, McqContentBar ],
  template: `
   <div class="flex h-full p-5">
    <app-mcq-menu (onGetMcqs)="getMcqs()" />
    <app-mcq-content [mcqs]="mcqs" (onAddChoice)="addChoice($event)" />
    <div class="bg-gray-400 w-52 ml-4">3rd</div>
  </div>
  `,
})
export class McqComponent {
  /**
   * TODO:
   *  - previous and next button should be icon (maybe show answers too)
   *  - do not render previous and next button if there are no previous and next mcq
   */
  mcqs: Mcq[] = [];


  constructor(
    private _mcqService: McqService,
  ) { }

  addChoice(payload: AddChoiceDTO) {
    // store the data first, then a final save button should call the api

    // show saving banner for a second
    this._mcqService.addChoice(payload.mcqId, payload.addedChoice)
      .subscribe({
        next: (data: McqChoice) => {
          console.log(data); // update in the UI? or verify the value - sync the id of the McqChoice
        },
        error: (err) => {
          console.log(err); // show banner here
        },
        complete: () => {
          console.log("saved") // show banner it was saved
        }
      });
  } 

  updateMcq() {

  }

  getMcqs(){
    if (this.mcqs.length > 0) return;

    const topic = 'AWS';
    this._mcqService.getQuestions(topic)
      .subscribe({
        next: (data: Mcq[]) =>  this.mcqs = data,
        error: (err) => console.log(err),
        complete: () =>  console.log(this.mcqs)
      })
  }

}