export function roundTo(value: number, digits: number): number {
  if (!Number.isFinite(value)) return 0
  const factor = 10 ** digits
  return Math.round((value + Number.EPSILON) * factor) / factor
}

export function clamp(value: number, min: number, max: number): number {
  return Math.min(max, Math.max(min, value))
}

export function formatScore(value: number): string {
  return roundTo(value, 2).toFixed(2)
}

export function formatSigned(value: number, digits = 2): string {
  const text = roundTo(value, digits).toFixed(digits)
  return value > 0 ? `+${text}` : text
}
