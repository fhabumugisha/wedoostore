import {HttpClient} from "@angular/common/http";
import {inject, Injectable} from "@angular/core";
import {CalculationResponse} from "./models/calculation-response";

@Injectable({
  providedIn: "root"
})
export class CalculatorService{
  private http:HttpClient = inject(HttpClient);

  calculateApiUrl =  "http://localhost:3000/shop/5/search-combination?amount=";
  calculate(amount:number | null){
    return this.http.get<CalculationResponse>(`${this.calculateApiUrl}${amount}` );
  }
}




