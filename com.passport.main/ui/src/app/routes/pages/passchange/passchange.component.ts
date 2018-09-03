import {Component, OnInit, Injector} from '@angular/core';
import {SettingsService} from '../../../core/settings/settings.service';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {LoginService} from "../../../services/login.service";

@Component({
  selector: 'app-lock',
  templateUrl: './passchange.component.html'
})
export class PasschangeComponent implements OnInit {

  valForm: FormGroup;
  router: Router;
  entity: any;

  constructor(public settings: SettingsService, protected loginService: LoginService, fb: FormBuilder, public injector: Injector) {
    this.entity = {oldPassword: null, newPassword: null};
    this.valForm = fb.group({
      'oldPassword': [null, Validators.required],
      'newPassword': [null, Validators.required]
    });

  }

  async submitForm($ev, value: any) {
    $ev.preventDefault();
    for (let c in this.valForm.controls) {
      this.valForm.controls[c].markAsTouched();
    }

    if (this.valForm.valid) {
      let result = await this.loginService.changePass(this.entity.oldPassword, this.entity.newPassword);
      if (result.success) {
        this.router.navigate(['home']);
      }
      else {
        alert(result.message);
      }
    }
  }
  homeNave(){
    this.router.navigate(['home']);
  }
  ngOnInit() {
    this.router = this.injector.get(Router);
  }

}
