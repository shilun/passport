import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AdminService} from '../../../services/admin.service';
import {GlobalService} from '../../../services/global.service';
import {RoleService} from '../../../services/role.service';
import {ProxyService} from "../../../services/proxy.service";

@Component({
  selector: 'app-admin-view',
  templateUrl: './proxy-pass.component.html'
})
export class ProxyPassComponent extends AbstractController implements OnInit {


  constructor(fm: FormBuilder, protected baseService: ProxyService, protected roleService: RoleService, protected globalService: GlobalService, route: ActivatedRoute, router: Router) {
    super(baseService, route, router);
    this.entity = {name: '', password:"",vpassword:"", id: ''};
    this.valForm = this.buildFormGroup(fm);

  }



  public buildFormGroup(fb: FormBuilder): FormGroup {
    return fb.group({
      'password': [null, Validators.required],
      'vpassword': [null, Validators.required]
    });
  }

  async ngOnInit() {
    await this.viewById();
  }

  public save() {
    if(this.entity.password!=this.entity.vpassword){
      alert("密码不一至");
      return ;
    }
    this.saveData('/system/proxy/list');
  }

  /**
   * 保存
   * @param {string} successUrl 保存成功跳转url
   */
  public async saveData(successUrl: string): Promise<void> {
    let result = await this.baseService.changePass(this.entity.id,this.entity.password);
    if (result.success) {
      this.router.navigate(successUrl.split('/'));
    }
    else {
      alert(result.message);
    }
  }

  submitForm($ev, value: any) {
    this.save();
  }
}
