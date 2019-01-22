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
import {AdminListComponent} from './admin-list/admin-list.component';
import {AdminViewComponent} from './admin-view/admin-view.component';
import {RoleListComponent} from './role-list/role-list.component';
import {RoleViewComponent} from './role-view/role-view.component';
import {ProxyListComponent} from './proxy-list/proxy-list.component';
import {ProxyViewComponent} from './proxy-view/proxy-view.component';
import {AdminPassComponent} from './admin-pass/admin-pass.component';
import {ProxyPassComponent} from "./proxy-pass/proxy-pass.component";
import {ServerListComponent} from './server-list/server-list.component';
import {ServerViewComponent} from './server-view/server-view.component';
import { SoftwareListComponent } from './software-list/software-list.component';
import { SoftwareViewComponent } from './software-view/software-view.component';
import {FileUploadModule} from 'ng2-file-upload';
import {ProxyUserListComponent} from "./proxy-user-list/proxy-user-list.component";
import {ProxyUserViewComponent} from "./proxy-user-view/proxy-user-view.component";
import {ProxyUserPassComponent} from "./proxy-user-pass/proxy-user-pass.component";
import {ProxyConfigListComponent} from "./proxy-confg-list/proxy-config-list.component";
import {ProxyConfigViewComponent} from "./proxy-config-view/proxy-config-view.component";
import {SmsListComponent} from "./sms-list/sms-list.component";


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
  {path: 'proxyuser/list', component: ProxyUserListComponent},
  {path: 'proxyconfig/list', component: ProxyConfigListComponent},
  {path: 'proxyconfig/view', component: ProxyConfigViewComponent},
  {path: 'proxyuser/view', component: ProxyUserViewComponent},
  {path: 'proxyuser/pass', component: ProxyUserPassComponent},
  {path: 'proxy/changePass', component: ProxyPassComponent},
  {path: 'server/list', component: ServerListComponent},
  {path: 'server/view', component: ServerViewComponent},
  {path: 'software/list', component: SoftwareListComponent},
  {path: 'software/view', component: SoftwareViewComponent},
  {path: 'sms/list', component: SmsListComponent},

];

@NgModule({
  imports: [
    CommonModule,
    FileUploadModule,
    SharedModule,
    RouterModule.forChild(routes),
    Ng2TableModule,
    NgxDatatableModule
  ],
  declarations: [
    ConfigListComponent, ConfigViewComponent, UserListComponent, UserViewComponent, AdminListComponent, AdminViewComponent,
    AdminPassComponent, RoleListComponent, RoleViewComponent, ProxyListComponent, ProxyViewComponent,ProxyPassComponent,
    ServerListComponent,ServerViewComponent, SoftwareListComponent, SoftwareViewComponent,ProxyUserListComponent,ProxyUserViewComponent,ProxyUserPassComponent,
    ProxyConfigListComponent,ProxyConfigViewComponent
  ]
})


export class SystemModule {
}
