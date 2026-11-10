import type {
  ElementLevel,
  Jump,
  JumpEdge,
  JumpRotation,
  JumpType,
  ProgramElement,
  SpinType,
} from './types.ts'

const JUMP_TYPE_CODE: Record<JumpType, string> = {
  EULER: 'Eu',
  TOE: 'T',
  SALCHOW: 'S',
  LOOP: 'Lo',
  FLIP: 'F',
  LUTZ: 'Lz',
  AXEL: 'A',
}

const JUMP_ROTATION_CODE: Record<JumpRotation, string> = {
  CLEAN: '',
  QUARTER: 'q',
  UNDERROTATED: '<',
  DOWNGRADED: '<<',
}

const JUMP_EDGE_CODE: Record<JumpEdge, string> = {
  CORRECT: '',
  UNCLEAR: '!',
  WRONG: 'e',
}

const JUMP_NAME: Record<JumpType, string> = {
  EULER: 'Euler',
  TOE: 'Toe Loop',
  SALCHOW: 'Salchow',
  LOOP: 'Loop',
  FLIP: 'Flip',
  LUTZ: 'Lutz',
  AXEL: 'Axel',
}

const ROTATION_NAME: Record<number, string> = {
  0: 'Zero-rotation',
  1: 'Single',
  2: 'Double',
  3: 'Triple',
  4: 'Quadruple',
  5: 'Quintuple',
}

const SPIN_TYPE_CODE: Record<SpinType, string> = {
  UPRIGHT: 'U',
  SIT: 'S',
  CAMEL: 'C',
  LAYBACK: 'L',
  COMBINATION: 'Co',
}

const SPIN_TYPE_NAME: Record<SpinType, string> = {
  UPRIGHT: 'Upright',
  SIT: 'Sit',
  CAMEL: 'Camel',
  LAYBACK: 'Layback',
  COMBINATION: 'Combination',
}

const LEVEL_CODE: Record<ElementLevel, string> = {
  BASIC: 'B',
  ONE: '1',
  TWO: '2',
  THREE: '3',
  FOUR: '4',
}

const LEVEL_NAME: Record<ElementLevel, string> = {
  BASIC: 'Basic Level',
  ONE: 'Level 1',
  TWO: 'Level 2',
  THREE: 'Level 3',
  FOUR: 'Level 4',
}

export function jumpCode(jump: Jump): string {
  return `${jump.rotations}${JUMP_TYPE_CODE[jump.jumpType]}${JUMP_ROTATION_CODE[jump.jumpRotation]}${JUMP_EDGE_CODE[jump.jumpEdge]}`
}

export function jumpName(jump: Jump): string {
  const rotation = ROTATION_NAME[jump.rotations] ?? `${jump.rotations}-rotation`
  const calls: string[] = []

  if (jump.jumpRotation === 'QUARTER') calls.push('quarter underrotated')
  if (jump.jumpRotation === 'UNDERROTATED') calls.push('underrotated')
  if (jump.jumpRotation === 'DOWNGRADED') calls.push('downgraded')
  if (jump.jumpEdge === 'UNCLEAR') calls.push('unclear edge')
  if (jump.jumpEdge === 'WRONG') calls.push('wrong edge')

  const base = `${rotation} ${JUMP_NAME[jump.jumpType]}`
  return calls.length === 0 ? base : `${base} (${calls.join(', ')})`
}

export function elementCode(element: ProgramElement): string {
  switch (element.elementType) {
    case 'JUMP_PASS':
      return element.jumps.map(jumpCode).join('+')
    case 'SPIN':
      return `${element.flying ? 'F' : ''}${element.changeFoot ? 'C' : ''}${SPIN_TYPE_CODE[element.spinType]}Sp${LEVEL_CODE[element.level]}${element.vStatus ? 'V' : ''}`
    case 'STEP_SEQUENCE':
      return `StSq${LEVEL_CODE[element.level]}`
    case 'CHOREOGRAPHIC':
      return 'ChSq1'
  }
}

export function elementName(element: ProgramElement): string {
  switch (element.elementType) {
    case 'JUMP_PASS': {
      if (element.jumps.length === 0) return 'Jump pass'
      const jumps = element.jumps.map(jumpName).join(' + ')
      return element.bonus ? `${jumps} · bonus` : jumps
    }
    case 'SPIN': {
      const prefixes = [
        element.flying ? 'Flying' : '',
        element.changeFoot ? 'Change Foot' : '',
        SPIN_TYPE_NAME[element.spinType],
        'Spin',
        LEVEL_NAME[element.level],
      ].filter(Boolean)
      return element.vStatus ? `${prefixes.join(' ')} V` : prefixes.join(' ')
    }
    case 'STEP_SEQUENCE':
      return `Step Sequence ${LEVEL_NAME[element.level]}`
    case 'CHOREOGRAPHIC':
      return element.choreoType === 'CHOREOGRAPHIC_SPIN'
        ? 'Choreographic Spin'
        : 'Choreographic Sequence'
  }
}
