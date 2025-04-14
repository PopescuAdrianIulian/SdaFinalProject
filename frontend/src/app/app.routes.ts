import {Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component'
import {LoginComponent} from './components/user/login/login.component'
import {CreateAccountComponent} from './components/user/create-account/create-account.component'
import {SendParcelComponent} from "./components/parcel/send-parcel/send-parcel.component";
import {FindParcelsComponent} from "./components/parcel/find-parcels/find-parcels.component";
import {DashboardComponent} from "./components/courier/courier-dashboard/dashboard.component";
import {SupportComponent} from "./components/support/add-support-ticket/support.component";
import {SupportTicketsComponent} from "./components/support/support-tickets/support-tickets.component";
import {InfoComponent} from "./components/support/info/info.component";
import {ReturnParcelComponent} from "./components/parcel/return-parcel/return-parcel.component";
import {AdminDashboardComponent} from "./components/admin/admin-dashboard/admin-dashboard.component";
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'createAccount', component: CreateAccountComponent},
  {path: 'sendParcel', component: SendParcelComponent},
  {path: 'myParcels', component: FindParcelsComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'add-support', component: SupportComponent},
  {path: 'tickets', component: SupportTicketsComponent},
  {path: 'info', component: InfoComponent},
  {path: 'returnParcel', component: ReturnParcelComponent},
  {path: 'admin-dashboard', component: AdminDashboardComponent},


];
