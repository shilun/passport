import { Component, OnInit } from '@angular/core';
import {GlobalService} from '../../../services/global.service';
import {AbstractController} from '../../../common/abstract.controller';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {RoleService} from '../../../services/role.service';
import {AdminService} from '../../../services/admin.service';

@Component({
  selector: 'app-proxy-view',
  templateUrl: './proxy-view.component.html',
  styleUrls: ['./proxy-view.component.scss']
})
export class ProxyViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;

  constructor(fm: FormBuilder, baseService: AdminService, protected roleService: RoleService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {pin:'',name: '', status: '', phone: '', id: '',url:'',linkMan:'',remark:''};
    this.valForm = this.buildFormGroup(fm);

  }


  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'name': [null, Validators.required],
      'status': [null, Validators.required],
      'phone': [null, Validators.required],
      'linkMan': [null, Validators.required]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    await this.viewById();
  }

  public save() {
    this.saveData('/system/proxy/list');
  }

  submitForm($ev, value: any) {
    this.save();
  }
}
