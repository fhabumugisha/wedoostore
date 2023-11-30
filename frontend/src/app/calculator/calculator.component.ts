import {Component, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CalculatorService} from "./calculator.service";
import {CalculationResponse} from "./models/calculation-response";
import {ValueCard} from "./models/value-card";
import {CalculatorValueFormGroup} from "./models/calculator-value-form-group";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";

@Component({
  selector: 'app-calculator',
  standalone: true,
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.scss',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatButtonModule, MatIconModule, MatCardModule, MatInputModule],

})
export class CalculatorComponent {

  private fb: FormBuilder = inject(FormBuilder);
  private calculateService: CalculatorService = inject(CalculatorService);

  public availableCards!: ValueCard;
  public ceilValue: number = 0;
  public floorValue: number = 0;
  public showNextValue = signal(false);
  private defaultAmount = signal(0);
  public calculatorForm: CalculatorValueFormGroup = this.fb.group({
    amount: [null, Validators.required]
  }) as CalculatorValueFormGroup;

  calculate() {
    const calculatorValue = this.calculatorForm.value;
    this.defaultAmount.set(Number(calculatorValue.amount!));
    this.calculateService.calculate(calculatorValue.amount!).subscribe({
      next: data => {
        this.processData(data);
      },
      error: err => console.log(err)
    });

  }

  private processData(data: CalculationResponse) {
    //the desired amount is possible,
    if (data.equal) {
      this.availableCards = data.equal;
      this.showNextValue.set(false);

      //the desired amount is not possible,
    } else if (data.floor && data.ceil) {
      this.ceilValue = data.ceil.value;
      this.floorValue = data.floor.value;
      this.showNextValue.set(true);
      this.availableCards = {} as ValueCard;

      // the desired amount is higher or lower than the possible amounts,
    } else if (data.ceil && data.floor === undefined) {
      this.setValue(data.ceil.value);
    }
  }

  setValue(newValue: any) {
    this.calculatorForm.patchValue(
      {
        'amount': newValue
      });

    this.calculate();

  }

  getPrevious() {
    this.defaultAmount.update((oldValue) => oldValue - 1);
    this.processNextValues(false);
  }


  getNext() {
    this.defaultAmount.update((oldValue) => oldValue + 1);
    this.processNextValues(true);

  }

  private processNextValues(isNext: boolean) {
    this.calculateService.calculate(this.defaultAmount()).subscribe({
      next: data => {
        if (!isNext && data.floor) {
          this.setValue(data.floor.value);
        } else if (isNext && data.ceil) {
          this.setValue(data.ceil.value);
        }
      },
      error: err => console.log(err)
    });
  }
}
