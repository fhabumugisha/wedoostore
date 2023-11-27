import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CalculatorService} from "./calculator.service";
import {CalculationResponse} from "./models/calculation-response";
import {ValueCard} from "./models/value-card";
import {CalculatorValueFormGroup} from "./models/calculator-value-form-group";

@Component({
  selector: 'app-calculator',
  standalone: true,
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.scss',
  imports: [CommonModule, FormsModule, ReactiveFormsModule ],

})
export class CalculatorComponent {

  private fb: FormBuilder = inject(FormBuilder);
  private calculateService: CalculatorService = inject(CalculatorService);

  public availableCards!: ValueCard;
  public ceilValue: number = 0;
  public floorValue: number = 0;
  showNextValue = false;
  private defaultAmount = 0;
  public calculatorForm: CalculatorValueFormGroup = this.fb.group({
    amount: [null, Validators.required]
  }) as CalculatorValueFormGroup;

  calculate() {

    let calculatorValue = this.calculatorForm.value;
    this.defaultAmount = Number(calculatorValue.amount!);
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
      this.showNextValue = false;

      //the desired amount is not possible,
    } else if (data.floor && data.ceil) {
      this.ceilValue = data.ceil.value;
      this.floorValue = data.floor.value;
      this.showNextValue = true;
      this.availableCards = {} as ValueCard;

      // the desired amount is higher or lower than the possible amounts,
    } else if (data.ceil && data.floor === null) {
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
    this.defaultAmount -=1;
    this.calculateService.calculate(this.defaultAmount).subscribe({
      next: data => {
        if(data.floor){
          this.defaultAmount = data.floor.value;
          this.setValue(data.floor.value);
        }
      },
      error: err => console.log(err)
    });
  }

  getNext() {
    this.defaultAmount +=1;
    console.log(this.defaultAmount)
    this.calculateService.calculate(this.defaultAmount).subscribe({
      next: data => {
        if(data.ceil){
          this.defaultAmount =  data.ceil.value;
          this.setValue(data.ceil.value);
        }
      },
      error: err => console.log(err)
    });
  }
}
