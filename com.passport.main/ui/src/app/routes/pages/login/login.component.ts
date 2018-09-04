import {Component, OnInit} from '@angular/core';
import {SettingsService} from '../../../core/settings/settings.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TokenUtils} from '../../../common/token.utils';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from '../../../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  valForm: FormGroup;
  entity: { userName: '', password: '' };

  constructor(public settings: SettingsService, fb: FormBuilder, private loginService: LoginService,protected route: ActivatedRoute,protected router: Router) {
    this.entity = {userName: '', password: ''};
    this.valForm = fb.group({
      'userName': [null, Validators.required],
      'password': [null, Validators.required]
    });
  }

  submitForm($ev, value: any) {
    $ev.preventDefault();
    for (let c in this.valForm.controls) {
      this.valForm.controls[c].markAsTouched();
    }
    if (!this.valForm.valid) {
      return;
    }
    this.loginService.login(this.entity.userName, this.entity.password).then(result => {
      if (result.success) {
        //TokenUtils.upToken(result.data.token);
        this.router.navigate(["home"]);
      }
    });
  }

  ngOnInit() {

  }


}
