import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {SupportTicketService} from "@service/support.service";
import {SupportTicketRequest} from "../../../model/support.model";

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgClass
  ],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  currentStep = 1;
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  supportTicket: SupportTicketRequest = {
    email: '',
    title: '',
    description: ''
  };

  constructor(private supportTicketService: SupportTicketService) {
  }

  nextStep() {
    if (this.currentStep < 3) {
      this.currentStep++;
    }
  }

  previousStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  submitTicket() {
    this.isLoading = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.supportTicketService.createSupportTicket(this.supportTicket).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Support ticket submitted successfully!';
        this.currentStep = 1;


        this.supportTicket = {
          email: '',
          title: '',
          description: ''
        };

      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to submit support ticket. Please try again.';
        console.error(error);
      }
    });
  }
}
