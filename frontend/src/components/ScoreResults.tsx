import { formatScore } from '../format.ts'
import type { LiveProgramScore } from '../liveScore.ts'

type ScoreResultsProps = {
  score: LiveProgramScore
}

export function ScoreResults({ score }: ScoreResultsProps) {
  return (
    <section className="score-grid" aria-live="polite" aria-label="Program score summary">
      <article className="score-card score-primary">
        <span className="field-label">TOTAL SCORE</span>
        <ScoreValue value={score.totalProgramScore} />
      </article>
      <article className="score-card">
        <span className="field-label">TECHNICAL SCORE</span>
        <ScoreValue value={score.technicalScore} />
      </article>
      <article className="score-card">
        <span className="field-label">COMPONENT SCORE</span>
        <ScoreValue value={score.componentScore} />
        <span className="score-note">
          {score.factor == null ? 'No factor for this selection' : `Factor ${formatScore(score.factor)}`}
        </span>
      </article>
      <article className="score-card">
        <span className="field-label">DEDUCTIONS</span>
        <ScoreValue value={score.deductionsTotal} />
      </article>
      {score.error && (
        <p className="banner-error" role="alert">
          {score.error}
        </p>
      )}
    </section>
  )
}

function ScoreValue({ value }: { value: number | null }) {
  return <div className="score-value">{value == null ? '—' : formatScore(value)}</div>
}
