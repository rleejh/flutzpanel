import { clamp, formatScore, roundTo } from '../format.ts'
import { COMPONENT_OPTIONS } from '../options.ts'
import type { ComponentScores, ProgramComponentType } from '../types.ts'
import { SliderField } from './Fields.tsx'

type ComponentSlidersProps = {
  values: ComponentScores
  factor: number | null
  disabled?: boolean
  onChange: (type: ProgramComponentType, value: number) => void
}

export function ComponentSliders({ values, factor, disabled = false, onChange }: ComponentSlidersProps) {
  return (
    <section className="panel" aria-labelledby="components-heading">
      <div className="panel-heading">
        <h2 id="components-heading">Program Components</h2>
        <span className="count-badge">{factor == null ? 'No factor' : `Factor ${formatScore(factor)}`}</span>
      </div>
      <div className="panel-body">
        {COMPONENT_OPTIONS.map((option) => (
          <SliderField
            key={option.value}
            id={`component-${option.value.toLowerCase()}`}
            label={option.label}
            min={0}
            max={10}
            step={0.25}
            digits={2}
            value={values[option.value]}
            disabled={disabled}
            onChange={(value) => onChange(option.value, roundTo(clamp(value, 0, 10), 2))}
          />
        ))}
      </div>
    </section>
  )
}
