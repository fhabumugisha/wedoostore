import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";

export const authenticationInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next:
  HttpHandlerFn) => {
  const userToken = 'tokenTest123';
  const modifiedReq = req.clone({
    headers: req.headers.set('Authorization', `${userToken}`),
  });

  return next(modifiedReq);
};
