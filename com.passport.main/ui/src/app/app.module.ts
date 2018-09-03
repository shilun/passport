import {BrowserAnimationsModule} from '@angular/platform-browser/animations'; // this is needed!
import {NgModule} from '@angular/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

import {AppComponent} from './app.component';

import {CoreModule} from './core/core.module';
import {LayoutModule} from './layout/layout.module';
import {SharedModule} from './shared/shared.module';
import {RoutesModule} from './routes/routes.module';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {UserService} from './services/user.service';
import {ConfigService} from './services/config.service';
import {LoginService} from './services/login.service';
import {AdminService} from './services/admin.service';
import {RoleService} from './services/role.service';
import {GlobalService} from './services/global.service';
import {ProxyService} from './services/proxy.service';
import {ProxyBizService} from './services/proxy.biz.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserAnimationsModule,
    CoreModule,
    LayoutModule,
    SharedModule.forRoot(),
    RoutesModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    ConfigService,
    LoginService,
    UserService,
    AdminService,
    RoleService,
    GlobalService,
    ProxyService,
    ProxyBizService,
    {
      provide: LocationStrategy
      , useClass: HashLocationStrategy
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
