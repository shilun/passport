///<reference path="../../../common/abstract.controller.ts"/>
import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ConfigService} from '../../../services/config.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CustomValidators} from 'ng2-validation';

@Component({
  selector: 'app-config-view',
  templateUrl: './config-view.component.html'
})
export class ConfigViewComponent extends AbstractController implements OnInit {


  constructor(fm: FormBuilder, baseService: ConfigService, route: ActivatedRoute, router: Router) {

    super(baseService, route, router);
    this.entity = {name: '', keyName: '', value: ''};
    this.valForm = this.buildFormGroup(fm);
  }


  public buildFormGroup(fb: FormBuilder): FormGroup {

    return fb.group({
      'name': [null, [Validators.required]],
      'keyName': [null, [Validators.required]],
      'value': [null, Validators.required],
    });
  }

  ngOnInit() {
    if (this.getRequest('id') != '') {
      this.viewById();
    }
  }

  public save(){
    this.saveData("/system/config/list");
  }

  submitForm($ev, value: any) {
    this.save();
  }
}
