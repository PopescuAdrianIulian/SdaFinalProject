import {Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component'
import {LoginComponent} from './components/login/login.component'
import {CreateAccountComponent} from './components/create-account/create-account.component'
import {SendParcelComponent} from "./components/send-parcel/send-parcel.component";
import {FindParcelsComponent} from "./components/find-parcels/find-parcels.component";
import {DashboardComponent} from "./components/courier-dashboard/dashboard.component";
import {SupportComponent} from "./components/support/support.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'createAccount', component: CreateAccountComponent},
  {path: 'sendParcel', component: SendParcelComponent},
  {path: 'myParcels', component: FindParcelsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'support', component: SupportComponent},


];
