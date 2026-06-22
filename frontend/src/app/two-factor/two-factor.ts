import {Component} from '@angular/core';
import {TwoFactorService} from '../two-factor.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-two-factor',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './two-factor.html',
  styleUrl: './two-factor.css',
})
export class TwoFactor {

  constructor(private readonly twoFactorService: TwoFactorService) {
    this.twoFactorService.getTotpUrl();
  }

  enableTwoFactor() {
    this.twoFactorService.twoFactorDone = true;
  }
}
