export class Decimal {
  private readonly scaled: bigint
  private readonly scale: number

  private constructor(scaled: bigint, scale: number) {
    this.scaled = scaled
    this.scale = scale
  }

  static parse(text: string): Decimal {
    const negative = text.trim().startsWith('-')
    const unsigned = negative ? text.trim().slice(1) : text.trim()
    const [whole = '0', fraction = ''] = unsigned.split('.')
    const digits = `${whole}${fraction}` || '0'
    const scaled = BigInt(digits)
    return new Decimal(negative ? -scaled : scaled, fraction.length)
  }

  static fromNumber(value: number): Decimal {
    if (!Number.isFinite(value)) {
      throw new Error('Score value is not a number.')
    }
    return Decimal.parse(value.toFixed(4))
  }

  static zero(): Decimal {
    return new Decimal(0n, 0)
  }

  add(other: Decimal): Decimal {
    const scale = Math.max(this.scale, other.scale)
    return new Decimal(this.align(scale) + other.align(scale), scale)
  }

  multiply(other: Decimal): Decimal {
    return new Decimal(this.scaled * other.scaled, this.scale + other.scale)
  }

  abs(): Decimal {
    return new Decimal(this.scaled < 0n ? -this.scaled : this.scaled, this.scale)
  }

  compare(other: Decimal): number {
    const scale = Math.max(this.scale, other.scale)
    const left = this.align(scale)
    const right = other.align(scale)
    if (left === right) return 0
    return left > right ? 1 : -1
  }

  isInteger(): boolean {
    return this.scaled % 10n ** BigInt(this.scale) === 0n
  }

  intValue(): number {
    return Number(this.setScale(0).scaled)
  }

  setScale(nextScale: number): Decimal {
    if (nextScale === this.scale) return this
    if (nextScale > this.scale) {
      return new Decimal(this.scaled * 10n ** BigInt(nextScale - this.scale), nextScale)
    }

    const factor = 10n ** BigInt(this.scale - nextScale)
    const sign = this.scaled < 0n ? -1n : 1n
    const absolute = this.scaled < 0n ? -this.scaled : this.scaled
    const quotient = absolute / factor
    const remainder = absolute % factor
    const rounded = remainder * 2n >= factor ? quotient + 1n : quotient
    return new Decimal(sign * rounded, nextScale)
  }

  toScore(): number {
    return Number(this.setScale(2).scaled) / 100
  }

  private align(scale: number): bigint {
    return this.setScale(scale).scaled
  }
}
