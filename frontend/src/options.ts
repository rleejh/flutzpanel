import type {
  ChoreographicElementType,
  DeductionType,
  Discipline,
  ElementKind,
  ElementLevel,
  JumpEdge,
  JumpRotation,
  JumpType,
  ProgramComponentType,
  ProgramType,
  SpinType,
} from './types.ts'

export type SelectOption<T extends string> = {
  value: T
  label: string
}

export const PROGRAM_TYPE_OPTIONS = [
  { value: 'SHORT_PROGRAM', label: 'Short Program' },
  { value: 'FREE_SKATE', label: 'Free Skate' },
] as const satisfies readonly SelectOption<ProgramType>[]

export const DISCIPLINE_OPTIONS = [
  { value: 'MEN', label: 'Men' },
  { value: 'WOMEN', label: 'Women' },
  { value: 'PAIRS', label: 'Pairs' },
] as const satisfies readonly SelectOption<Discipline>[]

export const ELEMENT_KIND_OPTIONS = [
  { value: 'JUMP_PASS', label: 'Jump Pass' },
  { value: 'SPIN', label: 'Spin' },
  { value: 'STEP_SEQUENCE', label: 'Step Sequence' },
  { value: 'CHOREOGRAPHIC', label: 'Choreographic Element' },
] as const satisfies readonly SelectOption<ElementKind>[]

export const JUMP_TYPE_OPTIONS = [
  { value: 'EULER', label: 'Euler' },
  { value: 'TOE', label: 'Toe Loop' },
  { value: 'SALCHOW', label: 'Salchow' },
  { value: 'LOOP', label: 'Loop' },
  { value: 'FLIP', label: 'Flip' },
  { value: 'LUTZ', label: 'Lutz' },
  { value: 'AXEL', label: 'Axel' },
] as const satisfies readonly SelectOption<JumpType>[]

export const JUMP_ROTATION_OPTIONS = [
  { value: 'CLEAN', label: 'Clean' },
  { value: 'QUARTER', label: 'Quarter (q)' },
  { value: 'UNDERROTATED', label: 'Underrotated (<)' },
  { value: 'DOWNGRADED', label: 'Downgraded (<<)' },
] as const satisfies readonly SelectOption<JumpRotation>[]

export const JUMP_EDGE_OPTIONS = [
  { value: 'CORRECT', label: 'Correct edge' },
  { value: 'UNCLEAR', label: 'Unclear edge (!)' },
  { value: 'WRONG', label: 'Wrong edge (e)' },
] as const satisfies readonly SelectOption<JumpEdge>[]

export const SPIN_TYPE_OPTIONS = [
  { value: 'UPRIGHT', label: 'Upright' },
  { value: 'SIT', label: 'Sit' },
  { value: 'CAMEL', label: 'Camel' },
  { value: 'LAYBACK', label: 'Layback' },
  { value: 'COMBINATION', label: 'Combination' },
] as const satisfies readonly SelectOption<SpinType>[]

export const LEVEL_OPTIONS = [
  { value: 'BASIC', label: 'Basic (B)' },
  { value: 'ONE', label: 'Level 1' },
  { value: 'TWO', label: 'Level 2' },
  { value: 'THREE', label: 'Level 3' },
  { value: 'FOUR', label: 'Level 4' },
] as const satisfies readonly SelectOption<ElementLevel>[]

export const CHOREO_TYPE_OPTIONS = [
  { value: 'CHOREOGRAPHIC_SEQUENCE', label: 'Choreographic Sequence' },
  { value: 'CHOREOGRAPHIC_SPIN', label: 'Choreographic Spin' },
] as const satisfies readonly SelectOption<ChoreographicElementType>[]

export const COMPONENT_OPTIONS = [
  { value: 'COMPOSITION', label: 'Composition' },
  { value: 'PRESENTATION', label: 'Presentation' },
  { value: 'SKATING_SKILLS', label: 'Skating Skills' },
] as const satisfies readonly SelectOption<ProgramComponentType>[]

export const DEDUCTION_TYPE_OPTIONS = [
  { value: 'FALL', label: 'Fall' },
  { value: 'TIME_VIOLATION', label: 'Time violation' },
  { value: 'COSTUME_VIOLATION', label: 'Costume violation' },
  { value: 'ILLEGAL_ELEMENT', label: 'Illegal element' },
  { value: 'OTHER', label: 'Other' },
] as const satisfies readonly SelectOption<DeductionType>[]

export function isSelectValue<T extends string>(
  value: string,
  options: readonly SelectOption<T>[],
): value is T {
  return options.some((option) => option.value === value)
}
