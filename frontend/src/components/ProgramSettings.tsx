import { DISCIPLINE_OPTIONS, PROGRAM_TYPE_OPTIONS } from '../options.ts'
import type { Discipline, ProgramType } from '../types.ts'
import { LabeledSelect } from './Fields.tsx'

type ProgramSettingsProps = {
  season: string
  programType: ProgramType
  discipline: Discipline
  disabled?: boolean
  onSeasonChange: (season: string) => void
  onProgramTypeChange: (programType: ProgramType) => void
  onDisciplineChange: (discipline: Discipline) => void
}

export function ProgramSettings({
  season,
  programType,
  discipline,
  disabled = false,
  onSeasonChange,
  onProgramTypeChange,
  onDisciplineChange,
}: ProgramSettingsProps) {
  return (
    <section className="panel settings-panel" aria-labelledby="program-settings-heading">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">PROGRAM</p>
          <h2 id="program-settings-heading">Settings</h2>
        </div>
      </div>
      <div className="settings-grid">
        <div className="field">
          <label htmlFor="season">Season</label>
          <input
            id="season"
            value={season}
            disabled={disabled}
            autoComplete="off"
            onChange={(event) => onSeasonChange(event.target.value)}
          />
          <p className="field-note">The loaded scale of values and component factors are for 2026-27.</p>
        </div>
        <LabeledSelect
          id="program-type"
          label="Program type"
          value={programType}
          options={PROGRAM_TYPE_OPTIONS}
          disabled={disabled}
          onChange={onProgramTypeChange}
        />
        <LabeledSelect
          id="discipline"
          label="Discipline"
          value={discipline}
          options={DISCIPLINE_OPTIONS}
          disabled={disabled}
          onChange={onDisciplineChange}
        />
      </div>
    </section>
  )
}
