import { isSelectValue, type SelectOption } from '../options.ts'

type LabeledSelectProps<T extends string> = {
  id: string
  label: string
  value: T
  options: readonly SelectOption<T>[]
  onChange: (value: T) => void
  disabled?: boolean
}

export function LabeledSelect<T extends string>({
  id,
  label,
  value,
  options,
  onChange,
  disabled = false,
}: LabeledSelectProps<T>) {
  return (
    <div className="field">
      <label htmlFor={id}>{label}</label>
      <select
        id={id}
        value={value}
        disabled={disabled}
        onChange={(event) => {
          const next = event.target.value
          if (isSelectValue(next, options)) onChange(next)
        }}
      >
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </div>
  )
}

type SliderFieldProps = {
  id: string
  label: string
  min: number
  max: number
  step: number
  value: number
  digits?: number
  disabled?: boolean
  onChange: (value: number) => void
}

export function SliderField({
  id,
  label,
  min,
  max,
  step,
  value,
  digits = 2,
  disabled = false,
  onChange,
}: SliderFieldProps) {
  const display = value.toFixed(digits)

  return (
    <div className="slider-field">
      <div className="slider-header">
        <label htmlFor={id}>{label}</label>
        <output htmlFor={id}>{display}</output>
      </div>
      <input
        id={id}
        type="range"
        min={min}
        max={max}
        step={step}
        value={value}
        disabled={disabled}
        aria-valuemin={min}
        aria-valuemax={max}
        aria-valuenow={Number(display)}
        aria-valuetext={display}
        onChange={(event) => onChange(Number(event.target.value))}
      />
    </div>
  )
}

type CheckboxFieldProps = {
  id: string
  label: string
  checked: boolean
  disabled?: boolean
  onChange: (checked: boolean) => void
}

export function CheckboxField({ id, label, checked, disabled = false, onChange }: CheckboxFieldProps) {
  return (
    <label className="check-row" htmlFor={id}>
      <input
        id={id}
        type="checkbox"
        checked={checked}
        disabled={disabled}
        onChange={(event) => onChange(event.target.checked)}
      />
      <span>{label}</span>
    </label>
  )
}
