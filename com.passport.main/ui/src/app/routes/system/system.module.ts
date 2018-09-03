import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConfigListComponent} from './config-list/config-list.component';
import {ConfigViewComponent} from './config-view/config-view.component';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '../../shared/shared.module';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {Ng2TableModule} from 'ng2-table';
import {UserListComponent} from './user-list/user-list.component';
import {UserViewComponent} from './user-view/user-view.component';
import { AdminListComponent } from './admin-list/admin-list.component';
import { AdminViewComponent } from './admin-view/admin-view.component';
import { RoleListComponent } from './role-list/role-list.component';
import { RoleViewComponent } from './role-view/role-view.component';
import { ProxyListComponent } from './proxy-list/proxy-list.component';
import { ProxyViewComponent } from './proxy-view/proxy-view.component';
import {AdminPassComponent} from "./admin-pass/admin-pass.component";

const routes: Routes = [
  {path: 'config/list', component: ConfigListComponent},
  {path: 'config/view', component: ConfigViewComponent},
  {path: 'user/list', component: UserListComponent},
  {path: 'user/view', component: UserViewComponent},
  {path: 'admin/list', component: AdminListComponent},
  {path: 'admin/view', component: AdminViewComponent},
  {path: 'admin/changePass', component: AdminPassComponent},
  {path: 'role/list', component: RoleListComponent},
  {path: 'role/view', component: RoleViewComponent},
  {path: 'proxy/list', component: ProxyListComponent},
  {path: 'proxy/view', component: ProxyViewComponent},
  ];

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes),
    Ng2TableModule,
    NgxDatatableModule
  ],
  declarations: [
    ConfigListComponent, ConfigViewComponent, UserListComponent, UserViewComponent, AdminListComponent, AdminViewComponent,AdminPassComponent, RoleListComponent, RoleViewComponent, ProxyListComponent, ProxyViewComponent]
})


export class SystemModule {
}
