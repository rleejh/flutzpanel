const STORAGE_KEY = 'flutzpanel.recentPrograms'

export function readRecentPrograms(): number[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (!stored) return []
    const parsed = JSON.parse(stored) as unknown
    if (!Array.isArray(parsed)) return []
    return parsed.filter((id): id is number => typeof id === 'number' && Number.isInteger(id) && id > 0).slice(0, 8)
  } catch {
    return []
  }
}

export function rememberProgram(id: number): number[] {
  const next = [id, ...readRecentPrograms().filter((stored) => stored !== id)].slice(0, 8)
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(next))
  } catch {
    return next
  }
  return next
}
