import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-info',
  standalone: true,
  imports: [],
  templateUrl: './info.component.html',
  styleUrl: './info.component.css'
})
export class InfoComponent {

  constructor(private router: Router) {
  }

  goToSupport() {
    this.router.navigate(['/support']);

  }
}
