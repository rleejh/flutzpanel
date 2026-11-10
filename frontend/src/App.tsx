import { useMemo, useRef, useState, type FormEvent } from 'react'
import { createProgram, fetchProgram, updateProgram } from './api.ts'
import './App.css'
import { ComponentSliders } from './components/ComponentSliders.tsx'
import { DeductionEditor } from './components/DeductionEditor.tsx'
import { ElementBuilder } from './components/ElementBuilder.tsx'
import { ElementList } from './components/ElementList.tsx'
import { ProgramSettings } from './components/ProgramSettings.tsx'
import { ScoreResults } from './components/ScoreResults.tsx'
import { scoreProgram } from './liveScore.ts'
import { toProgramRequest, validateProgram } from './programDraft.ts'
import { readRecentPrograms, rememberProgram } from './recentPrograms.ts'
import type {
  ComponentScores,
  Discipline,
  DraftDeduction,
  DraftElement,
  ProgramElement,
  ProgramType,
} from './types.ts'

const initialComponents: ComponentScores = {
  COMPOSITION: 0,
  PRESENTATION: 0,
  SKATING_SKILLS: 0,
}

function App() {
  const [programId, setProgramId] = useState<number | null>(null)
  const [programIdInput, setProgramIdInput] = useState('')
  const [recentIds, setRecentIds] = useState<number[]>(() => readRecentPrograms())
  const [season, setSeason] = useState('2026-27')
  const [programType, setProgramType] = useState<ProgramType>('SHORT_PROGRAM')
  const [discipline, setDiscipline] = useState<Discipline>('MEN')
  const [elements, setElements] = useState<DraftElement[]>([])
  const [components, setComponents] = useState<ComponentScores>(initialComponents)
  const [deductions, setDeductions] = useState<DraftDeduction[]>([])
  const [saving, setSaving] = useState(false)
  const savingRef = useRef(false)
  const [loading, setLoading] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [loadError, setLoadError] = useState<string | null>(null)
  const busy = saving || loading

  const liveScore = useMemo(
    () =>
      scoreProgram({
        season,
        programType,
        discipline,
        elements: elements.map((item) => item.element),
        components,
        deductions,
      }),
    [season, programType, discipline, elements, components, deductions],
  )

  function addElement(element: ProgramElement) {
    setElements((current) => [...current, { clientId: crypto.randomUUID(), element }])
    setSaveError(null)
  }

  function removeElement(clientId: string) {
    setElements((current) => current.filter((item) => item.clientId !== clientId))
    setSaveError(null)
  }

  async function loadSavedProgram(id: number) {
    if (savingRef.current || loading) return

    setLoading(true)
    setLoadError(null)
    setSaveError(null)

    try {
      const loaded = await fetchProgram(id)
      setProgramId(loaded.id)
      setProgramIdInput(String(loaded.id))
      setSeason(loaded.season)
      setProgramType(loaded.programType)
      setDiscipline(loaded.discipline)
      setElements(
        loaded.elements.map((element) => ({
          clientId: crypto.randomUUID(),
          element: normalizeLoadedElement(element),
        })),
      )
      setComponents(loaded.components)
      setDeductions(
        loaded.deductions.map((deduction) => ({
          clientId: crypto.randomUUID(),
          type: deduction.type,
          value: deduction.value,
        })),
      )
      setRecentIds(rememberProgram(loaded.id))
    } catch (error) {
      setLoadError(error instanceof Error ? error.message : 'The program could not be loaded.')
    } finally {
      setLoading(false)
    }
  }

  function submitLoad(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const id = Number(programIdInput.trim())
    if (!Number.isInteger(id) || id <= 0) {
      setLoadError('Enter a saved program id.')
      return
    }
    void loadSavedProgram(id)
  }

  async function saveProgram() {
    if (savingRef.current) return

    const request = toProgramRequest({
      season,
      programType,
      discipline,
      elements,
      components,
      deductions,
    })
    const validationError = validateProgram(request)
    if (validationError) {
      setSaveError(validationError)
      return
    }

    savingRef.current = true
    setSaving(true)
    setSaveError(null)

    try {
      const savedId =
        programId == null ? await createProgram(request) : await updateProgram(programId, request)
      setProgramId(savedId)
      setProgramIdInput(String(savedId))
      setRecentIds(rememberProgram(savedId))
    } catch (error) {
      setSaveError(error instanceof Error ? error.message : 'The program could not be saved.')
    } finally {
      savingRef.current = false
      setSaving(false)
    }
  }

  return (
    <main className="app-shell">
      <header className="topbar">
        <a className="brand" href="#program-settings-heading">
          <span className="brand-mark">F</span>
          <span>
            flutz<span className="brand-light">panel</span>
          </span>
        </a>
      </header>

      <section className="page-heading">
        <h1>Program overview</h1>
        <form className="load-form" onSubmit={submitLoad}>
          <label className="field-label" htmlFor="program-id">
            Load program
          </label>
          <input
            id="program-id"
            inputMode="numeric"
            value={programIdInput}
            disabled={busy}
            placeholder="Program id"
            onChange={(event) => {
              setProgramIdInput(event.target.value)
              setLoadError(null)
            }}
          />
          <button className="secondary-button" type="submit" disabled={busy}>
            {loading ? 'Loading...' : 'Load'}
          </button>
          {recentIds.length > 0 && (
            <div className="recent-programs">
              {recentIds.map((id) => (
                <button
                  key={id}
                  className="recent-program"
                  type="button"
                  disabled={busy}
                  onClick={() => void loadSavedProgram(id)}
                >
                  {id}
                </button>
              ))}
            </div>
          )}
          {loadError && (
            <p className="banner-error" role="alert">
              {loadError}
            </p>
          )}
        </form>
      </section>

      <ProgramSettings
        season={season}
        programType={programType}
        discipline={discipline}
        disabled={busy}
        onSeasonChange={(value) => {
          setSeason(value)
          setSaveError(null)
        }}
        onProgramTypeChange={(value) => {
          setProgramType(value)
          setSaveError(null)
        }}
        onDisciplineChange={(value) => {
          setDiscipline(value)
          setSaveError(null)
        }}
      />

      <div className="workspace">
        <div className="builder-column">
          <ElementBuilder disabled={busy} onAdd={addElement} />
          <ComponentSliders
            values={components}
            factor={liveScore.factor}
            disabled={busy}
            onChange={(type, value) => {
              setComponents((current) => ({ ...current, [type]: value }))
              setSaveError(null)
            }}
          />
          <DeductionEditor
            deductions={deductions}
            disabled={busy}
            onChange={(next) => {
              setDeductions(next)
              setSaveError(null)
            }}
          />
        </div>

        <div className="results-column">
          <ElementList elements={elements} disabled={busy} onRemove={removeElement} />
          <ScoreResults score={liveScore} />
        </div>
      </div>

      <div className="save-bar">
        {saveError && (
          <p className="banner-error" role="alert">
            {saveError}
          </p>
        )}
        <button className="save-button" type="button" onClick={() => void saveProgram()} disabled={busy}>
          {saving ? 'Saving...' : 'Save'}
        </button>
      </div>

      <footer className="app-footer">
        <span>
          FLUTZPANEL <span className="footer-separator">/</span> FIGURE SKATING
        </span>
      </footer>
    </main>
  )
}

function normalizeLoadedElement(element: ProgramElement): ProgramElement {
  if (element.elementType !== 'JUMP_PASS') return element
  return {
    ...element,
    jumps: element.jumps.map((jump) => ({
      ...jump,
      jumpEdge: jump.jumpType === 'FLIP' || jump.jumpType === 'LUTZ' ? jump.jumpEdge : 'CORRECT',
    })),
  }
}

export default App
