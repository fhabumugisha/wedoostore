import { Routes } from '@angular/router';
import {CalculatorComponent} from "./calculator/calculator.component";

export const routes: Routes = [
  {
    path : 'cards', component : CalculatorComponent
  },
  {
    path : '', redirectTo :'/cards', pathMatch:"full"
  },


];
