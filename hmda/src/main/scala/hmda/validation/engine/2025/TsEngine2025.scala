package hmda.validation.engine

import hmda.model.filing.ts.TransmittalSheet
import hmda.validation.context.ValidationContext
import hmda.validation.rules.ts.syntactical.{ S300, S302 }
import hmda.validation.rules.ts.validity._
import hmda.validation.rules.ts.quality._2025._
// $COVERAGE-OFF$
private[engine] object TsEngine2025 extends ValidationEngine[TransmittalSheet] {

  override def syntacticalChecks(ctx: ValidationContext) = Vector(
    S300,
    S302.withContext(ctx)
  )

  override def validityChecks(ctx: ValidationContext) = Vector(
    V600,
    V601,
    V602,
    V603,
    V604,
    V605,
    V606,
    V607,
    V717,
    V719_1,
    V719_2
  )

  override def qualityChecks(ctx: ValidationContext) = Vector(
    Q303.withContext(ctx)
  )

}
// $COVERAGE-ON$