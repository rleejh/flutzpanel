import { roundTo } from './format.ts'
import type { ProgramDraft, ProgramElement, ProgramRequest } from './types.ts'

const COMPONENT_ORDER = ['COMPOSITION', 'PRESENTATION', 'SKATING_SKILLS'] as const

export function toProgramRequest(draft: ProgramDraft): ProgramRequest {
  return {
    season: draft.season.trim(),
    programType: draft.programType,
    discipline: draft.discipline,
    elements: draft.elements.map((item) => withRoundedGoe(item.element)),
    programComponents: COMPONENT_ORDER.map((type) => ({
      type,
      score: roundTo(draft.components[type], 2),
    })),
    deductions: draft.deductions.map((item) => ({
      type: item.type,
      value: item.value,
    })),
  }
}

function withRoundedGoe(element: ProgramElement): ProgramElement {
  const rounded = { ...element, goe: roundTo(element.goe, 2) }
  if (rounded.elementType !== 'JUMP_PASS') return rounded
  return {
    ...rounded,
    jumps: rounded.jumps.map((jump) => ({
      ...jump,
      jumpEdge: jump.jumpType === 'FLIP' || jump.jumpType === 'LUTZ' ? jump.jumpEdge : 'CORRECT',
    })),
  }
}

export function validateProgram(request: ProgramRequest): string | null {
  if (!request.season) {
    return 'Enter a season before saving.'
  }

  if (request.elements.some((element) => element.elementType === 'JUMP_PASS' && element.jumps.length === 0)) {
    return 'Each jump pass needs at least one jump.'
  }

  if (request.elements.some((element) => element.goe < -5 || element.goe > 5)) {
    return 'GOE must stay between -5.00 and +5.00.'
  }

  if (request.programComponents.some((component) => component.score < 0 || component.score > 10)) {
    return 'Program component scores must stay between 0.00 and 10.00.'
  }

  if (request.deductions.some((deduction) => !Number.isInteger(deduction.value) || deduction.value <= 0)) {
    return 'Each deduction needs a positive whole-number value.'
  }

  return null
}
