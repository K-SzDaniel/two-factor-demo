import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {TwoFactorResponse} from './model/two-factor-response';

@Injectable({
  providedIn: 'root',
})
export class TwoFactorService {

  private readonly _twoFactorDone = new BehaviorSubject<boolean>(false);
  private readonly backendDomain = 'http://host.docker.internal:8080';
  private readonly _twoFactorUrl = new BehaviorSubject<string>('');

  constructor(private readonly http: HttpClient) {
  }


  get getTwoFactorUrl(): BehaviorSubject<string> {
    return this._twoFactorUrl;
  }

  get getTwoFactorDone(): Observable<boolean> {
    return this._twoFactorDone.asObservable();
  }

  set twoFactorDone(value: boolean) {
    this._twoFactorDone.next(value);
  }

  getTotpUrl() {
    this.http.post<TwoFactorResponse>(`${this.backendDomain}/api/v1/two-factor/generate-url`, {}).subscribe(value => this._twoFactorUrl.next(value.url));
  }

  validateNumbers(number: string) : Observable<HttpResponse<unknown>> {
    return this.http.post<HttpResponse<unknown>>(`${this.backendDomain}/api/v1/two-factor/validate`, {"code": number});
  }

  resetTwoFactorSecret() {
    this.http.post<HttpResponse<unknown>>(`${this.backendDomain}/api/v1/two-factor/reset`, null)
      .subscribe(() => this.getTotpUrl());
  }
}
