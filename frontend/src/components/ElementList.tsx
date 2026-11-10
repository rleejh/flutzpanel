import { elementCode, elementName } from '../elementDisplay.ts'
import { formatScore, formatSigned } from '../format.ts'
import { scoreElement } from '../liveScore.ts'
import type { DraftElement, ProgramElement } from '../types.ts'

type ElementListProps = {
  elements: DraftElement[]
  disabled?: boolean
  onRemove: (clientId: string) => void
}

export function ElementList({ elements, disabled = false, onRemove }: ElementListProps) {
  return (
    <section className="panel" aria-labelledby="elements-heading">
      <div className="panel-heading">
        <h2 id="elements-heading">Technical Elements</h2>
        <span className="count-badge">{elements.length} items</span>
      </div>

      {elements.length === 0 ? (
        <div className="empty-state">
          <span className="empty-icon" aria-hidden="true">
            ＋
          </span>
          <strong>No elements yet</strong>
          <p>Build an element on the left, then add it to this program.</p>
        </div>
      ) : (
        <ol className="element-list">
          {elements.map((item, index) => {
            const code = elementCode(item.element)
            const name = elementName(item.element)
            return (
              <li className="element-row" key={item.clientId}>
                <span className="element-number">{String(index + 1).padStart(2, '0')}</span>
                <div className="element-info">
                  <strong>{code || 'Incomplete element'}</strong>
                  <span>{name}</span>
                  <ElementMetrics element={item.element} />
                </div>
                <button
                  className="remove-button"
                  type="button"
                  disabled={disabled}
                  onClick={() => onRemove(item.clientId)}
                  aria-label={`Remove ${name}`}
                  title="Remove element"
                >
                  ×
                </button>
              </li>
            )
          })}
        </ol>
      )}
    </section>
  )
}

export function ElementMetrics({ element }: { element: ProgramElement }) {
  const scored = scoreElement(element)
  if (!scored.ok) {
    return <p className="field-error">{scored.message}</p>
  }

  return (
    <dl className="element-metrics">
      <div>
        <dt>Base value</dt>
        <dd>{formatScore(scored.score.baseValue)}</dd>
      </div>
      <div>
        <dt>GOE</dt>
        <dd>{formatSigned(scored.score.goeContribution)}</dd>
      </div>
      <div>
        <dt>Total</dt>
        <dd>{formatScore(scored.score.total)}</dd>
      </div>
    </dl>
  )
}
