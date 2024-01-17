import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Mcq, McqChoice } from './types';

@Injectable({
  providedIn: 'root'
})
export class McqService {

  baseUrl = "http://localhost:8080/mcq";

  constructor(
    private _http: HttpClient
  ) { }
    
  // generate a quiz
  generateTest(topic: string) {
    const endpoint = `${this.baseUrl}/test`;
    const test = this._http.post(endpoint, {});
  }

  getQuestions(topic: string): Observable<Mcq[]> {
    const endpoint = `${this.baseUrl}/questions/${topic}`;
    var headers = new HttpHeaders({ 
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Methods': 'GET,POST,OPTIONS,DELETE,PUT' 
    });
    const questions = this._http.get<Mcq[]>(endpoint, { headers: headers });
    return questions;
  }
  
  addChoice(mcqId: number, requestBody: McqChoice): Observable<McqChoice> {
    const endpoint = `${this.baseUrl}/question/${mcqId}/choice`;
    const response = this._http.post<McqChoice>(endpoint, requestBody);
    return response;
  }

}

