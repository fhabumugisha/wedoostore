import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CalculatorService} from "./calculator.service";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";

@Component({
  selector: 'app-calculator',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule ],
  providers: [CalculatorService],
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.scss'

})
export class CalculatorComponent {

  private  fb:FormBuilder= inject(FormBuilder);
  private calculateService: CalculatorService = inject(CalculatorService);



  public calculatorForm =  this.fb.group({
    amount : [null,Validators.required]
  });

  calculate() {

    let amount = this.calculatorForm.value.amount;
    console.log(amount)
     this.calculateService.calculate(amount!).subscribe({
       next:value => console.log(value),
       error: err => console.log(err)
     });

  }
}
