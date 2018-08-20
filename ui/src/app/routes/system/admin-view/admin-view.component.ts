import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AdminService} from '../../../services/admin.service';
import {GlobalService} from '../../../services/global.service';
import {RoleService} from '../../../services/role.service';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html'
})
export class AdminViewComponent extends AbstractController implements OnInit {

  statuses: Array<any>;
  sexTypes: Array<any>;
  roles: Array<any>;

  constructor(fm: FormBuilder, baseService: AdminService, protected roleService: RoleService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {name: '', status: '', phone: '', email: '', sexType: '', roles: [], pin: '', id: ''};
    this.valForm = this.buildFormGroup(fm);

  }


  upChecket(e: any, checket: boolean) {
    for (let item of this.roles) {
      if (e.value == item.id) {
        if (checket) {
          item.checked = 'checked';
        }
        else {
          item.checked = '';
        }
        break;
      }
    }
  }

  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'name': [null, Validators.required],
      'status': [null, Validators.required],
      'phone': [null, Validators.required],
      'email': [null, Validators.required],
      'sexType': [null, Validators.required]
    });
  }

  async ngOnInit() {
    let result = await this.globalService.list('yesorno');
    if (result.success) {
      this.statuses = result.data.list;
    }
    result = await this.globalService.list('sextype');
    if (result.success) {
      this.sexTypes = result.data.list;
    }
    result = await this.roleService.list({pageinfo: {page: 0, size: 100}});
    if (result.success) {
      this.roles = result.data.list;
    }
    await this.viewById();

    for (let roleItem of this.roles) {
      roleItem.checked = '';
      for (let role of this.entity.roles) {
        if (roleItem.id == role) {
          roleItem.checked = 'checked';
          break;
        }
      }
    }
  }

  public save() {
    this.saveData('/system/admin/list');
  }

  submitForm($ev, value: any) {
    let roles = [];
    for (let roleItem of this.roles) {
      if (roleItem.checked != '') {
        roles.push(roleItem.id);
      }
    }
    this.entity.roles=roles;
    this.save();
  }
}
