export interface McqChoice {
  id: number;
  value: string;
  explanation: string;
  correct: boolean;
}

export interface Mcq {
  id: number;
  topic: string;
  question: string;
  choices: McqChoice[];
}