import { DEDUCTION_TYPE_OPTIONS } from '../options.ts'
import type { DeductionType, DraftDeduction } from '../types.ts'
import { LabeledSelect } from './Fields.tsx'

type DeductionEditorProps = {
  deductions: DraftDeduction[]
  disabled?: boolean
  onChange: (deductions: DraftDeduction[]) => void
}

export function DeductionEditor({ deductions, disabled = false, onChange }: DeductionEditorProps) {
  function addDeduction() {
    onChange([
      ...deductions,
      { clientId: crypto.randomUUID(), type: 'FALL', value: 1 },
    ])
  }

  function updateDeduction(clientId: string, patch: Partial<Pick<DraftDeduction, 'type' | 'value'>>) {
    onChange(
      deductions.map((deduction) =>
        deduction.clientId === clientId ? { ...deduction, ...patch } : deduction,
      ),
    )
  }

  function removeDeduction(clientId: string) {
    onChange(deductions.filter((deduction) => deduction.clientId !== clientId))
  }

  return (
    <section className="panel" aria-labelledby="deductions-heading">
      <div className="panel-heading">
        <h2 id="deductions-heading">Deductions</h2>
        <button className="secondary-button" type="button" onClick={addDeduction} disabled={disabled}>
          ＋ Add
        </button>
      </div>
      <div className="panel-body">
        {deductions.length === 0 ? (
          <p className="field-note">No deductions.</p>
        ) : (
          <ul className="deduction-list">
            {deductions.map((deduction, index) => {
              const typeId = `deduction-type-${deduction.clientId}`
              const valueId = `deduction-value-${deduction.clientId}`
              const invalid = !Number.isInteger(deduction.value) || deduction.value <= 0

              return (
                <li className="deduction-row" key={deduction.clientId}>
                  <LabeledSelect
                    id={typeId}
                    label={`Deduction ${index + 1} type`}
                    value={deduction.type}
                    options={DEDUCTION_TYPE_OPTIONS}
                    disabled={disabled}
                    onChange={(type: DeductionType) => updateDeduction(deduction.clientId, { type })}
                  />
                  <div className="field">
                    <label htmlFor={valueId}>Value</label>
                    <input
                      id={valueId}
                      type="number"
                      min={1}
                      step={1}
                      inputMode="numeric"
                      value={deduction.value}
                      disabled={disabled}
                      aria-invalid={invalid}
                      aria-describedby={invalid ? `${valueId}-error` : undefined}
                      onChange={(event) => {
                        const next = Number(event.target.value)
                        updateDeduction(deduction.clientId, {
                          value: Number.isFinite(next) ? Math.trunc(next) : deduction.value,
                        })
                      }}
                    />
                    {invalid && (
                      <p className="field-error" id={`${valueId}-error`}>
                        Enter a positive whole number.
                      </p>
                    )}
                  </div>
                  <button
                    className="secondary-button remove-deduction"
                    type="button"
                    disabled={disabled}
                    onClick={() => removeDeduction(deduction.clientId)}
                  >
                    Remove
                  </button>
                </li>
              )
            })}
          </ul>
        )}
      </div>
    </section>
  )
}
