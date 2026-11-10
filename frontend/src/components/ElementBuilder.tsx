import { useState } from 'react'
import { elementCode, elementName } from '../elementDisplay.ts'
import { clamp, roundTo } from '../format.ts'
import {
  CHOREO_TYPE_OPTIONS,
  ELEMENT_KIND_OPTIONS,
  JUMP_EDGE_OPTIONS,
  JUMP_ROTATION_OPTIONS,
  JUMP_TYPE_OPTIONS,
  LEVEL_OPTIONS,
  SPIN_TYPE_OPTIONS,
} from '../options.ts'
import type {
  ElementKind,
  Jump,
  JumpPassElement,
  ProgramElement,
  SpinElement,
} from '../types.ts'
import { CheckboxField, LabeledSelect } from './Fields.tsx'
import { ElementMetrics } from './ElementList.tsx'
import { GoeControl } from './GoeControl.tsx'

type ElementBuilderProps = {
  disabled?: boolean
  onAdd: (element: ProgramElement) => void
}

export function ElementBuilder({ disabled = false, onAdd }: ElementBuilderProps) {
  const [draft, setDraft] = useState<ProgramElement>(() => createElement('JUMP_PASS', 0))

  function changeKind(kind: ElementKind) {
    setDraft((current) => createElement(kind, current.goe))
  }

  function changeGoe(goe: number) {
    setDraft((current) => ({ ...current, goe }))
  }

  const code = elementCode(draft)
  const name = elementName(draft)
  const canAdd = draft.elementType !== 'JUMP_PASS' || draft.jumps.length > 0

  return (
    <section className="panel" aria-labelledby="builder-heading">
      <div className="panel-heading">
        <h2 id="builder-heading">Add an element</h2>
        <span className="plus-mark" aria-hidden="true">
          ＋
        </span>
      </div>

      <form
        className="builder-form"
        onSubmit={(event) => {
          event.preventDefault()
          if (!canAdd || disabled) return
          onAdd({ ...draft, goe: roundTo(draft.goe, 2) })
        }}
      >
        <LabeledSelect
          id="element-kind"
          label="Element type"
          value={draft.elementType}
          options={ELEMENT_KIND_OPTIONS}
          disabled={disabled}
          onChange={changeKind}
        />

        {draft.elementType === 'JUMP_PASS' && (
          <JumpPassFields
            element={draft}
            disabled={disabled}
            onChange={(element) => setDraft(element)}
          />
        )}

        {draft.elementType === 'SPIN' && (
          <SpinFields element={draft} disabled={disabled} onChange={(element) => setDraft(element)} />
        )}

        {draft.elementType === 'STEP_SEQUENCE' && (
          <LabeledSelect
            id="step-level"
            label="Level"
            value={draft.level}
            options={LEVEL_OPTIONS}
            disabled={disabled}
            onChange={(level) => setDraft({ ...draft, level })}
          />
        )}

        {draft.elementType === 'CHOREOGRAPHIC' && (
          <>
            <LabeledSelect
              id="choreo-type"
              label="Choreographic type"
              value={draft.choreoType}
              options={CHOREO_TYPE_OPTIONS}
              disabled={disabled}
              onChange={(choreoType) => setDraft({ ...draft, choreoType })}
            />
            <p className="field-note">
              The backend stores the selected type and currently reports ChSq1 as the code for both
              choreographic elements.
            </p>
          </>
        )}

        <div className="generated-preview" aria-live="polite">
          <span className="field-label">GENERATED ELEMENT</span>
          <strong>{code || 'Add a jump'}</strong>
          <span>{name}</span>
        </div>

        <GoeControl value={draft.goe} disabled={disabled} onChange={changeGoe} />
        <ElementMetrics element={draft} />

        <button className="add-button" type="submit" disabled={disabled || !canAdd}>
          <span aria-hidden="true">＋</span> Add to program
        </button>
      </form>
    </section>
  )
}

function createElement(kind: ElementKind, goe: number): ProgramElement {
  switch (kind) {
    case 'JUMP_PASS':
      return { elementType: 'JUMP_PASS', goe, bonus: false, jumps: [createJump(3)] }
    case 'SPIN':
      return {
        elementType: 'SPIN',
        goe,
        spinType: 'UPRIGHT',
        level: 'ONE',
        flying: false,
        changeFoot: false,
        vStatus: false,
      }
    case 'STEP_SEQUENCE':
      return { elementType: 'STEP_SEQUENCE', goe, level: 'ONE' }
    case 'CHOREOGRAPHIC':
      return { elementType: 'CHOREOGRAPHIC', goe, choreoType: 'CHOREOGRAPHIC_SEQUENCE' }
  }
}

function createJump(rotations: number): Jump {
  return {
    rotations,
    jumpType: 'TOE',
    jumpRotation: 'CLEAN',
    jumpEdge: 'CORRECT',
  }
}

function JumpPassFields({
  element,
  disabled,
  onChange,
}: {
  element: JumpPassElement
  disabled: boolean
  onChange: (element: JumpPassElement) => void
}) {
  function updateJump(index: number, jump: Jump) {
    onChange({
      ...element,
      jumps: element.jumps.map((current, jumpIndex) => (jumpIndex === index ? jump : current)),
    })
  }

  return (
    <div className="type-fields">
      {element.jumps.map((jump, index) => (
        <JumpFields
          key={index}
          index={index}
          jump={jump}
          disabled={disabled}
          canRemove={element.jumps.length > 1}
          onChange={(next) => updateJump(index, next)}
          onRemove={() =>
            onChange({ ...element, jumps: element.jumps.filter((_, jumpIndex) => jumpIndex !== index) })
          }
        />
      ))}
      <button
        className="secondary-button"
        type="button"
        disabled={disabled}
        onClick={() => onChange({ ...element, jumps: [...element.jumps, createJump(2)] })}
      >
        ＋ Add jump to this pass
      </button>
      <CheckboxField
        id="jump-bonus"
        label="Bonus (base value × 1.1)"
        checked={element.bonus}
        disabled={disabled}
        onChange={(bonus) => onChange({ ...element, bonus })}
      />
    </div>
  )
}

function JumpFields({
  index,
  jump,
  disabled,
  canRemove,
  onChange,
  onRemove,
}: {
  index: number
  jump: Jump
  disabled: boolean
  canRemove: boolean
  onChange: (jump: Jump) => void
  onRemove: () => void
}) {
  const prefix = `jump-${index}`

  return (
    <fieldset className="jump-card">
      <legend>Jump {index + 1}</legend>
      <button className="text-button jump-remove" type="button" onClick={onRemove} disabled={disabled || !canRemove}>
        Remove jump
      </button>
      <div className="field-grid">
        <div className="field">
          <label htmlFor={`${prefix}-rotations`}>Rotations</label>
          <input
            id={`${prefix}-rotations`}
            type="number"
            min={0}
            max={5}
            step={1}
            value={jump.rotations}
            disabled={disabled}
            onChange={(event) =>
              onChange({
                ...jump,
                rotations: roundTo(clamp(Number(event.target.value), 0, 5), 0),
              })
            }
          />
        </div>
        <LabeledSelect
          id={`${prefix}-type`}
          label="Jump type"
          value={jump.jumpType}
          options={JUMP_TYPE_OPTIONS}
          disabled={disabled}
          onChange={(jumpType) =>
            onChange({
              ...jump,
              jumpType,
              jumpEdge: jumpType === 'FLIP' || jumpType === 'LUTZ' ? jump.jumpEdge : 'CORRECT',
            })
          }
        />
        <LabeledSelect
          id={`${prefix}-rotation`}
          label="Rotation"
          value={jump.jumpRotation}
          options={JUMP_ROTATION_OPTIONS}
          disabled={disabled}
          onChange={(jumpRotation) => onChange({ ...jump, jumpRotation })}
        />
        {(jump.jumpType === 'FLIP' || jump.jumpType === 'LUTZ') && (
          <LabeledSelect
            id={`${prefix}-edge`}
            label="Edge"
            value={jump.jumpEdge}
            options={JUMP_EDGE_OPTIONS}
            disabled={disabled}
            onChange={(jumpEdge) => onChange({ ...jump, jumpEdge })}
          />
        )}
      </div>
    </fieldset>
  )
}

function SpinFields({
  element,
  disabled,
  onChange,
}: {
  element: SpinElement
  disabled: boolean
  onChange: (element: SpinElement) => void
}) {
  return (
    <div className="type-fields">
      <div className="field-grid">
        <LabeledSelect
          id="spin-type"
          label="Spin type"
          value={element.spinType}
          options={SPIN_TYPE_OPTIONS}
          disabled={disabled}
          onChange={(spinType) => onChange({ ...element, spinType })}
        />
        <LabeledSelect
          id="spin-level"
          label="Level"
          value={element.level}
          options={LEVEL_OPTIONS}
          disabled={disabled}
          onChange={(level) => onChange({ ...element, level })}
        />
      </div>
      <div className="check-grid">
        <CheckboxField
          id="spin-flying"
          label="Flying"
          checked={element.flying}
          disabled={disabled}
          onChange={(flying) => onChange({ ...element, flying })}
        />
        <CheckboxField
          id="spin-change-foot"
          label="Change foot"
          checked={element.changeFoot}
          disabled={disabled}
          onChange={(changeFoot) => onChange({ ...element, changeFoot })}
        />
        <CheckboxField
          id="spin-v-status"
          label="V mark"
          checked={element.vStatus}
          disabled={disabled}
          onChange={(vStatus) => onChange({ ...element, vStatus })}
        />
      </div>
    </div>
  )
}
