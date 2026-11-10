import { clamp, formatSigned, roundTo } from '../format.ts'

const WHOLE_GOE_VALUES = [-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5]

type GoeControlProps = {
  id?: string
  value: number
  disabled?: boolean
  onChange: (value: number) => void
}

export function GoeControl({ id = 'element-goe', value, disabled = false, onChange }: GoeControlProps) {
  const rounded = roundTo(clamp(value, -5, 5), 2)

  function update(next: number) {
    onChange(roundTo(clamp(next, -5, 5), 2))
  }

  return (
    <div className="goe-control">
      <div className="slider-header">
        <label htmlFor={id}>Grade of execution</label>
        <output htmlFor={id}>{formatSigned(rounded)}</output>
      </div>
      <input
        id={id}
        type="range"
        min={-5}
        max={5}
        step={0.01}
        value={rounded}
        disabled={disabled}
        aria-valuemin={-5}
        aria-valuemax={5}
        aria-valuenow={rounded}
        aria-valuetext={formatSigned(rounded)}
        onChange={(event) => update(Number(event.target.value))}
      />
      <div className="goe-buttons" role="group" aria-label="Whole-number grade of execution">
        {WHOLE_GOE_VALUES.map((goe) => (
          <button
            key={goe}
            type="button"
            aria-pressed={rounded === goe}
            disabled={disabled}
            onClick={() => update(goe)}
          >
            {formatSigned(goe, 0)}
          </button>
        ))}
      </div>
    </div>
  )
}
