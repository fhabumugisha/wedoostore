import {ValueCard} from "./value-card";

export  interface CalculationResponse{
  equal?: ValueCard;
  ceil?: ValueCard;
  floor?: ValueCard;
}
