export type ProgramType = 'SHORT_PROGRAM' | 'FREE_SKATE'

export type Discipline = 'MEN' | 'WOMEN' | 'PAIRS'

export type ProgramComponentType = 'COMPOSITION' | 'PRESENTATION' | 'SKATING_SKILLS'

export type DeductionType =
  | 'FALL'
  | 'TIME_VIOLATION'
  | 'COSTUME_VIOLATION'
  | 'ILLEGAL_ELEMENT'
  | 'OTHER'

export type ElementKind = 'JUMP_PASS' | 'SPIN' | 'STEP_SEQUENCE' | 'CHOREOGRAPHIC'

export type JumpType = 'EULER' | 'TOE' | 'SALCHOW' | 'LOOP' | 'FLIP' | 'LUTZ' | 'AXEL'

export type JumpRotation = 'CLEAN' | 'QUARTER' | 'UNDERROTATED' | 'DOWNGRADED'

export type JumpEdge = 'CORRECT' | 'UNCLEAR' | 'WRONG'

export type SpinType = 'UPRIGHT' | 'SIT' | 'CAMEL' | 'LAYBACK' | 'COMBINATION'

export type ElementLevel = 'BASIC' | 'ONE' | 'TWO' | 'THREE' | 'FOUR'

export type ChoreographicElementType = 'CHOREOGRAPHIC_SEQUENCE' | 'CHOREOGRAPHIC_SPIN'

export type Jump = {
  rotations: number
  jumpType: JumpType
  jumpRotation: JumpRotation
  jumpEdge: JumpEdge
}

export type JumpPassElement = {
  elementType: 'JUMP_PASS'
  goe: number
  bonus: boolean
  jumps: Jump[]
}

export type SpinElement = {
  elementType: 'SPIN'
  goe: number
  spinType: SpinType
  level: ElementLevel
  flying: boolean
  changeFoot: boolean
  vStatus: boolean
}

export type StepSequenceElement = {
  elementType: 'STEP_SEQUENCE'
  goe: number
  level: ElementLevel
}

export type ChoreographicElement = {
  elementType: 'CHOREOGRAPHIC'
  goe: number
  choreoType: ChoreographicElementType
}

export type ProgramElement =
  | JumpPassElement
  | SpinElement
  | StepSequenceElement
  | ChoreographicElement

export type DraftElement = {
  clientId: string
  element: ProgramElement
}

export type DraftDeduction = {
  clientId: string
  type: DeductionType
  value: number
}

export type ComponentScores = Record<ProgramComponentType, number>

export type ProgramDraft = {
  season: string
  programType: ProgramType
  discipline: Discipline
  elements: DraftElement[]
  components: ComponentScores
  deductions: DraftDeduction[]
}

export type ProgramComponentRequest = {
  type: ProgramComponentType
  score: number
}

export type DeductionRequest = {
  type: DeductionType
  value: number
}

export type ProgramRequest = {
  season: string
  programType: ProgramType
  discipline: Discipline
  elements: ProgramElement[]
  programComponents: ProgramComponentRequest[]
  deductions: DeductionRequest[]
}

export type ProgramScore = {
  technicalScore: number
  componentScore: number
  deductionsTotal: number
  totalProgramScore: number
}
