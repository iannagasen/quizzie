export interface McqChoice {
  id: number;
  value: string;
  explanation: string;
  correct: boolean;
}

export interface Mcq {
  id?: number;
  topic: string;
  question: string;
  choices: McqChoice[];
}

export interface UpdateMcqDTO {
  mcq: Mcq
}

export interface AddChoiceDTO {
  addedChoice: McqChoice,
  mcqId: number
}