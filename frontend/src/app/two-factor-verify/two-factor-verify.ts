import {Component, signal} from '@angular/core';
import {TwoFactorService} from '../two-factor.service';
import {FormsModule} from '@angular/forms';
import {QRCodeComponent} from 'angularx-qrcode';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-two-factor-verify',
  imports: [
    FormsModule,
    QRCodeComponent,
    NgOptimizedImage
  ],
  templateUrl: './two-factor-verify.html',
  styleUrl: './two-factor-verify.css',
})
export class TwoFactorVerify {

  url: string = '';
  status = signal<'unknown' | 'success' | 'failed'>('unknown');

  constructor(private readonly twoFactorService: TwoFactorService) {
    this.twoFactorService.getTwoFactorUrl.subscribe(value => this.url = value);
  }

  protected changeModal() {
    this.twoFactorService.twoFactorDone = false;
  }

  protected submitCode(code: string) {
    try {
      this.twoFactorService.validateNumbers(code).subscribe({
        next: () => this.status.set('success'),
        error: () => this.status.set('failed')  ,
      });
    } catch (e) {
      console.log(e);
    }
    setTimeout(() => this.status.set('unknown'), 5000);
  }
}
