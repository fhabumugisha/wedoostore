import { AbstractControl, FormGroup } from '@angular/forms';
import {CalculatorValue} from "./calculator-value";
export interface CalculatorValueFormGroup  extends FormGroup {
  value : CalculatorValue;
  controls: {
    amount : AbstractControl
  }
}
