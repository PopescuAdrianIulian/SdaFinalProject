import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ParcelService} from '@service/parcel.service';
import {TrackingService} from '@service/tracking.service';
import {NgIf} from '@angular/common';
import {SupportTicketService} from '@service/support.service';
import {SupportTicketRequest} from '../../../../model/support.model';

@Component({
  selector: 'app-return-parcel',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './return-parcel.component.html',
  styleUrls: ['./return-parcel.component.css']
})
export class ReturnParcelComponent {
  awbNumber: string = '';
  returnReason: string = '';
  email: string = '';
  isAwbValid: boolean = false;
  awbNumberSubmitted: boolean = false;
  emailSubmitted: boolean = false;
  isSubmitted: boolean = false;

  constructor(
    private parcelService: ParcelService,
    private trackingService: TrackingService,
    private supportService: SupportTicketService
  ) {
  }

  validateAwbNumber(): boolean {
    return this.parcelService.getParcelStatusByAwb(this.awbNumber) != null;
  }

  onSubmit() {
    this.awbNumberSubmitted = true;
    this.emailSubmitted = true;

    if (this.isAwbValid && this.returnReason && this.email) {
      const supportTicket: SupportTicketRequest = {
        email: this.email,
        title: 'RETURN',
        description: `${this.returnReason} (AWB: ${this.awbNumber})`
      };

      this.trackingService.changeDeliveryStatus(this.awbNumber, 'CANCELED').subscribe();
      this.supportService.createSupportTicket(supportTicket).subscribe();

      this.isSubmitted = true;
      console.log('Return request submitted:', {
        awbNumber: this.awbNumber,
        returnReason: this.returnReason
      });

      setTimeout(() => {
        this.resetForm();
      }, 3000);
    }
  }

  onAwbNumberChange() {
    this.isAwbValid = this.validateAwbNumber();
  }

  resetForm() {
    this.awbNumber = '';
    this.returnReason = '';
    this.email = '';
    this.isAwbValid = false;
    this.awbNumberSubmitted = false;
    this.emailSubmitted = false;
    this.isSubmitted = false;
  }
}
