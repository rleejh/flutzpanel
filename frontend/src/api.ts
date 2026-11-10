import type {
  ComponentScores,
  DeductionType,
  Discipline,
  ElementLevel,
  Jump,
  ProgramElement,
  ProgramRequest,
  ProgramScore,
  ProgramType,
} from './types.ts'

export type LoadedProgram = {
  id: number
  season: string
  programType: ProgramType
  discipline: Discipline
  elements: ProgramElement[]
  components: ComponentScores
  deductions: Array<{ type: DeductionType; value: number }>
}

// The Vite dev and preview servers proxy /api to http://localhost:8080.
const API_ROOT = '/api'

export async function createProgram(program: ProgramRequest): Promise<number> {
  const payload = await send('/programs', {
    method: 'POST',
    body: JSON.stringify(program),
  })
  return readProgramId(payload)
}

export async function updateProgram(id: number, program: ProgramRequest): Promise<number> {
  const payload = await send(`/programs/${id}`, {
    method: 'PUT',
    body: JSON.stringify(program),
  })
  return readProgramId(payload)
}

export async function fetchProgram(id: number): Promise<LoadedProgram> {
  const payload = await send(`/programs/${id}`, { method: 'GET' }, loadFailure)
  return readLoadedProgram(payload)
}

export async function fetchProgramScore(id: number): Promise<ProgramScore> {
  const payload = await send(`/programs/${id}/score`, { method: 'GET' }, scoreFailure)
  return readProgramScore(payload)
}

async function send(
  path: string,
  init: RequestInit,
  describeFailure: (status: number, body: string) => string = saveFailure,
): Promise<unknown> {
  let response: Response
  try {
    response = await fetch(`${API_ROOT}${path}`, {
      ...init,
      headers: {
        Accept: 'application/json',
        ...(init.body ? { 'Content-Type': 'application/json' } : {}),
      },
    })
  } catch {
    throw new Error(
      'Could not reach the API at http://localhost:8080. Start the Spring Boot backend and try again.',
    )
  }

  const body = await response.text()
  if (!response.ok) {
    throw new Error(describeFailure(response.status, body))
  }

  if (!body) return null

  try {
    return JSON.parse(body) as unknown
  } catch {
    throw new Error('The API returned a response that was not valid JSON.')
  }
}

function loadFailure(status: number, body: string): string {
  const detail = readErrorDetail(body)
  if (detail) return detail
  if (status === 404) return 'That saved program could not be found.'
  return `The program could not be loaded (HTTP ${status}).`
}

function saveFailure(status: number, body: string): string {
  const detail = readErrorDetail(body)
  if (detail) return detail
  if (status === 404) return 'That saved program could not be found.'
  return `The program could not be saved (HTTP ${status}).`
}

function scoreFailure(status: number, body: string): string {
  const detail = readErrorDetail(body)
  if (detail) return `Scoring failed: ${detail}`
  if (status === 404) return 'Scoring failed because that saved program could not be found.'
  if (status >= 500) {
    return 'Scoring failed. The backend could not score this saved program. Check that the season is 2026-27 and that each element is recognized by the scale of values.'
  }
  return `Scoring failed (HTTP ${status}).`
}

function readErrorDetail(body: string): string | null {
  if (!body.trim()) return null
  try {
    const parsed = JSON.parse(body) as { message?: unknown; detail?: unknown; error?: unknown }
    const detail = [parsed.detail, parsed.message].find((value) => typeof value === 'string' && value.trim())
    return typeof detail === 'string' ? detail : null
  } catch {
    return body.trim()
  }
}

function readProgramId(payload: unknown): number {
  if (isRecord(payload) && typeof payload.id === 'number') return payload.id
  throw new Error('The server response did not include a program id.')
}

function readProgramScore(payload: unknown): ProgramScore {
  if (!isRecord(payload)) {
    throw new Error('Scoring failed because the score response was empty.')
  }

  return {
    technicalScore: readScoreNumber(payload.technicalScore, 'technical score'),
    componentScore: readScoreNumber(payload.componentScore, 'component score'),
    deductionsTotal: readScoreNumber(payload.deductionsTotal, 'deductions total'),
    totalProgramScore: readScoreNumber(payload.totalProgramScore, 'total score'),
  }
}

function readScoreNumber(value: unknown, label: string): number {
  const numeric = typeof value === 'number' ? value : typeof value === 'string' ? Number(value) : Number.NaN
  if (!Number.isFinite(numeric)) {
    throw new Error(`Scoring failed because the ${label} was missing.`)
  }
  return numeric
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null
}

function readLoadedProgram(payload: unknown): LoadedProgram {
  if (!isRecord(payload) || typeof payload.id !== 'number') {
    throw new Error('The server response did not include a program id.')
  }

  const components: ComponentScores = {
    COMPOSITION: 0,
    PRESENTATION: 0,
    SKATING_SKILLS: 0,
  }
  if (Array.isArray(payload.programComponents)) {
    for (const component of payload.programComponents) {
      if (!isRecord(component) || !isComponentType(component.type)) continue
      components[component.type] = readScoreNumber(component.score, 'component score')
    }
  }

  return {
    id: payload.id,
    season: typeof payload.season === 'string' ? payload.season : '',
    programType: readEnum(payload.programType, ['SHORT_PROGRAM', 'FREE_SKATE'] as const, 'program type'),
    discipline: readEnum(payload.discipline, ['MEN', 'WOMEN', 'PAIRS'] as const, 'discipline'),
    elements: Array.isArray(payload.elements) ? payload.elements.map(readElement) : [],
    components,
    deductions: Array.isArray(payload.deductions) ? payload.deductions.map(readDeduction) : [],
  }
}

function readElement(value: unknown): ProgramElement {
  if (!isRecord(value)) throw new Error('A saved element could not be read.')
  const goe = readScoreNumber(value.goe, 'element GOE')

  switch (value.elementType) {
    case 'JUMP_PASS':
      return {
        elementType: 'JUMP_PASS',
        goe,
        bonus: value.bonus === true,
        jumps: Array.isArray(value.jumps) ? value.jumps.map(readJump) : [],
      }
    case 'SPIN':
      return {
        elementType: 'SPIN',
        goe,
        spinType: readEnum(
          value.spinType,
          ['UPRIGHT', 'SIT', 'CAMEL', 'LAYBACK', 'COMBINATION'] as const,
          'spin type',
        ),
        level: readLevel(value.level),
        flying: value.flying === true,
        changeFoot: value.changeFoot === true,
        vStatus: value.vStatus === true,
      }
    case 'STEP_SEQUENCE':
      return {
        elementType: 'STEP_SEQUENCE',
        goe,
        level: readLevel(value.level),
      }
    case 'CHOREOGRAPHIC':
      return {
        elementType: 'CHOREOGRAPHIC',
        goe,
        choreoType: readEnum(
          value.choreoType,
          ['CHOREOGRAPHIC_SEQUENCE', 'CHOREOGRAPHIC_SPIN'] as const,
          'choreographic type',
        ),
      }
    default:
      throw new Error('A saved element has an unknown type.')
  }
}

function readJump(value: unknown): Jump {
  if (!isRecord(value) || typeof value.rotations !== 'number') {
    throw new Error('A saved jump could not be read.')
  }
  return {
    rotations: value.rotations,
    jumpType: readEnum(
      value.jumpType,
      ['EULER', 'TOE', 'SALCHOW', 'LOOP', 'FLIP', 'LUTZ', 'AXEL'] as const,
      'jump type',
    ),
    jumpRotation: readEnum(
      value.jumpRotation,
      ['CLEAN', 'QUARTER', 'UNDERROTATED', 'DOWNGRADED'] as const,
      'jump rotation',
    ),
    jumpEdge: readEnum(value.jumpEdge, ['CORRECT', 'UNCLEAR', 'WRONG'] as const, 'jump edge'),
  }
}

function readDeduction(value: unknown): { type: DeductionType; value: number } {
  if (!isRecord(value)) throw new Error('A saved deduction could not be read.')
  const amount = readScoreNumber(value.value, 'deduction value')
  return {
    type: readEnum(
      value.type,
      ['FALL', 'TIME_VIOLATION', 'COSTUME_VIOLATION', 'ILLEGAL_ELEMENT', 'OTHER'] as const,
      'deduction type',
    ),
    value: amount,
  }
}

function readLevel(value: unknown): ElementLevel {
  return readEnum(value, ['BASIC', 'ONE', 'TWO', 'THREE', 'FOUR'] as const, 'level')
}

function readEnum<T extends string>(value: unknown, options: readonly T[], label: string): T {
  if (typeof value === 'string' && options.some((option) => option === value)) return value as T
  throw new Error(`A saved ${label} could not be read.`)
}

function isComponentType(value: unknown): value is keyof ComponentScores {
  return value === 'COMPOSITION' || value === 'PRESENTATION' || value === 'SKATING_SKILLS'
}
