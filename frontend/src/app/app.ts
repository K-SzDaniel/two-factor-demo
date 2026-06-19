import { Component, signal } from '@angular/core';
import {TwoFactor} from './two-factor/two-factor';
import {TwoFactorVerify} from './two-factor-verify/two-factor-verify';
import {TwoFactorService} from './two-factor.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [TwoFactor, TwoFactorVerify, NgOptimizedImage],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('two-factor-demo-FE');
  protected twoFactorDone: boolean = false;

  constructor(private readonly twoFactorService: TwoFactorService) {
   this.twoFactorService.getTwoFactorDone.subscribe(value => this.twoFactorDone = value);
  }
}
