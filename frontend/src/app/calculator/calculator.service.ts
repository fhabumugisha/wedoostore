import {HttpClient} from "@angular/common/http";
import {inject, Injectable} from "@angular/core";
import {CalculationResponse} from "./models/calculation-response";

@Injectable()
export class CalculatorService{
  private http:HttpClient = inject(HttpClient);

  calculateApiUrl =  "http://localhost:3000/shop/5/search-combination?amount=";
  calculate(amount:string | null){
    return this.http.get<CalculationResponse[]>(`${this.calculateApiUrl}${amount}` );
  }
}




