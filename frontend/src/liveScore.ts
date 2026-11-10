import { Decimal } from './decimal.ts'
import { elementCode, jumpCode } from './elementDisplay.ts'
import factorsFile from './data/program-component-factors.json' with { type: 'json' }
import scaleOfValues from './data/scale-of-values.json' with { type: 'json' }
import type { ComponentScores, DeductionType, Discipline, ProgramElement, ProgramType } from './types.ts'

const BONUS_MULTIPLIER = Decimal.parse('1.1')
const COMPONENT_ORDER = ['COMPOSITION', 'PRESENTATION', 'SKATING_SKILLS'] as const

type ScaleEntry = {
  id: string
  baseValue: number
  goe: Record<string, number>
}

type ScaleFile = {
  jumps: ScaleEntry[]
  spins: ScaleEntry[]
  stepSequences: ScaleEntry[]
  choreographicElements: ScaleEntry[]
}

type FactorEntry = {
  season: string
  discipline: Discipline
  programType: ProgramType
  factor: number
}

type TableEntry = {
  baseValue: Decimal
  goe: Map<number, Decimal>
}

export type ElementBreakdown = {
  baseValue: number
  goeContribution: number
  total: number
}

export type ElementScoreResult =
  | { ok: true; score: ElementBreakdown }
  | { ok: false; message: string }

export type LiveProgramScore = {
  technicalScore: number | null
  componentScore: number | null
  deductionsTotal: number
  totalProgramScore: number | null
  factor: number | null
  error: string | null
}

const tables = indexScale(scaleOfValues as ScaleFile)
const factors = factorsFile as FactorEntry[]

export function scoreElement(element: ProgramElement): ElementScoreResult {
  const scored = scoreElementExact(element)
  if (!scored.ok) return scored
  return {
    ok: true,
    score: {
      baseValue: scored.baseValue.toScore(),
      goeContribution: scored.goeContribution.toScore(),
      total: scored.total.toScore(),
    },
  }
}

export function findComponentFactor(
  season: string,
  programType: ProgramType,
  discipline: Discipline,
): number | null {
  const match = factors.find(
    (factor) =>
      factor.season === season.trim() &&
      factor.programType === programType &&
      factor.discipline === discipline,
  )
  return match ? Decimal.fromNumber(match.factor).toScore() : null
}

export function scoreProgram(input: {
  season: string
  programType: ProgramType
  discipline: Discipline
  elements: ProgramElement[]
  components: ComponentScores
  deductions: Array<{ type: DeductionType; value: number }>
}): LiveProgramScore {
  const errors: string[] = []
  let technical = Decimal.zero()
  let technicalOk = true

  for (const element of input.elements) {
    const scored = scoreElementExact(element)
    if (!scored.ok) {
      technicalOk = false
      errors.push(scored.message)
    } else {
      technical = technical.add(scored.total)
    }
  }

  const factor = findComponentFactor(input.season, input.programType, input.discipline)
  let component = Decimal.zero()
  if (factor == null) {
    errors.push('No component factor for this season, discipline, and program.')
  } else {
    const factorValue = Decimal.fromNumber(factor)
    for (const type of COMPONENT_ORDER) {
      component = component.add(Decimal.fromNumber(input.components[type]).multiply(factorValue).setScale(2))
    }
  }

  let deductions = Decimal.zero()
  for (const deduction of input.deductions) {
    if (Number.isInteger(deduction.value) && deduction.value > 0) {
      deductions = deductions.add(Decimal.parse(String(deduction.value)))
    }
  }

  const technicalScore = technicalOk ? technical.toScore() : null
  const componentScore = factor == null ? null : component.toScore()
  const deductionsTotal = deductions.toScore()
  const totalProgramScore =
    technicalOk && factor != null
      ? technical.add(component).add(deductions.multiply(Decimal.parse('-1'))).toScore()
      : null

  return {
    technicalScore,
    componentScore,
    deductionsTotal,
    totalProgramScore,
    factor,
    error: errors.length > 0 ? errors[0] : null,
  }
}

type ExactScore = {
  ok: true
  baseValue: Decimal
  goeContribution: Decimal
  total: Decimal
}

function scoreElementExact(element: ProgramElement): ExactScore | { ok: false; message: string } {
  if (element.elementType === 'JUMP_PASS') {
    return scoreJumpPass(element)
  }

  const code = elementCode(element)
  const entry = findEntry(code)
  if (!entry) return { ok: false, message: `${code || 'This element'} is not in the scale of values.` }

  const goeContribution = goeValue(entry, Decimal.fromNumber(element.goe))
  if (!goeContribution) return { ok: false, message: `No GOE value for ${code}.` }

  return {
    ok: true,
    baseValue: entry.baseValue,
    goeContribution,
    total: entry.baseValue.add(goeContribution),
  }
}

function scoreJumpPass(
  element: Extract<ProgramElement, { elementType: 'JUMP_PASS' }>,
): ExactScore | { ok: false; message: string } {
  if (element.jumps.length === 0) {
    return { ok: false, message: 'Add at least one jump.' }
  }

  let baseValue = Decimal.zero()
  let highest: TableEntry | null = null

  for (const jump of element.jumps) {
    const code = jumpCode(jump)
    const entry = findEntry(code)
    if (!entry) return { ok: false, message: `${code} is not in the scale of values.` }

    let jumpValue = entry.baseValue
    if (element.bonus) jumpValue = jumpValue.multiply(BONUS_MULTIPLIER)
    baseValue = baseValue.add(jumpValue)

    if (highest == null || entry.baseValue.compare(highest.baseValue) > 0) {
      highest = entry
    }
  }

  const goeContribution = goeValue(highest as TableEntry, Decimal.fromNumber(element.goe))
  const code = elementCode(element)
  if (!goeContribution) return { ok: false, message: `No GOE value for ${code}.` }

  return {
    ok: true,
    baseValue,
    goeContribution,
    total: baseValue.add(goeContribution),
  }
}

function goeValue(entry: TableEntry, goe: Decimal): Decimal | null {
  let contribution: Decimal | undefined
  if (goe.isInteger()) {
    contribution = entry.goe.get(goe.intValue())
  } else if (goe.compare(Decimal.zero()) > 0) {
    const plusOne = entry.goe.get(1)
    contribution = plusOne?.multiply(goe)
  } else {
    const minusOne = entry.goe.get(-1)
    contribution = minusOne?.abs().multiply(goe)
  }

  return contribution ? contribution.setScale(2) : null
}

function findEntry(code: string): TableEntry | undefined {
  return tables.get(code)
}

function indexScale(scale: ScaleFile): Map<string, TableEntry> {
  const indexed = new Map<string, TableEntry>()
  for (const list of [scale.jumps, scale.spins, scale.stepSequences, scale.choreographicElements]) {
    for (const entry of list) {
      if (indexed.has(entry.id)) continue
      indexed.set(entry.id, {
        baseValue: Decimal.fromNumber(entry.baseValue),
        goe: new Map(Object.entries(entry.goe).map(([mark, value]) => [Number(mark), Decimal.fromNumber(value)])),
      })
    }
  }
  return indexed
}
